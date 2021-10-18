package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentsByCategoryStatsDto {
    List<PaymentsShortStatsDto> incomeStatsListByCategory;
    List<PaymentsShortStatsDto> expenseStatsListByCategory;
}
