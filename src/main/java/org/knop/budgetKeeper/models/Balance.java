package org.knop.budgetKeeper.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "Balances")
@NoArgsConstructor
@AllArgsConstructor
public class Balance {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "User_id")
    @Getter
    private User user;

    @Column(name = "Initial_balance")
    @Getter
    @Setter
    private BigDecimal initialBalance;

    @Column(name = "Final_balance")
    @Getter
    @Setter
    private BigDecimal finalBalance;

    @Column(name = "Month")
    @Getter
    @Setter
    private Date month;

    @Column(name = "Is_initialized")
    @Getter
    @Setter
    private Boolean isInitialized;
}
