package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.models.Payment;
import org.knop.budgetKeeper.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Comparator;
import java.util.List;
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
}
