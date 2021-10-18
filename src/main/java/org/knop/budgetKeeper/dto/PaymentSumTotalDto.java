package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.knop.budgetKeeper.models.Payment;
import org.springframework.format.datetime.DateFormatter;

import java.math.BigDecimal;
import java.util.Locale;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentSumTotalDto {
    public PaymentSumTotalDto(Payment payment) {
        DateFormatter df = new DateFormatter("dd-MM-YYYY");
        this.value = payment.getValue();
        this.date = df.print(payment.getDate(), Locale.ITALIAN);
    }

    private BigDecimal value;
    private String date;
}
