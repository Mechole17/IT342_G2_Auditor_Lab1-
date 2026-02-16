package com.auditor.userauth.service;

import com.auditor.userauth.dto.LoginRequestDTO;
import com.auditor.userauth.dto.LoginResponseDTO;
import com.auditor.userauth.dto.RegistrationRequestDTO;
import com.auditor.userauth.entity.User;
import com.auditor.userauth.repository.UserRepository;
import com.auditor.userauth.security.TokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenProvider tokenProvider){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
    }

    public User createUser(RegistrationRequestDTO userDto){
        Optional<?> existingUser = userRepository.findByEmail(userDto.getEmail());

        if(existingUser.isPresent()){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        return userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO loginDto) {
        // 1. Find user by email
        User user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Check if raw password matches hashed password in DB
        // matches(rawPassword, encodedPassword)
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // 3. Generate the token using the User ID
        String token = tokenProvider.createToken(user.getUserid());

        // 4. Return the response DTO
        return new LoginResponseDTO(token,user.getFirstname(), user.getLastname(), user.getEmail());
    }
}
