package org.pawpal.service;

import org.pawpal.dto.LoginDTO;
import org.pawpal.dto.RegisterDTO;
import org.pawpal.model.Role;
import org.pawpal.model.User;
import org.pawpal.repository.RoleRepository;
import org.pawpal.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User register(RegisterDTO registerDTO) {
        User user = new User();
        user.setFirstName(registerDTO.getFirstName());
        user.setLastName(registerDTO.getLastName());
        user.setEmail(registerDTO.getEmail());
        String password = PasswordService.generatePassword();
        System.out.println("Generated password: " + password);
        user.setPassword(passwordEncoder.encode(password));
        user.setNew(true);
        Role userRole = roleRepository.findByName("ROLE_USER");
        if(userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }
        user.setRoles(Set.of(userRole));
        return userRepository.save(user);
    }

    public User login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        return (User) authentication.getPrincipal();
    }
}
