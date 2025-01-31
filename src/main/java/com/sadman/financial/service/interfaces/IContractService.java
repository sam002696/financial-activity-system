package com.sadman.financial.service.interfaces;

import com.sadman.financial.entity.*;

public interface IContractService {
    void createContractForIncome(Income income, User user);

    void createContractForExpense(Expense expense, User user);

    // Create a contract for the loan
    Contract createContractForLoan(Loan loan, User user);

    // Update the loan repayment status
    void updateLoanRepaymentStatus(Loan loan);
}
