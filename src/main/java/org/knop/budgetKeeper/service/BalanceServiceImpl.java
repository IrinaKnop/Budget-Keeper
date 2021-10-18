package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.BalanceDto;
import org.knop.budgetKeeper.dto.BalanceInitialDto;
import org.knop.budgetKeeper.dto.DailyLimitDto;
import org.knop.budgetKeeper.dto.UserIdDto;
import org.knop.budgetKeeper.models.Balance;
import org.knop.budgetKeeper.models.User;
import org.knop.budgetKeeper.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class BalanceServiceImpl implements BalanceService{

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserService userService;

    @Override
    public BalanceDto getCurrentBalance(Integer userId) {
        Date timeStart = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date timeEnd = Date.valueOf(LocalDate.now());
        Optional<Balance> balanceOptional = balanceRepository.findByUserIdAndMonthBetween(userId, timeStart, timeEnd);
        if (balanceOptional.isPresent()) {
            return new BalanceDto(balanceOptional.get());
        }
        else {
            return new BalanceDto(new Balance());
        }
    }

    @Override
    @Transactional
    public Balance createInitialBalance(BalanceInitialDto balanceInitialDto) {
        Date nowDate = Date.valueOf(LocalDate.now());
        User user = userService.id(new UserIdDto(balanceInitialDto.getUserId()));
        BigDecimal balance = balanceInitialDto.getInitialBalanceValue();
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
           balance = BigDecimal.ZERO;
        }
        Balance newBalance = new Balance(-1, user, balance, balance, nowDate, true);
        return balanceRepository.save(newBalance);
    }

    @Override
    public DailyLimitDto getDailyLimit(Integer userId) {
        LocalDate now = LocalDate.now();
        LocalDate nextMonth = LocalDate.now().plusMonths(1).withDayOfMonth(1);
        long numberOfDays = DAYS.between(now, nextMonth);
        BigDecimal currentBalance = this.getCurrentBalance(userId).getFinalBalance();
        return new DailyLimitDto(currentBalance.divide(BigDecimal.valueOf(numberOfDays), RoundingMode.CEILING));
    }
}
