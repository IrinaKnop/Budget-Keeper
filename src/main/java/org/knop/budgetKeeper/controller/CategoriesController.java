package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.CategoriesDto;
import org.knop.budgetKeeper.dto.CategoryDto;
import org.knop.budgetKeeper.dto.SubcategoriesDto;
import org.knop.budgetKeeper.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/getAvailableCategories")
    public ResponseEntity<CategoriesDto> getAvailableCategories(@RequestParam Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new CategoriesDto(Collections.emptyList(), Collections.emptyList()));
        }
        else {
            return ResponseEntity.ok(categoryService.getAvailableCategories(userId));
        }
    }

    @GetMapping("/getAllCategoriesByPeriod")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesByPeriod(@RequestParam Integer userId,
                                                                      @RequestParam Date dateStart,
                                                                      @RequestParam Date dateEnd) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(categoryService.getAllCategories(userId, dateStart, dateEnd));
        }
    }

    @PostMapping("/addCategory")
    public ResponseEntity<CategoryDto> addCategory(@RequestBody CategoryDto categoryDto) {
        if (categoryDto.getUserId() == null) {
            return ResponseEntity.status(401).body(null);
        }
        else {
            return ResponseEntity.ok(categoryService.createNewCategory(categoryDto));
        }
    }

    @PostMapping("/addSubcategory")
    public ResponseEntity<SubcategoriesDto> addSubcategory(@RequestBody SubcategoriesDto subcategoriesDto) {
        if (subcategoriesDto.getUserId() == null) {
            return ResponseEntity.status(401).body(null);
        }
        else {
            return ResponseEntity.ok(categoryService.createNewSubcategory(subcategoriesDto));
        }
    }

}
