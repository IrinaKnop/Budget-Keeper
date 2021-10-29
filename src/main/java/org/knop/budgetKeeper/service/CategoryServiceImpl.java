package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.CategoriesDto;
import org.knop.budgetKeeper.dto.CategoryDto;
import org.knop.budgetKeeper.dto.SubcategoriesDto;
import org.knop.budgetKeeper.models.*;
import org.knop.budgetKeeper.repository.CategoryRepository;
import org.knop.budgetKeeper.repository.PaymentRepository;
import org.knop.budgetKeeper.repository.SubcategoryRepository;
import org.knop.budgetKeeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private SubcategoryRepository subcategoryRepository;

    @Autowired
    private UserRepository userRepository;

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
    public CategoryDto createNewCategory(CategoryDto categoryDto) {
        Optional<User> user = userRepository.findById(categoryDto.getUserId());
        if (user.isPresent()) {
            Optional<Category> category = categoryRepository.findByNameAndUserIdAndIncomeLabel(
                    categoryDto.getName(),
                    categoryDto.getUserId(),
                    categoryDto.getIncomeLabel()
            );
            if (category.isEmpty()) {
                Category newCategory = new Category(-1,
                        user.get(),
                        categoryDto.getName(),
                        categoryDto.getUselessType(),
                        categoryDto.getIncomeLabel());
                categoryRepository.save(newCategory);
                return new CategoryDto(newCategory);
            }
            else return null;
        } else return null;
    }

    @Override
    public SubcategoriesDto createNewSubcategory(SubcategoriesDto subcategoriesDto) {
        Optional<User> user = userRepository.findById(subcategoriesDto.getUserId());
        Optional<Category> category = categoryRepository.findByNameAndUserIdAndIncomeLabel(
                subcategoriesDto.getCategoryName(),
                user.get().getId(),
                subcategoriesDto.getIncomeLabel());
        if (user.isPresent() && category.isPresent()) {
            Optional<Subcategory> subcategory = subcategoryRepository.findByNameAndCategoryId(
                    subcategoriesDto.getName(),
                    category.get().getId()
            );
            if (subcategory.isEmpty()) {
                Subcategory newSubcategory = new Subcategory(-1,
                        category.get(),
                        user.get(),
                        subcategoriesDto.getName(),
                        subcategoriesDto.getUselessType(),
                        subcategoriesDto.getIncomeLabel());
                subcategoryRepository.save(newSubcategory);
                return new SubcategoriesDto(newSubcategory);
            }
            else return null;
        }
        else return null;
    }

    @Override
    public void createDefaultCategory(User user) {
        defaultCategories.forEach(
                it -> {
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
                        it -> {
                            it.setListSubcategories(
                                    subcategoryRepository
                                            .findAllByUserIdAndCategoryId(userId, it.getId())
                                            .stream()
                                            .map(SubcategoriesDto::new)
                                            .collect(Collectors.toList()));
                            return it;
                        }
                )
                .sorted(Comparator.comparing(CategoryDto::getName))
                .collect(Collectors.toList());
        List<CategoryDto> expensesList = categoryRepository.findAllByUserIdAndIncomeLabel(userId, false)
                .stream()
                .map(CategoryDto::new)
                .map(
                        it -> {
                            it.setListSubcategories(
                                    subcategoryRepository
                                            .findAllByUserIdAndCategoryId(userId, it.getId())
                                            .stream()
                                            .map(SubcategoriesDto::new)
                                            .collect(Collectors.toList()));
                            return it;
                        }
                )
                .sorted(Comparator.comparing(CategoryDto::getName))
                .collect(Collectors.toList());
        return new CategoriesDto(
                incomeList,
                expensesList
        );
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer userId, Date dateStart, Date dateEnd) {
        return paymentRepository.findAllByUserIdAndDateBetween(userId, dateStart, dateEnd)
                .stream()
                .map(it -> it.getCategory())
                .distinct()
                .map(it -> new CategoryDto(it))
                .collect(Collectors.toList());
    }
}
