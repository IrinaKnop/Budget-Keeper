package org.knop.budgetKeeper.repository;

import org.knop.budgetKeeper.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findAllByUserId(Integer userId);
}
