package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class PaymentsGraphDto {
    public PaymentsGraphDto() {
        this.income = new BigDecimal(0);
        this.expense = new BigDecimal(0);
    }
    private String date;
    private BigDecimal income;
    private BigDecimal expense;

    public void setNulls(){
        if(this.income.equals(BigDecimal.ZERO)) {
            this.income = null;
        }

        if(this.expense.equals(BigDecimal.ZERO)) {
            this.expense = null;
        }
    }
}
