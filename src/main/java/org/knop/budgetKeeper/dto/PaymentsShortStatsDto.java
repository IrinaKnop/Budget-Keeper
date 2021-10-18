package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentsShortStatsDto {
    private String category;
    private BigDecimal value;
}
