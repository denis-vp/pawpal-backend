package org.pawpal.service;

import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import org.pawpal.dto.LoginDTO;
import org.pawpal.dto.RegisterDTO;
import org.pawpal.mail.EmailSender;
import org.pawpal.model.Role;
import org.pawpal.model.User;
import org.pawpal.repository.RoleRepository;
import org.pawpal.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final EmailSender emailSender;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User register(RegisterDTO registerDTO) throws MessagingException {
        User user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        String password = PasswordService.generatePassword();
        System.out.println("Generated password: " + password);
        user.setPassword(passwordEncoder.encode(password));
        user.setNew(true);
        Optional<Role> optionalUserRole = roleRepository.findByName("ROLE_USER");
        Role userRole = new Role();
        if(optionalUserRole.isEmpty()) {
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        } else {
            userRole = optionalUserRole.get();
        }

        user.setRoles(Set.of(userRole));
        user.setPets(new ArrayList<>());
        String recipient = user.getEmail();
        String subject = user.getFirstName() + " " + user.getLastName();
        String content = "You password is " + password;
        emailSender.sendEmail(recipient, subject, content);
        return userRepository.save(user);
    }

    public User login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        return (User) authentication.getPrincipal();
    }
}
