package com.sadman.financial.service.interfaces;

import com.sadman.financial.dto.ExpenseRequest;
import com.sadman.financial.responses.ExpenseResponse;

import java.util.Map;

public interface IExpenseService {
    ExpenseResponse logExpense(ExpenseRequest expenseRequest);

    ExpenseResponse getExpenseById(Long expenseId);

    Map<String, Object> search(Integer page, Integer size, String sortBy, String search);

    ExpenseResponse updateExpense(Long expenseId, ExpenseRequest expenseRequest);

    void deleteExpenseById(Long expenseId);
}
