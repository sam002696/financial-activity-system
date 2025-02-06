package com.sadman.financial.service.interfaces;

import com.sadman.financial.entity.*;

public interface IContractService {
    void createContractForIncome(Income income, User user);

    void createContractForExpense(Expense expense, User user);

    // Create a contract for the loan
    Contract createContractForLoan(Loan loan, User user);

    // Update the loan repayment status
    void updateLoanRepaymentStatus(Loan loan);

    // Fetch contract for Income
    Contract getContractForIncome(Long incomeId);

    // Fetch contract for Expense
    Contract getContractForExpense(Long expenseId);

    // Fetch contract for Loan
    Contract getContractForLoan(Long loanId);
}
