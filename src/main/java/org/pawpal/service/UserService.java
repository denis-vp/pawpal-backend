package org.pawpal.service;

import org.pawpal.dto.RegisterDTO;
import org.pawpal.model.Role;
import org.pawpal.model.User;
import org.pawpal.repository.RoleRepository;
import org.pawpal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean findByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User addUser(RegisterDTO user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        String password = PasswordService.generatePassword();
        System.out.println("Generated password: " + password);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setNew(true);
        Role userRole = roleRepository.findByName("ROLE_USER");
        if(userRole == null) {
            userRole = new Role("ROLE_USER");
            roleRepository.save(userRole);
        }
        newUser.setRoles(Set.of(userRole));
        return userRepository.save(newUser);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
