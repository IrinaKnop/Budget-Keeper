package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.*;
import org.knop.budgetKeeper.models.Payment;

import java.sql.Date;
import java.util.List;

public interface PaymentService {
    List<PaymentDto> getAllForUser(Integer userId);
    List<PaymentDto> getAllForUserWithLimit(Integer userId, Integer limit);
    List<PaymentsShortStatsDto> getShortPaymentsStats(Integer userId);
    List<PaymentsShortStatsDto> getPaymentsStatsByPeriod(AnalyticStatsDto analyticStatsDto);
    List<PaymentsShortStatsDto> getStatsByCategory(AnalyticStatsByCategoryDto analyticStatsByCategoryDto);
    PaymentDto addPayment(PaymentDto paymentDto);
    List<PaymentsGraphDto> getGraphStats(Integer userId, Date dateStart, Date dateEnd);
    List<UselessPaymentDto> getUselessPayments(Integer userId, Date dateStart, Date dateEnd);
    Boolean deletePayment(PaymentDto paymentDto);
}
