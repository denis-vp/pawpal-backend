package org.pawpal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pawpal.model.Role;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {
    private Long id;
    private String firstName ;
    private String lastName;
    private String email;
    private String password;
    private boolean isNew;
    private Set<Role> roles;
    private List<PetDTO> pets;
}
