package org.pawpal.controller;

import org.pawpal.dto.RegisterDTO;
import org.pawpal.dto.ResetPasswordDTO;
import org.pawpal.dto.UserDTO;
import org.pawpal.model.User;
import org.pawpal.service.PasswordService;
import org.pawpal.service.UserService;
import org.pawpal.util.MapperUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    //@Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public List<UserDTO> getAllUsers() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        List<User> users = userService.findAll();
        List<UserDTO> response = users.stream().map(MapperUtil::toUserDTO).toList();
        return response;
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public ResponseEntity<Object> createUser(@RequestBody RegisterDTO user) {
        if(userService.findByEmail(user.getEmail()))
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        User newUser = userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/del/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        User user = userService.findById(userId);
        userService.deleteUser(user);
        return ResponseEntity.ok().body("User deleted successfully");
    }

    @Secured("ROLE_USER")
    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        if(!PasswordService.isValid(resetPasswordDTO.getPassword()))
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password! Password must contain at least one uppercase letter, "
            + "one lowercase letter, one number, one special character");
        User user = userService.findUserByEmail(resetPasswordDTO.getEmail());
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
        user.setNew(false);
        userService.updateUser(user);
        return ResponseEntity.ok().body("Password reset successfully");
    }
}
