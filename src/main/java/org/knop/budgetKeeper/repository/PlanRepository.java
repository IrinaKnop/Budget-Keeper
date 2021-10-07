package org.knop.budgetKeeper.repository;

import org.knop.budgetKeeper.models.Payment;
import org.knop.budgetKeeper.models.Plan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Integer> {
    List<Plan> findAllByUserId(Integer userId);
}
