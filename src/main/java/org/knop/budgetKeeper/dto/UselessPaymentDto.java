package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UselessPaymentDto {
    private String category;
    private String subcategory;
    private BigDecimal value;

    @Override
    public int hashCode() {
        return this.category.hashCode();
    }

    @Override
    public boolean equals (Object o)  {
        if(!(o instanceof UselessPaymentDto)) {
            return false;
        }

        UselessPaymentDto other = (UselessPaymentDto) o;
        if(this.subcategory != null) {
            return other.getSubcategory() != null && this.subcategory.equals(other.getSubcategory());
        }
        return this.category.equals(other.getCategory());
    }
}
