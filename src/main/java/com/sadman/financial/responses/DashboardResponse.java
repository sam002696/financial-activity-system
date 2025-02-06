package com.sadman.financial.responses;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardResponse {

    private double totalIncome;
    private double totalExpense;
    private double totalLoan;
    private double remainingLoan;
    private double balance;
}
