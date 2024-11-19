package com.task_manager.user_service.controller;

import com.task_manager.user_service.dto.JwtRequest;
import com.task_manager.user_service.dto.RegistrationUserDto;
import com.task_manager.user_service.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto){
        return authService.createNewUser(registrationUserDto);
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader){
        return authService.validateToken(authHeader);
    }

}
