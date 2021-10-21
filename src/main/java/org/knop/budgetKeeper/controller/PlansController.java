package org.knop.budgetKeeper.controller;

import org.knop.budgetKeeper.dto.PlanDto;
import org.knop.budgetKeeper.service.PlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PlansController {

    @Autowired
    private PlanService planService;

    @PostMapping("/addPlan")
    public ResponseEntity<PlanDto> addPlan(@RequestBody PlanDto planDto) {
        if(planDto.getUserId() == null) {
            return ResponseEntity.status(401).body(null);
        }
        else {
            return ResponseEntity.ok(planService.addPlan(planDto));
        }
    }

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
