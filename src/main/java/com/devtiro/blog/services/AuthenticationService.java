package com.devtiro.blog.services;

import com.devtiro.blog.dto.AuthResponse;
import com.devtiro.blog.dto.LoginRequest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {

    UserDetails authenticate(String email, String password);
    String generateToken(UserDetails userDetails);
    UserDetails validateToken(String token);

}
