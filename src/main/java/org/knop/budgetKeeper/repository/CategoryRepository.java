package org.knop.budgetKeeper.repository;

import org.knop.budgetKeeper.models.Category;
import org.knop.budgetKeeper.models.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByNameAndUserIdAndIncomeLabel(String name, Integer userId, Boolean incomeLabel);
    List<Category> findAllByUserIdAndIncomeLabel (Integer userId, Boolean incomeLabel);
}
