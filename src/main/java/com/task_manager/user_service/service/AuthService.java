package com.task_manager.user_service.service;

import com.task_manager.user_service.dto.JwtRequest;
import com.task_manager.user_service.dto.JwtResponse;
import com.task_manager.user_service.dto.RegistrationUserDto;
import com.task_manager.user_service.dto.UserDto;
import com.task_manager.user_service.entity.Role;
import com.task_manager.user_service.entity.User;
import com.task_manager.user_service.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest){
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        }catch (BadCredentialsException exception){
            return new ResponseEntity<>("Invalid login or password", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());

        String encodedPassword = userService.getEncodedPassword(authRequest.getUsername());

        String token = jwtTokenUtils.generateToken(userDetails, encodedPassword);

        return new ResponseEntity<>(new JwtResponse(token), HttpStatus.OK);
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto){
        if (!registrationUserDto.getPassword().equals(registrationUserDto.getConfirmPassword()))
            return new ResponseEntity<>("Passwords don't match", HttpStatus.BAD_REQUEST);
        if (userService.findByUsername(registrationUserDto.getUsername()).isPresent())
            return new ResponseEntity<>("Such user already exists", HttpStatus.BAD_REQUEST);

        User user = userService.createNewUser(registrationUserDto);
        return new ResponseEntity<>(new UserDto(user.getId(), user.getUsername(), user.getEmail()), HttpStatus.OK);
    }

    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String authHeader){
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid");
            }
            String token = authHeader.substring(7);
            jwtTokenUtils.validateToken(token);

            String username = jwtTokenUtils.getUsername(token);
//            List<String> roles = jwtTokenUtils.getRoles(token);
            String encodedPassword = jwtTokenUtils.getEncodedPassword(token);

            return ResponseEntity.ok("");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}
