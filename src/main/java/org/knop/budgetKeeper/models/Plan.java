package org.knop.budgetKeeper.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "Plans")
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "User_id")
    @Getter
    private User user;

    @Column(name = "Name")
    @Getter
    @Setter
    private String name;

    @Column(name = "Value")
    @Getter
    @Setter
    private Integer value;

    @Column(name = "Date_start")
    @Getter
    @Setter
    private Date dateStart;

    @Column(name = "Date_ending")
    @Getter
    @Setter
    private Date dateEnding;

    @Column(name = "Progress")
    @Getter
    private Double progress;

    @Column(name = "Accumulate_flag")
    @Getter
    @Setter
    private Boolean accumulateFlag;

    @Column(name = "Is_open")
    @Getter
    @Setter
    private Boolean isOpen;
}
