package com.sadman.financial.repository;

import com.sadman.financial.entity.Contract;
import com.sadman.financial.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    // Custom method to find contract by loan
    Optional<Contract> findByLoan(Loan loan);

    // Find contract by income ID
    Contract findByIncomeId(Long incomeId);

    // Find contract by expense ID
    Contract findByExpenseId(Long expenseId);

    // Find contract by loan ID
    Contract findByLoanId(Long loanId);


}

