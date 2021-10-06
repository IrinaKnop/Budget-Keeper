package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PaymentDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getAllForUserWithLimit(Integer userId, Integer limit);
}
