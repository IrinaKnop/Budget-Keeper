package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoriesDto {
    private List<CategoryDto> incomeList;
    private List<CategoryDto> expensesList;
}
