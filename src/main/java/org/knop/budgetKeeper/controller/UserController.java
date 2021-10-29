package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.LoginDto;
import org.knop.budgetKeeper.dto.RegistrationDto;
import org.knop.budgetKeeper.dto.RegistrationResultDto;
import org.knop.budgetKeeper.dto.UserIdDto;
import org.knop.budgetKeeper.models.User;
import org.knop.budgetKeeper.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto login) {
        User user = userService.login(login);
        if (user == null) {
            return ResponseEntity.status(400).body(new RegistrationResultDto(null, "Неверный логин или пароль"));
        } else {
            return ResponseEntity.ok(new RegistrationResultDto(user, null));
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<RegistrationResultDto> registration(@RequestBody RegistrationDto registration) {
        RegistrationResultDto user = userService.registration(registration);
        if (user.getUser() == null) {
            return ResponseEntity.status(400).body(user);
        } else {
            return ResponseEntity.ok(user);
        }
    }

    @PostMapping("/checkAuth")
    public ResponseEntity<?> checkAuth(@RequestBody UserIdDto userId) {
        User user = userService.id(userId);
        if (user == null) {
            return ResponseEntity.status(400).body(new RegistrationResultDto(null, "Неверный логин или пароль"));
        } else {
            return ResponseEntity.ok(new RegistrationResultDto(user, null));
        }
    }
}
