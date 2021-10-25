package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.models.Balance;
import org.knop.budgetKeeper.models.Payment;
import org.knop.budgetKeeper.models.User;
import org.knop.budgetKeeper.repository.BalanceRepository;
import org.knop.budgetKeeper.repository.PaymentRepository;
import org.knop.budgetKeeper.repository.PlanRepository;
import org.knop.budgetKeeper.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;

@Component
public class ScheduledService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Scheduled(cron="0 0 0 1 1/1 *")
    public void reinitializeBalance() {
        userRepository.findAll().forEach(
                it-> {
                    LocalDate pastMonthStart = LocalDate.now().minusMonths(1).withDayOfMonth(1);
                    LocalDate pastMonthEnd = LocalDate.now().minusMonths(1);
                    pastMonthEnd.withDayOfMonth(pastMonthEnd.lengthOfMonth());
                    balanceRepository.findByUserIdAndMonthBetween(it.getId(),
                            Date.valueOf(pastMonthStart),
                            Date.valueOf(pastMonthEnd)).ifPresent(
                                    balance -> {
                                        Balance newBalance = new Balance(
                                                -1,
                                                it,
                                                balance.getFinalBalance(),
                                                balance.getFinalBalance(),
                                                Date.valueOf(LocalDate.now()),
                                                true
                                        );
                                        balanceRepository.save(newBalance);
                                    }
                    );
                }
        );
    }

    @Scheduled(cron="0 0 0 1 1/1 *")
    public void checkPlans() {
       planRepository.findAll()
               .stream()
               .filter(it -> !it.getIsAccumulate())
               .forEach(
                       plan -> {
                         User user = plan.getUser();
                         LocalDate now = LocalDate.now();
                         LocalDate pastMonthStart = now.minusMonths(1).withDayOfMonth(1);
                         LocalDate pastMonthEnd = now.minusMonths(1);
                         pastMonthEnd.withDayOfMonth(pastMonthEnd.lengthOfMonth());

                         LocalDate pastPeriodStart = now.minusMonths(2).withDayOfMonth(1);
                         LocalDate pastPeriodEnd = now.minusMonths(2);
                         pastPeriodEnd.withDayOfMonth(pastPeriodEnd.lengthOfMonth());
                         Integer currentProgress =
                                 paymentRepository.findAllByUserIdAndDateBetween(user.getId(), Date.valueOf(pastPeriodStart), Date.valueOf(pastPeriodEnd))
                                         .stream()
                                         .map(Payment::getValue)
                                         .reduce(BigDecimal::add).orElse(BigDecimal.ZERO).intValue()
                                -paymentRepository.findAllByUserIdAndDateBetween(user.getId(), Date.valueOf(pastMonthStart), Date.valueOf(pastMonthEnd))
                                         .stream()
                                         .map(Payment::getValue)
                                         .reduce(BigDecimal::add).orElse(BigDecimal.ZERO)
                                         .intValue();

                         if(currentProgress <= 0) {
                             plan.setProgress(0.0);
                         } else {
                             plan.setProgress((currentProgress.doubleValue() / plan.getValue()) * 100);
                         }

                         planRepository.save(plan);
                       });
    }

}
