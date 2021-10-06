package org.knop.budgetKeeper.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;

@Entity
@Table(name = "Payments")
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private BigInteger id;

    @ManyToOne
    @JoinColumn(name = "Account_Id")
    @Getter
    private Account account;

    @ManyToOne
    @JoinColumn(name = "User_id")
    @Getter
    private User user;

    @ManyToOne
    @JoinColumn(name = "Category_id")
    @Getter
    private Category category;

    @ManyToOne
    @JoinColumn(name = "Subcategory_id")
    @Getter
    private Subcategory subcategory;

    @Column(name = "Income_label")
    @Getter
    @Setter
    private Boolean incomeLabel;

    @Column(name = "Value")
    @Getter
    @Setter
    private BigDecimal value;

    @Column(name = "Date")
    @Getter
    @Setter
    private Date date;
}
