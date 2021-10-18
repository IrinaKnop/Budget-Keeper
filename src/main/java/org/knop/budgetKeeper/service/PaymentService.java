package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.dto.PaymentsByCategoryStatsDto;
import org.knop.budgetKeeper.dto.PaymentsShortStatsDto;
import org.knop.budgetKeeper.models.Payment;

import java.sql.Date;
import java.util.List;

public interface PaymentService {
    List<PaymentDto> getAllForUser(Integer userId);
    List<PaymentDto> getAllForUserWithLimit(Integer userId, Integer limit);
    List<PaymentsShortStatsDto> getShortPaymentsStats(Integer userId);
    PaymentsByCategoryStatsDto getPaymentsStatsByPeriod(Integer userId, Date dateStart, Date dateEnd);
    List<PaymentsShortStatsDto> getStatsByCategory(Integer userId, Boolean incomeLabel, String categoryName, Date dateStart, Date dateEnd);
    PaymentDto addPayment(PaymentDto paymentDto);
}
