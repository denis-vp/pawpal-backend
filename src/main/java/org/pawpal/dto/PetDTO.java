package org.pawpal.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.pawpal.model.AnimalType;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PetDTO {
    private Long id;
    private String name;
    private String breed;
    private LocalDate dateOfBirth;
    private AnimalType type;
    private double weight;
    @JsonProperty("isMale")
    private boolean isMale;
    private String image;
    private String imageType;
}
