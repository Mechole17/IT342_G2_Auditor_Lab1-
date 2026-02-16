package com.auditor.userauth.controller;

import com.auditor.userauth.dto.LoginRequestDTO;
import com.auditor.userauth.dto.LoginResponseDTO;
import com.auditor.userauth.dto.RegistrationRequestDTO;
import com.auditor.userauth.entity.User;
import com.auditor.userauth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> createUser(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO){
        return ResponseEntity.ok(authService.createUser(registrationRequestDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginDto) {
        // This will return the token + email if successful
        LoginResponseDTO response = authService.login(loginDto);
        return ResponseEntity.ok(response);
    }


}
