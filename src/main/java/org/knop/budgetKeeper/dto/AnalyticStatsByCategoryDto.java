package org.knop.budgetKeeper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnalyticStatsByCategoryDto {
    private Integer userId;
    private Boolean incomeLabel;
    private String categoryName;
    private Date dateStart;
    private Date dateEnd;
}
