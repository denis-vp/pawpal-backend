package org.pawpal.controller;

import org.pawpal.dto.RegisterDTO;
import org.pawpal.model.User;
import org.pawpal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public List<User> getAllUsers() {
        return userService.findAll();
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
}
