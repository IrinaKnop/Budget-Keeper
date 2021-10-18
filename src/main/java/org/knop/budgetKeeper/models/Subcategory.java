package org.knop.budgetKeeper.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "Subcategories")
@NoArgsConstructor
@AllArgsConstructor
public class Subcategory {

    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "Category_id")
    @Getter
    @Setter
    private Category category;

    @ManyToOne
    @JoinColumn(name = "User_id")
    @Getter
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
