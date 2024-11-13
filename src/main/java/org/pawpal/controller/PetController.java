package org.pawpal.controller;

import org.pawpal.dto.PetDTO;
import org.pawpal.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    /**
     * @return the user's List of PetDTO if id is found,
     **/
    @GetMapping("/all/email")
    public List<PetDTO> getPetsByUserEmail() {
        return petService.getPetsByUserEmail();
    }

    /**
     * @return a list of PetDto
     **/
    @GetMapping("/all")
    public List<PetDTO> getPets() {
        return petService.getAllPets();
    }


    /**
     * @param id the ID of the pet to retrieve
     * @return a ResponseEntity containing the PetDTO if found,
     * or a 404 Not Found status if the pet does not exist
     * @throws RuntimeException if the pet with the specified ID is not found in the system
     **/
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPet(@PathVariable Long id) {
        try {
            PetDTO petDTO = petService.getPetById(id);
            return ResponseEntity.ok(petDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * @return the PetDTO that was added,
     **/
    @PostMapping("/add")
    public String createPet(@RequestBody PetDTO petDTO) {
        try{
            return petService.createPet(petDTO).getId().toString();
        }
        catch (IOException e){
            return "not ok";
        }
    }

    /**
     * @param id the ID of the pet to update
     * @return a ResponseEntity containing the PetDTO if found,
     * or a 404 Not Found status if the pet does not exist
     * @throws RuntimeException if the pet with the specified ID is not found in the system
     **/
    @PostMapping("/{id}")
    public ResponseEntity<String> updatePet(@PathVariable Long id, @RequestBody PetDTO petDTO) {
        try {
            PetDTO persistedPet = petService.updatePet(id, petDTO);
            return ResponseEntity.ok(persistedPet.getId().toString());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).eTag("Path not found").body(null);
        }
    }

    /**
     * @param id the ID of the pet to update
     * @throws RuntimeException if the pet with the specified ID is not found in the system
     **/
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        try {
            petService.deletePet(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).eTag("Pet not found").build();
        }
    }
}
