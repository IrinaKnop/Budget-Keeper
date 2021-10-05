package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.knop.budgetKeeper.models.User;


@Data
@AllArgsConstructor
public class RegistrationResultDto {
    private User user;
    private String message;
}
