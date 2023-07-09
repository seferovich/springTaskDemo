package com.example.demoapp.services;

import com.example.demoapp.exceptions.ValidateInput;
import com.example.demoapp.models.User;
import com.example.demoapp.repository.UserRepository;
import com.example.demoapp.respones.LoginResponse;
import com.example.demoapp.respones.RegisterResponse;
import com.example.demoapp.security.JwtIssuer;
import com.example.demoapp.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtIssuer jwtIssuer;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final ValidateInput validateInput;

public ResponseEntity<LoginResponse> attemptLogin(String username, String password) {
    try {
        validateInput.validateLogin(username, password);

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            LoginResponse response = LoginResponse.builder()
                    .message("Invalid username or password.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (AuthenticationServiceException e) {
            LoginResponse response = LoginResponse.builder()
                    .message("An error occurred during authentication.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        var principal = (UserPrincipal) authentication.getPrincipal();

        var token = jwtIssuer.issue(username);

        LoginResponse response = LoginResponse.builder()
                .accessToken(token)
                .message("Successfully logged in.")
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    } catch (ValidateInput.InvalidInputException e) {
        LoginResponse response = LoginResponse.builder()
                .message(e.getMessage())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}

    public ResponseEntity<RegisterResponse> attemptRegister(String username, String password, String email) {
        try {
            validateInput.validate(username, password, email);
            User savedUser = new User();
            savedUser.setEmail(email);
            savedUser.setPassword(passwordEncoder.encode(password));
            savedUser.setUsername(username);
            userRepository.save(savedUser);

            RegisterResponse response = RegisterResponse.builder()
                    .user(savedUser)
                    .message("Successfully created.")
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (ValidateInput.InvalidInputException e) {
            RegisterResponse response = RegisterResponse.builder().message(e.getMessage()).build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (DataIntegrityViolationException e){

            if (e.getCause() instanceof ConstraintViolationException) {
                ConstraintViolationException constraintEx = (ConstraintViolationException) e.getCause();

                if (constraintEx.getConstraintName().equals("users_email_key")) {
                    RegisterResponse response = RegisterResponse.builder()
                            .message("The email provided is already registered.")
                            .build();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                if (constraintEx.getConstraintName().equals("users_username_key")) {
                    RegisterResponse response = RegisterResponse.builder()
                            .message("The username provided is already registered.")
                            .build();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

                }
            }
        }
        RegisterResponse response = RegisterResponse.builder()
                .message("An error occurred while registering. Please try again later.")
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);

    }
    }