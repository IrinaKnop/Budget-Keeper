package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.LoginDto;
import org.knop.budgetKeeper.dto.RegistrationDto;
import org.knop.budgetKeeper.dto.RegistrationResultDto;
import org.knop.budgetKeeper.models.User;

/**
 * Service which provides logic on User entity like login, signUp etc...
 */
public interface UserService {

    User login (LoginDto loginDto);

    RegistrationResultDto registration (RegistrationDto registrationDto);

}
