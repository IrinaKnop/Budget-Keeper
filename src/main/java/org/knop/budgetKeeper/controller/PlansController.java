package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.PlanDto;
import org.knop.budgetKeeper.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlansController {

    @Autowired
    private PlanService planService;

    @GetMapping("/getAllPlans")
    public ResponseEntity<List<PlanDto>> getAllPlans(@RequestParam Integer userId) {
        if (userId == null) {
            return ResponseEntity.status(401).body(new ArrayList<>());
        }
        else {
            return ResponseEntity.ok(planService.getAllUserPlans(userId));
        }
    }
}