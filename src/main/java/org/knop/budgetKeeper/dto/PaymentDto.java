package org.knop.budgetKeeper.dto;

import lombok.Data;
import org.knop.budgetKeeper.models.Payment;

import java.math.BigDecimal;
import java.sql.Date;

@Data
public class PaymentDto {
    public PaymentDto(Payment payment) {
        this.incomeLabel = payment.getIncomeLabel();
        this.date = payment.getDate();
        this.categoryName = payment.getCategory().getName();
        this.subcategoryName = payment.getSubcategory().getName();
        this.value = payment.getValue();
    }
    private Boolean incomeLabel;
    private Date date;
    private String categoryName;
    private String subcategoryName;
    private BigDecimal value;
}
