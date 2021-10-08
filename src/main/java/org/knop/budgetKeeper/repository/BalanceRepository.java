package org.knop.budgetKeeper.repository;

import org.knop.budgetKeeper.models.Balance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<Balance, Integer> {
    Optional<Balance> findByUserIdAndDateBetween(Integer userId, Date timeStart, Date timeEnd);
}
