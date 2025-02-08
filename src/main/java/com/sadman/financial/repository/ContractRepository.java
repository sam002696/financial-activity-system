package com.sadman.financial.repository;

import com.sadman.financial.entity.Contract;
import com.sadman.financial.entity.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractRepository extends JpaRepository<Contract, Long> {
    // Custom method to find contract by loan
    Optional<Contract> findByLoan(Loan loan);

    // Finding contract by income ID
    Optional<Contract> findByIncomeId(Long incomeId);

    // Finding contract by expense ID
    Optional<Contract> findByExpenseId(Long expenseId);

    // Finding contract by loan ID
    Optional<Contract> findByLoanId(Long loanId);


}

