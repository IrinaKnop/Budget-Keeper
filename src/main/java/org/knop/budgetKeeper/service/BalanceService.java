package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.BalanceDto;
import org.knop.budgetKeeper.dto.BalanceInitialDto;
import org.knop.budgetKeeper.dto.DailyLimitDto;
import org.knop.budgetKeeper.models.Balance;

import java.math.BigDecimal;

public interface BalanceService {
    BalanceDto getCurrentBalance (Integer userId);
    Balance createInitialBalance (BalanceInitialDto balanceInitialDto);
    DailyLimitDto getDailyLimit (Integer userId);
}
