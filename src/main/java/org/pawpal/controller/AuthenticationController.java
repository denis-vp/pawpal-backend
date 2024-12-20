package org.pawpal.controller;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.pawpal.dto.LoginDTO;
import org.pawpal.dto.LoginResponse;
import org.pawpal.dto.RegisterDTO;
import org.pawpal.jwt.JwtService;
import org.pawpal.model.User;
import org.pawpal.repository.UserRepository;
import org.pawpal.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RestController
@AllArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    /*
        Creates an account for a user.
        @param the first name, last name and email of the user
        @return a message showing if the registration was successful
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody RegisterDTO registerDTO) {
        if(userRepository.findByEmail(registerDTO.getEmail()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An account with this email already exists");
        try{
            User user = authenticationService.register(registerDTO);
            return ResponseEntity.ok("Your account was created! Check your email");
        }
        catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to send email");
        }

    }

    /*
        Signs in an existent user
        @param the email and the password
        @return a jwt token, the expiration time and a flag showing if the user is new or not
        @throws BadCredentialsException if the login was not successful
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDTO loginDTO) {
        try {
            User user = authenticationService.login(loginDTO);
            String jwtToken = jwtService.generateToken(user);
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken(jwtToken);
            loginResponse.setFirstName(user.getFirstName());
            loginResponse.setLastName(user.getLastName());
            loginResponse.setNewUser(user.isNew());
            return ResponseEntity.ok(loginResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
