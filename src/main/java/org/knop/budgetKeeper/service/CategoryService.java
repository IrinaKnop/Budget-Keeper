package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.CategoriesDto;
import org.knop.budgetKeeper.models.Category;
import org.knop.budgetKeeper.models.User;

import java.util.List;

public interface CategoryService {
    Category createNewCategory(String name);
    void createDefaultCategory(User user);
    CategoriesDto getAvailableCategories(Integer userId);
}
