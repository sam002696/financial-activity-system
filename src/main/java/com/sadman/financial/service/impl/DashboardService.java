package com.sadman.financial.service.impl;


import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.IncomeRepository;
import com.sadman.financial.repository.ExpenseRepository;
import com.sadman.financial.repository.LoanRepository;
import com.sadman.financial.entity.User;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.responses.DashboardResponse;
import com.sadman.financial.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private LoanRepository loanRepository;

    public DashboardResponse getDashboardData() {
        // Fetching user by a hardcoded ID for simplicity (should be dynamic based on logged-in user)
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));

        // Fetching totals for each financial activity
        Double totalIncome = incomeRepository.findTotalIncomeByUser(user.getId());
        Double totalExpense = expenseRepository.findTotalExpenseByUser(user.getId());
        Double totalLoan = loanRepository.findTotalLoanByUser(user.getId());
        Double remainingLoan = loanRepository.findRemainingLoanByUser(user.getId());

        // Handling null values, defaulting to 0.0 if the values are null
        totalIncome = (totalIncome != null) ? totalIncome : 0.0;
        totalExpense = (totalExpense != null) ? totalExpense : 0.0;
        totalLoan = (totalLoan != null) ? totalLoan : 0.0;
        remainingLoan = (remainingLoan != null) ? remainingLoan : 0.0;

        // User balance
        double balance = user.getBalance();

        // Returning data wrapped in a response DTO
        return new DashboardResponse(totalIncome, totalExpense, totalLoan, remainingLoan, balance);
    }
}
