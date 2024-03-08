package com.example.User.controller;

import com.example.User.exception.UnauthorizedException;
import com.example.User.model.LoginResponse;
import com.example.User.model.User;
import com.example.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@Validated
public class UserController {

    @Autowired
    UserService userService;

    @Operation(summary = "Create a new user")
    @PostMapping("/users")
    @ApiResponse(responseCode = "200", description = "User created")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        try {
            // Cria um novo usuário com os dados fornecidos
            User newUser = userService.createUser(user);
            return ResponseEntity.ok(newUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Falha ao criar usuário");
        }
    }

    @Operation(summary = "Generate JWT token")
    @PostMapping("/login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token generated"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> generateToken(@RequestBody User user) {
        try {
            // Gerar token JWT
            String token = userService.generateToken(user.getEmail(), user.getPassword());
            // Retorna a mensagem e o token para o cliente
            LoginResponse loginResponse = new LoginResponse("Usuário logado com sucesso", token);
            return ResponseEntity.ok(loginResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (UnauthorizedException e) {
            // Retorna uma mensagem de erro caso as credenciais sejam inválidas
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            // Retorna uma mensagem de erro genérica caso ocorra um erro inesperado
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao autenticar usuário");
        }
    }

    @GetMapping("/isLoggedIn")
    public ResponseEntity<?> isLoggedIn(@RequestHeader(name = "Authorization") String token) {
        boolean loggedIn = userService.isUserLoggedIn(token.replace("Bearer ", ""));
        if (loggedIn) {
            return ResponseEntity.ok("Usuário está logado.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não está logado ou o token expirou.");
        }
    }

}
