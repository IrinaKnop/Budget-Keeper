package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PaymentsController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/getLastPayments")
    public ResponseEntity<List<PaymentDto>> getLastPayments(@RequestParam Integer userId, @RequestParam Integer limit) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(paymentService.getAllForUserWithLimit(userId, limit));
        }
    }
}
