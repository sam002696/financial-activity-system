package com.sadman.financial.service.impl;

import com.sadman.financial.dto.ExpenseRequest;
import com.sadman.financial.entity.Expense;
import com.sadman.financial.entity.User;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.ExpenseRepository;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.responses.ExpenseResponse;
import com.sadman.financial.security.UserPrincipal;
import com.sadman.financial.service.interfaces.IExpenseService;
import com.sadman.financial.utils.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExpenseService implements IExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private NotificationService notificationService;

    @Override
    public ExpenseResponse logExpense(ExpenseRequest expenseRequest) {
        // Retrieving the user from the SecurityContext (JWT)
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        // Retrieving the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));

        // Validating that the expense amount is greater than zero
        if (expenseRequest.getAmount() <= 0) {
            throw new CustomMessageException("Expense amount must be greater than zero.");
        }

        // Validating that the user has enough balance to log the expense
        if (user.getBalance() < expenseRequest.getAmount()) {
            throw new CustomMessageException("Insufficient balance to log the expense.");
        }

        // Creating the expense
        Expense expense = new Expense();
        expense.setAmount(expenseRequest.getAmount());
        expense.setCategory(expenseRequest.getCategory());
        expense.setDate(expenseRequest.getDate());
        expense.setUser(user);

        // Saving the expense
        expenseRepository.save(expense);

        // Updating the user's balance by deducting the expense amount
        user.setBalance(user.getBalance() - expenseRequest.getAmount());

        // Saving the updated user balance
        userRepository.save(user);

        // Creating a contract for the expense
        contractService.createContractForExpense(expense, user);

        notificationService.sendNotification(userId, "Expense",
                 " - " + expense.getAmount());

        // Return the response
        return ExpenseResponse.select(expense);
    }


    @Override
    public ExpenseResponse getExpenseById(Long expenseId) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomMessageException("Expense not found"));
        return ExpenseResponse.select(expense);
    }


    @Override
    public Map<String, Object> search(Integer page, Integer size, String sortBy, String search) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        ServiceHelper<Expense> serviceHelper = new ServiceHelper<>(Expense.class);
        return serviceHelper.getList(
                expenseRepository.search(search, userId, serviceHelper.getPageable(sortBy, page, size)),
                page, size);
    }


    @Override
    public ExpenseResponse updateExpense(Long expenseId, ExpenseRequest expenseRequest) {
        // Retrieving the existing expense
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomMessageException("Expense not found"));

        // Retrieving the user associated with the expense
        User user = existingExpense.getUser();

        // Validating that the expense amount is greater than zero
        if (expenseRequest.getAmount() <= 0) {
            throw new CustomMessageException("Expense amount must be greater than zero.");
        }

        // Calculating the difference between the old and new expense amounts
        Double amountDifference = expenseRequest.getAmount() - existingExpense.getAmount();

        // Updating the expense details
        existingExpense.setAmount(expenseRequest.getAmount());
        existingExpense.setCategory(expenseRequest.getCategory());
        existingExpense.setDate(expenseRequest.getDate());

        // Saving the updated expense
        expenseRepository.save(existingExpense);

        // Updating the user's balance
        if (amountDifference < 0) {
            // If the expense has been reduced, we increase the balance
            user.setBalance(user.getBalance() + Math.abs(amountDifference));
        } else {
            // If the expense has been increased, we decrease the balance
            user.setBalance(user.getBalance() - amountDifference);
        }

        // Saving the updated user balance
        userRepository.save(user);

        // Returning the updated expense
        return ExpenseResponse.select(existingExpense);
    }


    @Override
    public void deleteExpenseById(Long expenseId) {
        // Retrieving the existing expense by its ID
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new CustomMessageException("Expense not found"));


        // Deleting the expense record
        expenseRepository.delete(expense);

    }
}
