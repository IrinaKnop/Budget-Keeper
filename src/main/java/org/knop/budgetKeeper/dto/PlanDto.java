package org.knop.budgetKeeper.dto;

import lombok.Data;
import org.knop.budgetKeeper.models.Plan;

import java.sql.Date;

@Data
public class PlanDto {

    public PlanDto(Plan plan) {
        this.isOpen = plan.getIsOpen();
        this.accumulateFlag = plan.getAccumulateFlag();
        this.name = plan.getName();
        this.value = plan.getValue();
        this.dateStart = plan.getDateStart();
        this.dateEnding = plan.getDateEnding();
        this.progress =  (double)(Math.round(plan.getProgress() * 100) / 100);
    }

    private Boolean isOpen;
    private Boolean accumulateFlag;
    private String name;
    private Integer value;
    private Date dateStart;
    private Date dateEnding;
    private Double progress;
}
