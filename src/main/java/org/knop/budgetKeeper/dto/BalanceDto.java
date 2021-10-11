package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.knop.budgetKeeper.models.Balance;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class BalanceDto {
    public BalanceDto(Balance balance) {
        this.finalBalance = balance.getFinalBalance();
    }

    private BigDecimal finalBalance;
}
