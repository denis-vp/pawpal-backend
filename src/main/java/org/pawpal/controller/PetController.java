package org.pawpal.controller;

import lombok.AllArgsConstructor;
import org.pawpal.dto.PetDTO;
import org.pawpal.exception.DeleteRecordException;
import org.pawpal.exception.ResourceNotFoundException;
import org.pawpal.exception.SaveRecordException;
import org.pawpal.model.User;
import org.pawpal.service.PetService;
import org.pawpal.service.UserService;
import org.pawpal.validator.PetDTOValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/pets")
@AllArgsConstructor
public class PetController {
    private final PetService petService;
    private final UserService userService; //not best practice, only services can access different services
    private final PetDTOValidator petDTOValidator;

    @GetMapping("/all")
    public ResponseEntity<List<PetDTO>> getPetsByUserEmail() {
        try {
            List<PetDTO> pets = petService.getPetsByUserEmail();
            if(pets.isEmpty()){
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Collections.emptyList());
            }
            return ResponseEntity.status(HttpStatus.OK).body(pets);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Long id) {
        try {
            PetDTO petDTO = petService.getPetById(id);
            return ResponseEntity.ok(petDTO);
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<String> createPet(@RequestBody PetDTO petDTO) {
        if(!petDTOValidator.isValid(petDTO)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try{
            // find a new way to pass the user, maybe through front-end. a controller should depend on its service only
            User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(petService.createPet(user, petDTO).getId().toString());
        } catch (SaveRecordException exception){
          return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePet(@PathVariable Long id, @RequestBody PetDTO petDTO) {
        if(!petDTOValidator.isValid(petDTO)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        try {
            // find a new way to pass the user, maybe through front-end. a controller should depend on its service only
            User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            PetDTO persistedPet = petService.updatePet(id, petDTO, user);
            return ResponseEntity.ok(persistedPet.getId().toString());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).eTag("Path not found").body(null);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        try {
            petService.deletePet(id);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException exception) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (DeleteRecordException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/addpetdata")
    public ResponseEntity<Void> addHardcodedPetsForUser() {
        try {
            // find a new way to pass the user, maybe through front-end. a controller should depend on its service only
            User user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
            petService.addHardcodedPetsForUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(null);
        } catch (SaveRecordException exception) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(null);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
