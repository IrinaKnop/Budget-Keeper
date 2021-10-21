package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.dto.PlanDto;
import org.knop.budgetKeeper.models.Balance;
import org.knop.budgetKeeper.models.Payment;
import org.knop.budgetKeeper.models.Plan;
import org.knop.budgetKeeper.models.User;
import org.knop.budgetKeeper.repository.BalanceRepository;
import org.knop.budgetKeeper.repository.PlanRepository;
import org.knop.budgetKeeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService{

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public PlanDto addPlan(PlanDto planDto) {
        Optional<User> user = userRepository.findById(planDto.getUserId());
        if (user.isPresent()) {
            Date timeStart = Date.valueOf(LocalDate.now().withDayOfMonth(1));
            Date timeEnd = Date.valueOf(LocalDate.now());
            Balance balance = balanceRepository.findByUserIdAndMonthBetween(user.get().getId(), timeStart, timeEnd).get();
            BigDecimal finalBalance = balance.getFinalBalance();
            Double progress = 0.0;
            if (planDto.getIsAccumulate()) {
                 progress = finalBalance
                        .divide(BigDecimal.valueOf(planDto.getValue()), 4, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal.valueOf(100)).doubleValue();
            }
            Plan newPlan = new Plan(-1,
                    user.get(),
                    planDto.getName(),
                    planDto.getValue(),
                    planDto.getDateStart(),
                    planDto.getDateEnding(),
                    progress,
                    planDto.getIsAccumulate(),
                    planDto.getIsOpen());
            planRepository.save(newPlan);
            return new PlanDto(newPlan);
        } else {
            return null;
        }
    }

    @Override
    public List<PlanDto> getAllUserPlans(Integer userId) {
        return planRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Plan::getDateStart, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(PlanDto::new)
                .sorted(Comparator.comparing(PlanDto::getId))
                .collect(Collectors.toList());
    }
}
