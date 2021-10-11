package org.knop.budgetKeeper.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceInitialDto {
    private Integer userId;
    private BigDecimal initialBalance;
}
