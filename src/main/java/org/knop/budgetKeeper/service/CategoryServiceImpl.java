package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.CategoriesDto;
import org.knop.budgetKeeper.dto.CategoryDto;
import org.knop.budgetKeeper.dto.SubcategoriesDto;
import org.knop.budgetKeeper.models.Category;
import org.knop.budgetKeeper.models.User;
import org.knop.budgetKeeper.repository.CategoryRepository;
import org.knop.budgetKeeper.repository.SubcategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    private final static List<Category> defaultCategories = List.of(
            new Category(-1, null, "Заработная плата", false, true),
            new Category(-1, null, "Премии", false, true),
            new Category(-1, null, "Дивиденды", false, true),
            new Category(-1, null, "Другое", false, true),
            new Category(-1, null, "Еда / продукты", false, false),
            new Category(-1, null, "Домашние животные", false, false),
            new Category(-1, null, "Бытовые нужды", false, false),
            new Category(-1, null, "Автомобиль", false, false),
            new Category(-1, null, "Здоровье / медицина", false, false),
            new Category(-1, null, "Коммунальные платежи", false, false),
            new Category(-1, null, "Красота / уход", false, false),
            new Category(-1, null, "Техника", false, false),
            new Category(-1, null, "Дети", false, false),
            new Category(-1, null, "Кредиты", false, false),
            new Category(-1, null, "Общественный транспорт", false, false),
            new Category(-1, null, "Другое", false, false),
            new Category(-1, null, "Вредные привычки", true, false)
    );

    @Override
    public Category createNewCategory(String name) {
        return null;
    }

    @Override
    public void createDefaultCategory(User user) {
        defaultCategories.forEach(
                it-> {
                    it.setUser(user);
                    categoryRepository.save(it);
                }
        );
    }

    @Override
    public CategoriesDto getAvailableCategories(Integer userId) {
        List<CategoryDto> incomeList = categoryRepository.findAllByUserIdAndIncomeLabel(userId, true)
                .stream()
                .map(CategoryDto::new)
                .map(
                        it-> {
                            it.setListSubcategories(
                                    subcategoryRepository
                                            .findAllByUserIdAndCategoryId(userId, it.getId())
                                            .stream()
                                            .map(SubcategoriesDto::new)
                                            .collect(Collectors.toList()));
                            return it;
                        }
                )
                .collect(Collectors.toList());
        List<CategoryDto> expensesList = categoryRepository.findAllByUserIdAndIncomeLabel(userId, false)
                .stream()
                .map(CategoryDto::new)
                .map(
                        it-> {
                            it.setListSubcategories(
                                    subcategoryRepository
                                            .findAllByUserIdAndCategoryId(userId, it.getId())
                                            .stream()
                                            .map(SubcategoriesDto::new)
                                            .collect(Collectors.toList()));
                            return it;
                        }
                )
                .collect(Collectors.toList());
        return new CategoriesDto(
                incomeList,
                expensesList
        );
    }
}
