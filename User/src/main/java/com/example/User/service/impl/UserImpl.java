package com.example.User.service.impl;

import com.example.User.exception.UnauthorizedException;
import com.example.User.model.User;
import com.example.User.repository.UserRepository;
import com.example.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;
import java.util.stream.Collectors;
import java.time.temporal.ChronoUnit;


import com.example.User.exception.UnauthorizedException;
import com.example.User.model.User;
import com.example.User.repository.UserRepository;
import com.example.User.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Service;

@Service
public class UserImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtEncoder jwtEncoder;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtDecoder jwtDecoder;

    @Override
    public String generateToken(String email, String password) {
        // Verificar se os campos de email e senha foram fornecidos
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            throw new IllegalArgumentException("O email e a senha são obrigatórios.");
        }

        // Verificar o usuário e a senha
        if (isValidUser(email, password)) {
            // Gerar token JWT

            Authentication authentication =
                    authenticationManager
                            .authenticate(new UsernamePasswordAuthenticationToken(
                                    email,
                                    password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            Instant now = Instant.now();

            String scope = authentication.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plus(10, ChronoUnit.HOURS))
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .build();

           return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        } else {
            throw new UnauthorizedException("Credenciais inválidas");
        }
    }

    @Override
    public User createUser(User user) {
        if (user.getUsername() == null || user.getPassword() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("Todos os campos (username, password e email) são obrigatórios.");
        }

        // Verificar se já existe um usuário com o mesmo username
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new IllegalArgumentException("Nome de usuário já está em uso.");
        }

        // Verificar se já existe um usuário com o mesmo email
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Endereço de email já está em uso.");
        }

        // Hash da senha antes de armazenar no banco de dados
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);

        // Salva o novo usuário no banco de dados
        return userRepository.save(user);
    }


    @Override
    public boolean findByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return user != null; // Retorna true se o usuário for encontrado, caso contrário, retorna false
    }

    private boolean isValidUser(String email, String password) {
        // Procura o usuário no banco de dados pelo email
        User user = userRepository.findByEmailCustomQuery(email);
        if (user != null) {
            // Verifica se a senha fornecida corresponde à senha armazenada no banco de dados
            return passwordEncoder.matches(password, user.getPassword());
        }
        return false; // Retorna falso se o usuário não existir
    }


    @Override
    public boolean isUserLoggedIn(String token) {
        try {
            // Decodificar o token JWT
            Jwt jwt = jwtDecoder.decode(token);

            // Obtém a data de expiração do token JWT
            Instant expiration = jwt.getExpiresAt();

            // Verificar se o token está expirado
            return expiration != null && expiration.isAfter(Instant.now());
        } catch (Exception e) {
            // Se ocorrer uma exceção ao decodificar o token, significa que o token está inválido ou expirado
            return false;
        }
    }




}
