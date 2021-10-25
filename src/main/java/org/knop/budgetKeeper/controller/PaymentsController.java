package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.*;
import org.knop.budgetKeeper.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PaymentsController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/getAllPayments")
    public ResponseEntity<List<PaymentDto>> getAllPaymentsWithIncomeLabel(@RequestParam Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getAllForUser(userId));
        }
    }

    @GetMapping("/getLastPayments")
    public ResponseEntity<List<PaymentDto>> getLastPayments(@RequestParam Integer userId, @RequestParam Integer limit) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getAllForUserWithLimit(userId, limit));
        }
    }

    @GetMapping("/getShortPaymentsStats")
    public ResponseEntity<List<PaymentsShortStatsDto>> getShortPaymentsStats(@RequestParam Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getShortPaymentsStats(userId));
        }
    }

    @PostMapping("/getPaymentsStatsByPeriod")
    public ResponseEntity<List<PaymentsShortStatsDto>> getPaymentsStatsByPeriod(@RequestBody AnalyticStatsDto analyticStatsDto) {
        if (analyticStatsDto.getUserId() == null) {
            return ResponseEntity.status(401)
                    .body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getPaymentsStatsByPeriod(analyticStatsDto));
        }
    }

    @PostMapping("/getSubcategoryStatsByPeriod")
    public ResponseEntity<List<PaymentsShortStatsDto>> getSubcategoryStatsByPeriod(@RequestBody AnalyticStatsByCategoryDto analyticStatsByCategoryDto) {
        if (analyticStatsByCategoryDto.getUserId() == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getStatsByCategory(analyticStatsByCategoryDto));
        }
    }

    @PostMapping("/addPayment")
    public ResponseEntity<PaymentDto> addPayment (@RequestBody PaymentDto paymentDto) {
        if (paymentDto.getUserId() == null) {
            return ResponseEntity.status(401).body(null);
        }
        else {
            return ResponseEntity.ok(paymentService.addPayment(paymentDto));
        }
    }

    @PostMapping("/deletePayment")
    public ResponseEntity<Boolean> deletePayment (@RequestBody PaymentDto paymentDto) {
        if (paymentDto.getUserId() == null) {
            return ResponseEntity.status(401).body(null);
        } else {
            return ResponseEntity.ok(paymentService.deletePayment(paymentDto));
        }
    }

        @GetMapping("/getGraphStats")
    public ResponseEntity<List<PaymentsGraphDto>> getGraphStats(@RequestParam Integer userId,
                                                                @RequestParam Date dateStart,
                                                                @RequestParam Date dateEnd) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getGraphStats(userId, dateStart, dateEnd));
        }
    }
}
