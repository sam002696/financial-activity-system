package com.sadman.financial.service.impl;

import com.sadman.financial.dto.IncomeRequest;
import com.sadman.financial.entity.Income;
import com.sadman.financial.entity.User;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.IncomeRepository;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.security.UserPrincipal;
import com.sadman.financial.service.interfaces.IIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class IncomeService implements IIncomeService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ContractService contractService;

    @Override
    public Income logIncome(IncomeRequest incomeRequest) {
        // Retrieve the user from the SecurityContext (JWT)
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        // Retrieve the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));

        // Validate the income amount (should be positive)
        if (incomeRequest.getAmount() <= 0) {
            throw new CustomMessageException("Income amount must be greater than zero.");
        }

        // Create the income entity
        Income income = new Income();
        income.setAmount(incomeRequest.getAmount());
        income.setSource(incomeRequest.getSource());
        income.setCategory(incomeRequest.getCategory());
        income.setDate(incomeRequest.getDate());
        income.setUser(user);

        // Save the income entity
        incomeRepository.save(income);

        // Update the user's balance (add the income amount)
        user.setBalance(user.getBalance() + incomeRequest.getAmount());
        userRepository.save(user);

        // Create a contract for the income (contract enforcement logic)
        contractService.createContractForIncome(income, user);

        // Return the created income
        return income;
    }
}
