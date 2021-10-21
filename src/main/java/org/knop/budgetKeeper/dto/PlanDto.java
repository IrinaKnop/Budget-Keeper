package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knop.budgetKeeper.models.Plan;

import java.sql.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanDto {

    public PlanDto(Plan plan) {
        this.id = plan.getId();
        this.userId = plan.getUser().getId();
        this.name = plan.getName();
        this.value = plan.getValue();
        this.dateStart = plan.getDateStart();
        this.dateEnding = plan.getDateEnding();
        this.progress =  (double)(Math.round(plan.getProgress() * 100) / 100);
        this.isAccumulate = plan.getIsAccumulate();
        this.isOpen = plan.getIsOpen();
    }

    private Integer id;
    private Integer userId;
    private String name;
    private Integer value;
    private Date dateStart;
    private Date dateEnding;
    private Double progress;
    private Boolean isAccumulate;
    private Boolean isOpen;
}
