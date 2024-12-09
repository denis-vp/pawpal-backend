package org.pawpal.controller;

import lombok.AllArgsConstructor;
import org.pawpal.dto.RegisterDTO;
import org.pawpal.dto.ResetPasswordDTO;
import org.pawpal.dto.UserDTO;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.model.User;
import org.pawpal.service.PasswordService;
import org.pawpal.service.PetService;
import org.pawpal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RequestMapping("/users")
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PetService petService;

    //@Secured("ROLE_ADMIN")
    @GetMapping("/all")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        try {
            List<UserDTO> users = userService.getUsersDTO();
            if(users.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUsersDTO());
        } catch(Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public ResponseEntity<Object> createUser(@RequestBody RegisterDTO user) {
        if(userService.findByEmail(user.getEmail()))
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email already exists");
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
        } catch(SaveRecordException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error creating user");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating user");
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("/del/{userId}")
    public ResponseEntity<Object> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("User deleted successfully!");
        } catch(ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("The user does not exist!");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting user");
        }
    }

    @Secured("ROLE_USER")
    @PostMapping("/reset")
    public ResponseEntity<Object> resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) {
        if(!PasswordService.isValid(resetPasswordDTO.getPassword()))
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password! Password must contain at least one uppercase letter, "
            + "one lowercase letter, one number, one special character");
        try {
            User user = userService.findUserByEmail(resetPasswordDTO.getEmail());
            user.setPassword(passwordEncoder.encode(resetPasswordDTO.getPassword()));
            user.setNew(false);
            userService.updateUser(user);
            return ResponseEntity.ok().body("Password reset successfully");
        } catch(ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch(SaveRecordException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error resetting password");
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error resetting password");
        }
    }

//    @Secured("ROLE_USER")
//    @PostMapping("/pets/add")
//    public ResponseEntity<String> addPet(@RequestBody PetDTO petDTO) {
//        try {
//            String email = SecurityContextHolder.getContext().getAuthentication().getName();
//            return ResponseEntity.status(HttpStatus.CREATED).body(userService.addPetForUser(email, petDTO).getId().toString());
//        } catch (SaveRecordException exception) {
//            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
//        } catch (Exception exception) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }
}
