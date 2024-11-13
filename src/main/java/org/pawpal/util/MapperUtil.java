package org.pawpal.util;

import org.pawpal.dto.PetDTO;
import org.pawpal.dto.UserDTO;
import org.pawpal.model.Pet;
import org.pawpal.model.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;

public class MapperUtil {
    public static UserDTO toUserDTO(User user) {
        List<PetDTO> petsDTO = user.getPets().stream()
                .map(MapperUtil::toPetDTO)
                .toList();
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.isNew(), petsDTO, user.getRoles());
    }

    public static PetDTO toPetDTO(Pet pet) {
        /*String base64Image = "";
        if (pet.getImageData() != null) {
            base64Image = Base64.getEncoder().encodeToString(pet.getImageData());
        }*/
        return new PetDTO(pet.getId(), pet.getName(), pet.getBreed(), pet.getAge(), pet.getWeight(), "null", pet.isGender());
    }

    public static Pet toPet(PetDTO petDTO, User user) throws IOException {
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setName(petDTO.getName());
        pet.setBreed(petDTO.getBreed());
        pet.setAge(petDTO.getAge());
        pet.setWeight(petDTO.getWeight());
        pet.setGender(petDTO.isGender());
        pet.setOwner(user);
        pet.setImageData(null);
        /*if (petDTO.getImage() != null && !petDTO.getImage().isEmpty()) {
            System.out.println("poza");
            Path path = Paths.get(petDTO.getImage());
            byte[] imageBytes = Files.readAllBytes(path);
            pet.setImageData(imageBytes);
        }*/
        return pet;
    }

    public static User toUser(UserDTO userDTO) {
        User user = new User();
        user.setId(userDTO.getId());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        List<Pet> userPets = userDTO.getPets().stream().map(p -> {
            try {
                return MapperUtil.toPet(p, user);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).toList();
        user.setPets(userPets);
        user.setNew(userDTO.isNew());
        return user;
    }
}
