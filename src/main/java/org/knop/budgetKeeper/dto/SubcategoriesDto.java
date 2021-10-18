package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knop.budgetKeeper.models.Category;
import org.knop.budgetKeeper.models.Subcategory;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoriesDto {
    public SubcategoriesDto(Subcategory subcategory) {
        this.id = subcategory.getId();
        this.name = subcategory.getName();
        this.uselessType = subcategory.getUselessType();
        this.incomeLabel = subcategory.getIncomeLabel();
    }

    private Integer id;
    private String name;
    private Boolean uselessType;
    private Boolean incomeLabel;
}
