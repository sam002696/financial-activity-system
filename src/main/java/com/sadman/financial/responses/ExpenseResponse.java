package com.sadman.financial.responses;

import com.sadman.financial.entity.Expense;
import com.sadman.financial.enums.ExpenseCategory;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseResponse {

    private Long id;
    private Double amount;
    private ExpenseCategory category;
    private LocalDate date;
    private Long userId;  // assuming we need to return the userId too

    // Method to map an Expense entity to an ExpenseResponse
    public static ExpenseResponse select(Expense expense) {
        ExpenseResponse response = new ExpenseResponse();
        response.setId(expense.getId());
        response.setAmount(expense.getAmount());
        response.setCategory(expense.getCategory());
        response.setDate(expense.getDate());
        response.setUserId(expense.getUser().getId()); // Return userId associated with the expense
        return response;
    }
}

