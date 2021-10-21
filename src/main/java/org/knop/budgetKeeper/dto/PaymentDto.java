package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knop.budgetKeeper.models.Payment;
import org.springframework.format.datetime.DateFormatter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    public PaymentDto(Payment payment) {
        DateFormatter df = new DateFormatter("dd/MM/YYYY");
        this.userId = payment.getUser().getId();
        this.id = payment.getId();
        this.incomeLabel = payment.getIncomeLabel();
        this.date = df.print(payment.getDate(), Locale.ITALIAN);
        this.categoryName = payment.getCategory().getName();
        this.subcategoryName = payment.getSubcategory() != null ? payment.getSubcategory().getName() : null;
        this.value = payment.getValue();
    }
    private Integer userId;
    private BigInteger id;
    private Boolean incomeLabel;
    private String date;
    private String categoryName;
    private String subcategoryName;
    private BigDecimal value;
}
