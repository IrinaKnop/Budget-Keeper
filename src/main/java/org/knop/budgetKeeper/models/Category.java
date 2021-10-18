package org.knop.budgetKeeper.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Categories")
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "User_id")
    @Getter
    @Setter
    private User user;

    @Column(name = "Name")
    @Getter
    @Setter
    private String name;

    @Column(name="Useless_type")
    @Getter
    @Setter
    private Boolean uselessType;

    @Column(name = "Income_label")
    @Getter
    @Setter
    private Boolean incomeLabel;
}
