package com.sadman.financial.dto;

import com.sadman.financial.enums.ExpenseCategory;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ExpenseRequest {
    private Double amount;
    private ExpenseCategory category; // Enum for expense category
    private LocalDate date;
}
