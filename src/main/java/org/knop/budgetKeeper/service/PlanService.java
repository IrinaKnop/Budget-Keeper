package org.knop.budgetKeeper.service;

import org.knop.budgetKeeper.dto.PlanDto;

import java.util.List;

public interface PlanService {
    List<PlanDto> getAllUserPlans(Integer userId);
}
