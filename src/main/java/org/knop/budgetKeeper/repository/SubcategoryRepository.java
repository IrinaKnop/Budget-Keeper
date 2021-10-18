package org.knop.budgetKeeper.repository;

import org.knop.budgetKeeper.models.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Integer> {
    Optional<Subcategory> findByNameAndCategoryId(String name, Integer categoryId);
    List<Subcategory> findAllByUserIdAndCategoryId(Integer userId, Integer categoryId);
}
