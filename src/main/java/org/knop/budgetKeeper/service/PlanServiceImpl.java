package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PaymentDto;
import org.knop.budgetKeeper.dto.PlanDto;
import org.knop.budgetKeeper.models.Payment;
import org.knop.budgetKeeper.models.Plan;
import org.knop.budgetKeeper.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlanServiceImpl implements PlanService{

    @Autowired
    private PlanRepository planRepository;

    @Override
    public List<PlanDto> getAllUserPlans(Integer userId) {
        return planRepository
                .findAllByUserId(userId)
                .stream()
                .sorted(Comparator.comparing(Plan::getDateStart, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(PlanDto::new)
                .collect(Collectors.toList());
    }
}
