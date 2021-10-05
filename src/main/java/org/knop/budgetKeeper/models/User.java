package org.knop.budgetKeeper.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.knop.budgetKeeper.dto.RegistrationDto;

import javax.persistence.*;

@Entity
@Table(name = "Users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    public User(RegistrationDto registrationDto) {
        this.id = -1;
        this.name = registrationDto.getName();
        this.lastName = registrationDto.getLastName();
        this.email = registrationDto.getEmail();
        this.login = registrationDto.getLogin();
        this.password = registrationDto.getPassword();
    }

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @Column(name = "Account_Id")
    @Getter
    @Setter
    private Integer accountId;

    @Column(name = "Login")
    @Getter
    @Setter
    private String login;

    @Column(name = "Password")
    @Getter
    @Setter
    private String password;

    @Column(name = "Name")
    @Getter
    @Setter
    private String name;

    @Column(name = "Last_name")
    @Getter
    @Setter
    private String lastName;

    @Column(name = "email")
    @Getter
    @Setter
    private String email;
}
