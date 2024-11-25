package org.pawpal.util;

import org.pawpal.dto.PetDTO;
import org.pawpal.dto.UserDTO;
import org.pawpal.dto.VeterinaryAppointmentDTO;
import org.pawpal.model.Pet;
import org.pawpal.model.User;
import org.pawpal.model.VeterinaryAppointment;

import java.util.List;

public class MapperUtil {
    public static UserDTO toUserDTO(User user) {
        List<PetDTO> petsDTO = user.getPets().stream()
                .map(MapperUtil::toPetDTO)
                .toList();
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword(), user.isNew(), user.getRoles(), petsDTO);
    }

    public static PetDTO toPetDTO(Pet pet) {
        /*String base64Image = "";
        if (pet.getImageData() != null) {
            base64Image = Base64.getEncoder().encodeToString(pet.getImageData());
        }*/
        return new PetDTO(pet.getId(), pet.getName(), pet.getBreed(), pet.getDateOfBirth(), pet.getType(), pet.getWeight(), pet.isMale(), "null");
    }

    public static Pet toPet(PetDTO petDTO, User user) {
        Pet pet = new Pet();
        pet.setId(petDTO.getId());
        pet.setName(petDTO.getName());
        pet.setBreed(petDTO.getBreed());
        pet.setDateOfBirth(petDTO.getDateOfBirth());
        pet.setType(petDTO.getType());
        pet.setWeight(petDTO.getWeight());
        pet.setMale(petDTO.isMale());
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
        List<Pet> userPets = userDTO.getPets().stream().map(p -> MapperUtil.toPet(p, user)).toList();
        user.setPets(userPets);
        user.setNew(userDTO.isNew());
        return user;
    }

    public static VeterinaryAppointment toVeterinaryAppointment(VeterinaryAppointmentDTO veterinaryAppointmentDTO) {
        VeterinaryAppointment veterinaryAppointment = new VeterinaryAppointment();
        veterinaryAppointment.setId(veterinaryAppointmentDTO.getId());
        veterinaryAppointment.setLocalDateTime(veterinaryAppointmentDTO.getLocalDateTime());
        veterinaryAppointment.setDuration(veterinaryAppointmentDTO.getDuration());
        veterinaryAppointment.setCost(veterinaryAppointmentDTO.getCost());
        return veterinaryAppointment;
    }

    public static VeterinaryAppointmentDTO toVeterinaryAppointmentDTO(VeterinaryAppointment veterinaryAppointment) {
        return new VeterinaryAppointmentDTO(veterinaryAppointment.getId(), veterinaryAppointment.getUser().getId(), veterinaryAppointment.getPet().getId(), veterinaryAppointment.getStatus(), veterinaryAppointment.getLocalDateTime(), veterinaryAppointment.getDuration(), veterinaryAppointment.getCost());
    }
}
