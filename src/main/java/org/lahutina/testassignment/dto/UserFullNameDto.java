package org.lahutina.testassignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Dto to update only first name and last name of the user.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserFullNameDto {
    private String firstName;
    private String lastName;
}
