package com.auditor.userauth.controller;

import com.auditor.userauth.dto.UserDetailsDTO;
import com.auditor.userauth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final AuthService authService;

    public UserController(AuthService authService){
        this.authService = authService;
    }

    // Your instructor's required endpoint
    @GetMapping("/me")
    public ResponseEntity<UserDetailsDTO> getMe(Authentication authentication) {
        // authentication.getName() returns the email you set in the JwtFilter
        String email = authentication.getName();

        // This calls the method you just wrote in AuthService
        return ResponseEntity.ok(authService.getUserDetails(email));
    }
}
