package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.CategoriesDto;
import org.knop.budgetKeeper.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

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
}
