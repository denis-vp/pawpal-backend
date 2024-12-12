package org.pawpal.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDetails {
    private String firstName;
    private String lastName;
    private String email;
    private boolean isNew;
    private String image;
    private String imageType;
}
