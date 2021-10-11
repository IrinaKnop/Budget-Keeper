package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.BalanceDto;
import org.knop.budgetKeeper.dto.DailyLimitDto;
import org.knop.budgetKeeper.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @GetMapping("/getCurrentBalance")
    public ResponseEntity<BalanceDto> getCurrentBalance(@RequestParam Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new BalanceDto(BigDecimal.ZERO));
        }
        else {
            return ResponseEntity.ok(balanceService.getCurrentBalance(userId));
        }
    }

    @GetMapping("/getDailyLimit")
    public ResponseEntity<DailyLimitDto> getDailyLimit(@RequestParam Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new DailyLimitDto(BigDecimal.ZERO));
        }
        else {
            return ResponseEntity.ok(balanceService.getDailyLimit(userId));
        }
    }
}
