package org.knop.budgetKeeper.dto;

import lombok.Data;

@Data
public class RegistrationDto {
    private String name;
    private String lastName;
    private String email;
    private String login;
    private String password;
}
