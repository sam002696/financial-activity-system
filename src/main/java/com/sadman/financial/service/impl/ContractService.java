package com.sadman.financial.service.impl;

import com.sadman.financial.entity.*;
import com.sadman.financial.enums.ContractStatus;
import com.sadman.financial.enums.ContractType;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.ContractRepository;
import com.sadman.financial.service.interfaces.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ContractService implements IContractService {

    @Autowired
    private ContractRepository contractRepository;

    @Override
    public void createContractForIncome(Income income, User user) {
        // Create the contract for the income
        Contract contract = new Contract();
        contract.setContractType(ContractType.INCOME); // Type of contract: INCOME
        contract.setStatus(ContractStatus.ACTIVE);     // Initially set to ACTIVE
        contract.setTerms("Income source: " + income.getSource());
        contract.setStartDate(LocalDate.now());       // Set start date to current date
        contract.setIncome(income);                    // Associate income with the contract
        contract.setUser(user);                        // Associate the user with the contract
        contractRepository.save(contract);             // Save the contract to the repository
    }


    @Override
    public void createContractForExpense(Expense expense, User user) {
        // Create the contract for the expense
        Contract contract = new Contract();
        contract.setExpense(expense);
        contract.setUser(user);
        contract.setContractType(ContractType.EXPENSE);
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setTerms("Expense logged");
        contract.setStartDate(LocalDate.now());
        contractRepository.save(contract);             // Save the contract to the repository
    }


    // Create a contract for the loan
    @Override
    public Contract createContractForLoan(Loan loan, User user) {
        Contract contract = new Contract();
        contract.setLoan(loan);
        contract.setUser(user);
        contract.setContractType(ContractType.LOAN);
        contract.setStatus(ContractStatus.ACTIVE);
        contract.setTerms("Loan amount: " + loan.getAmount() + ", Repayment due date: " + loan.getDueDate());
        contract.setStartDate(LocalDate.now());
        contract.setDueDate(loan.getDueDate());

        // Save the contract
        contractRepository.save(contract);
        return contract;
    }

    // Update the loan repayment status
    @Override
    public void updateLoanRepaymentStatus(Loan loan) {
        Contract contract = contractRepository.findByLoan(loan)
                .orElseThrow(() -> new CustomMessageException("Contract not found for loan"));

        if (loan.getRemainingAmount() <= 0) {
            contract.setStatus(ContractStatus.PAID);  // Mark as PAID
        } else if (contract.getDueDate().isBefore(LocalDate.now())) {
            contract.setStatus(ContractStatus.OVERDUE);  // Mark as OVERDUE if due date has passed
        }

        contractRepository.save(contract);
    }


    // Fetch contract for Income
    @Override
    public Contract getContractForIncome(Long incomeId) {

        Contract existingContract = contractRepository.findById(incomeId)
                .orElseThrow(() -> new CustomMessageException("income not found in the contract"));
        return existingContract;
    }

    // Fetch contract for Expense
    @Override
    public Contract getContractForExpense(Long expenseId) {

        Contract existingContract = contractRepository.findById(expenseId)
                .orElseThrow(() -> new CustomMessageException("expense not found in the contract"));
        return existingContract;


    }

    // Fetch contract for Loan
    @Override
    public Contract getContractForLoan(Long loanId) {

        Contract existingContract = contractRepository.findById(loanId)
                .orElseThrow(() -> new CustomMessageException("loan not found in the contract"));
        return existingContract;
    }

}
