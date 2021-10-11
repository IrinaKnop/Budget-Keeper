package org.knop.budgetKeeper.service;

import org.apache.commons.validator.routines.EmailValidator;
import org.knop.budgetKeeper.dto.LoginDto;
import org.knop.budgetKeeper.dto.RegistrationDto;
import org.knop.budgetKeeper.dto.RegistrationResultDto;
import org.knop.budgetKeeper.dto.UserIdDto;
import org.knop.budgetKeeper.models.Account;
import org.knop.budgetKeeper.models.User;
import org.knop.budgetKeeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountService accountService;

    @Override
    public User login(LoginDto loginDto) {
        Optional<User> userOptional = userRepository.findByLogin(loginDto.getLogin());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (!user.getPassword().equals(loginDto.getPassword())) {
                return null;
            }
            return user;
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public RegistrationResultDto registration(RegistrationDto registrationDto) {
        RegistrationResultDto result = validateNewUser(registrationDto);
        if (result.getUser() == null) {
            return result;
        }
        Account userAccount = accountService.createAccount(result.getUser().getLogin());
        result.getUser().setAccount(userAccount);
        User user = userRepository.save(result.getUser());
        if (user == null) {
            return new RegistrationResultDto(user, "Internal server error");
        }
        return new RegistrationResultDto(user, "");
    }

    @Override
    public User id(UserIdDto userIdDto) {
        Optional<User> userOptional = userRepository.findById(userIdDto.getUserId());
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        else {
            return null;
        }
    }

    private RegistrationResultDto validateNewUser(RegistrationDto registrationDto) {
        EmailValidator validator = EmailValidator.getInstance();
        if ((!registrationDto.getName().matches("(^[A-Z]{1}[a-z]{1}([-a-z]{0,1})?([A-Za-z]{0,1})?([a-z]{0,11})?([-]{0,1})?([A-Z]{0,1})?([a-z]{0,14})?$)|(^[А-Я]{1}[а-я]{1}([-а-я]{0,1})?([А-Яа-я]{0,1})?([а-я]{0,11})?([-]{0,1})?([А-Я]{0,1})?([а-я]{0,14})?$)"))
         || (!registrationDto.getLastName().matches("(^[A-Z]{1}[a-z]{1}([-a-z]{0,1})?([A-Za-z]{0,1})?([a-z]{0,11})?([-]{0,1})?([A-Z]{0,1})?([a-z]{0,14})?$)|(^[А-Я]{1}[а-я]{1}([-а-я]{0,1})?([А-Яа-я]{0,1})?([а-я]{0,11})?([-]{0,1})?([А-Я]{0,1})?([а-я]{0,14})?$)"))) {
            return  new RegistrationResultDto(null,"Invalid name or last name");
        }
        if (!validator.isValid(registrationDto.getEmail())){
            return  new RegistrationResultDto(null,"Invalid email");
        }

        Optional<User> userOptional = userRepository.findByLogin(registrationDto.getLogin());
        if (userOptional.isPresent()) {
            return new RegistrationResultDto(null, "Login already in use");
        }

        Optional<User> optionalUser = userRepository.findByEmail(registrationDto.getEmail());
        if (optionalUser.isPresent()) {
            return new RegistrationResultDto(null, "E-mail already in use");
        }

        return new RegistrationResultDto(new User(registrationDto),"");
    }
}
