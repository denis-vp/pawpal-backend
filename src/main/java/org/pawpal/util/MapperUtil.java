package org.pawpal.util;

import org.pawpal.dto.PetDTO;
import org.pawpal.dto.UserDTO;
import org.pawpal.model.Pet;
import org.pawpal.model.User;

import java.util.Base64;
import java.util.List;

public class MapperUtil {
    public static UserDTO toUserDTO(User user) {
        List<PetDTO> petsDTO = user.getPets().stream()
                .map(MapperUtil::toPetDTO)
                .toList();
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.getPasswordAttempts(), user.isNew(), petsDTO);
    }

    public static PetDTO toPetDTO(Pet pet) {
        String base64Image = "";
        if (pet.getImageData() != null) {
            base64Image = Base64.getEncoder().encodeToString(pet.getImageData());
        }
        return new PetDTO(pet.getId(), pet.getName(), pet.getBreed(), pet.getAge(), pet.getWeight(), pet.getMedicalHistory(), pet.getOwner().getEmail(), base64Image);
    }

    public static Pet toPet(PetDTO petDTO, User user) {
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setName(petDTO.getName());
        pet.setBreed(petDTO.getBreed());
        pet.setAge(petDTO.getAge());
        pet.setWeight(petDTO.getWeight());
        pet.setMedicalHistory(petDTO.getMedicalHistory());
        pet.setOwner(user);
        if (petDTO.getImage() != null && !petDTO.getImage().isEmpty()) {
            byte[] imageData = Base64.getDecoder().decode(petDTO.getImage());
            pet.setImageData(imageData);
        }
        return pet;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setPasswordAttempts(userDTO.getPasswordAttempts());
        user.setNew(userDTO.isNew());
        return user;
    }
}