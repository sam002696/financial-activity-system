package com.sadman.financial.service.impl;

import com.sadman.financial.entity.Contract;
import com.sadman.financial.entity.Expense;
import com.sadman.financial.entity.Income;
import com.sadman.financial.entity.User;
import com.sadman.financial.enums.ContractStatus;
import com.sadman.financial.enums.ContractType;
import com.sadman.financial.repository.ContractRepository;
import com.sadman.financial.service.interfaces.IContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Service
public class ContractService implements IContractService {

    @Autowired
    private ContractRepository contractRepository;

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

}
