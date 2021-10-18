package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.CategoriesDto;
import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.dto.PaymentsByCategoryStatsDto;
import org.knop.budgetKeeper.dto.PaymentsShortStatsDto;
import org.knop.budgetKeeper.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
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

    @GetMapping("/getPaymentsStatsByPeriod")
    public ResponseEntity<PaymentsByCategoryStatsDto> getPaymentsStatsByPeriod(@RequestParam Integer userId,
                                                                               @RequestParam Date dateStart,
                                                                               @RequestParam Date dateEnd) {
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new PaymentsByCategoryStatsDto(Collections.emptyList(), Collections.emptyList()));
        }
        else {
            return ResponseEntity.ok(paymentService.getPaymentsStatsByPeriod(userId, dateStart, dateEnd));
        }
    }

    @GetMapping("/getSubcategoryStatsByPeriod")
    public ResponseEntity<List<PaymentsShortStatsDto>> getSubcategoryStatsByPeriod(@RequestParam Integer userId,
                                                                                   @RequestParam Boolean incomeLabel,
                                                                                   @RequestParam String categoryName,
                                                                                   @RequestParam Date dateStart,
                                                                                   @RequestParam Date dateEnd) {
        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getStatsByCategory(userId, incomeLabel, categoryName, dateStart, dateEnd));
        }
    }

    @PostMapping("/addPayment")
    public ResponseEntity<?> addPayment (@RequestBody PaymentDto paymentDto) {
        if (paymentDto.getUserId() == null) {
            return ResponseEntity.status(401).body(null);
        }
        else {
            return ResponseEntity.ok(paymentService.addPayment(paymentDto));
        }
    }
}
