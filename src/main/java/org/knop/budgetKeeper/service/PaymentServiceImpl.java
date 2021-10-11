package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.dto.PaymentsShortStatsDto;
import org.knop.budgetKeeper.models.Category;
import org.knop.budgetKeeper.models.Payment;
import org.knop.budgetKeeper.models.Subcategory;
import org.knop.budgetKeeper.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class PaymentServiceImpl implements PaymentService{
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public List<PaymentDto> getAllForUserWithLimit(Integer userId, Integer limit) {
        return paymentRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Payment::getDate, Comparator.nullsLast(Comparator.reverseOrder())))
                .limit(limit)
                .map(PaymentDto::new)
                .collect(Collectors.toList());
    }

    @Override
    public List<PaymentsShortStatsDto> getShortPaymentsStats(Integer userId) {
        Date timeStart = Date.valueOf(LocalDate.now().withDayOfMonth(1));
        Date timeEnd = Date.valueOf(LocalDate.now());
        List<Payment> payments = paymentRepository.findAllByUserIdAndDateBetween(userId, timeStart, timeEnd);
        BigDecimal consumptionSum = payments.stream()
                .filter(it -> !it.getIncomeLabel())
                .map(Payment::getValue)
                .reduce(BigDecimal::add)
                .get();
        Map<Category, BigDecimal> categorySum = new HashMap<>();
        payments.stream()
                .filter(it -> !it.getIncomeLabel())
                .forEach(
                        it -> {
                            BigDecimal consumption = categorySum.get(it.getCategory());
                            if (consumption == null) {
                                consumption = it.getValue();
                            } else {
                                consumption = consumption.add(it.getValue());
                            }
                            categorySum.put(it.getCategory(), consumption);
                        }
                );

        return categorySum.entrySet()
                .stream()
                .map(it -> new PaymentsShortStatsDto(
                    it.getKey().getName(),
                    it.getValue(),
                    it.getValue().divide(consumptionSum).multiply(BigDecimal.valueOf(100)).doubleValue()))
                .collect(Collectors.toList());
    }
}
