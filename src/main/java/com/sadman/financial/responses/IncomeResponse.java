package com.sadman.financial.responses;


import com.sadman.financial.entity.Income;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IncomeResponse {

    private Long id;
    private Double amount;
    private String source;
    private String category;
    private String date;

    // Mapping method from entity to response DTO
    public static IncomeResponse select(Income income) {
        IncomeResponse response = new IncomeResponse();
        response.setId(income.getId());
        response.setAmount(income.getAmount());
        response.setSource(income.getSource());
        response.setCategory(income.getCategory().toString());
        response.setDate(income.getDate().toString());
        return response;
    }
}
