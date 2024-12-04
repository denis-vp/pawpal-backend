package org.pawpal.service;

import lombok.AllArgsConstructor;
import org.pawpal.dto.PetDTO;
import org.pawpal.dto.RegisterDTO;
import org.pawpal.dto.UserDTO;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.model.Role;
import org.pawpal.model.User;
import org.pawpal.repository.RoleRepository;
import org.pawpal.repository.UserRepository;
import org.pawpal.util.MapperUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final PetService petService;

    @Transactional
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public boolean findByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public User findUserByEmail(String email) {
        Optional<User> user =  userRepository.findByEmail(email);
        if(user.isPresent())
            return user.get();
        else
            throw new ResourceNotFoundException("User not found with email: " + email);
    }

    public User findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent())
            return user.get();
        else
            throw new ResourceNotFoundException("User not found with ID: " + id);
    }

    public List<UserDTO> getUsersDTO() {
        return userRepository.findAll().stream().map(MapperUtil::toUserDTO).toList();
    }

    public User addUser(RegisterDTO user) {
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        String password = PasswordService.generatePassword();
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setNew(true);
        Optional<Role> optionalUserRole = roleRepository.findByName("ROLE_USER");
        Role userRole;
        if(optionalUserRole.isEmpty()) {
            userRole = new Role();
            userRole.setName("ROLE_USER");
            roleRepository.save(userRole);
        } else {
            userRole = optionalUserRole.get();
        }
        newUser.setRoles(Set.of(userRole));
        try {
            return userRepository.save(newUser);
        } catch (IllegalArgumentException exception){
            throw new SaveRecordException("Error creating user:" + exception.getMessage());
        } catch (RuntimeException exception) {
            throw new SaveRecordException("An unexpected error occurred while creating the user", exception);
        }
    }

    public User updateUser(User user) {
        try {
            return userRepository.save(user);
        } catch(IllegalArgumentException exception) {
            throw new SaveRecordException("Error updating user:" + exception.getMessage());
        } catch(RuntimeException exception) {
            throw new SaveRecordException("An unexpected error occurred while updating the user", exception);
        }
    }

    public void deleteUser(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        if(optionalUser.isEmpty())
            throw new ResourceNotFoundException("User not found with ID: " + id);
        userRepository.delete(optionalUser.get());
    }

    public PetDTO addPetForUser(String email, PetDTO petDTO) throws SaveRecordException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        return petService.createPet(user, petDTO);
    }

    public void updatePetForUser(String email, Long id, PetDTO petDTO) throws SaveRecordException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        petService.updatePet(id, petDTO, user);
    }
}
