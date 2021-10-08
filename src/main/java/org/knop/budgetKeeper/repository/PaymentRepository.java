package org.knop.budgetKeeper.repository;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findAllByUserId(Integer userId);
    List<Payment> findAllByUserIdAndDateBetween(Integer userId, Date timeStart, Date timeEnd);
}
