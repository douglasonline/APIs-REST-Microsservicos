package com.example.User.service;

import com.example.User.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    String generateToken(String email, String password);

    User createUser(User user);

    boolean findByUsername(String username);

    boolean isUserLoggedIn(String token);

}
