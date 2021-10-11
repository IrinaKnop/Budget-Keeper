package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.dto.PaymentsShortStatsDto;

import java.util.List;

public interface PaymentService {
    List<PaymentDto> getAllForUserWithLimit(Integer userId, Integer limit);
    List<PaymentsShortStatsDto> getShortPaymentsStats(Integer userId);
}
