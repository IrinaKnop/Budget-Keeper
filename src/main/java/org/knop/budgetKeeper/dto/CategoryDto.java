package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knop.budgetKeeper.models.Category;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    public CategoryDto(Category category) {
        this.id = category.getId();
        this.userId = category.getUser().getId();
        this.name = category.getName();
        this.uselessType = category.getUselessType();
        this.incomeLabel = category.getIncomeLabel();

    }

    private Integer id;
    private Integer userId;
    private String name;
    private Boolean uselessType;
    private Boolean incomeLabel;
    private List<SubcategoriesDto> listSubcategories;
}
