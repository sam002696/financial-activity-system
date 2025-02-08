package com.sadman.financial.service.interfaces;

import com.sadman.financial.entity.*;

public interface IContractService {
    void createContractForIncome(Income income, User user);

    void createContractForExpense(Expense expense, User user);

    // Creating a contract for the loan
    Contract createContractForLoan(Loan loan, User user);

    // Updating the loan repayment status
    void updateLoanRepaymentStatus(Loan loan);

    // Fetching contract for Income
    Contract getContractForIncome(Long incomeId);

    // Fetching contract for Expense
    Contract getContractForExpense(Long expenseId);

    // Fetching contract for Loan
    Contract getContractForLoan(Long loanId);
}
