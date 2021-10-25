package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.CategoriesDto;
import org.knop.budgetKeeper.dto.CategoryDto;
import org.knop.budgetKeeper.dto.SubcategoriesDto;
import org.knop.budgetKeeper.models.Category;
import org.knop.budgetKeeper.models.User;

import java.sql.Date;
import java.util.List;

public interface CategoryService {
    CategoryDto createNewCategory(CategoryDto categoryDto);
    SubcategoriesDto createNewSubcategory(SubcategoriesDto subcategoriesDto);
    void createDefaultCategory(User user);
    CategoriesDto getAvailableCategories(Integer userId);
    List<CategoryDto> getAllCategories(Integer userId, Date dateStart, Date dateEnd);
}
