package com.sadman.financial.service.impl;
import com.sadman.financial.dto.LoanRequest;
import com.sadman.financial.entity.Loan;
import com.sadman.financial.entity.User;
import com.sadman.financial.enums.LoanStatus;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.repository.LoanRepository;
import com.sadman.financial.repository.UserRepository;
import com.sadman.financial.responses.LoanResponse;
import com.sadman.financial.security.UserPrincipal;
import com.sadman.financial.service.interfaces.ILoanService;
import com.sadman.financial.utils.ServiceHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class LoanService implements ILoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContractService contractService;

    @Autowired
    private NotificationService notificationService;

    // Log a new loan
    @Override
    public LoanResponse logLoan(LoanRequest loanRequest) {
        // Retrieve the user from the SecurityContext (JWT)
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        // Retrieve the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));

        // Create a new loan
        Loan loan = new Loan();
        loan.setAmount(loanRequest.getAmount());
        loan.setPaidAmount(0.0);
        loan.setRemainingAmount(loanRequest.getAmount());
        loan.setDueDate(loanRequest.getDueDate());
        loan.setStatus(LoanStatus.ACTIVE);
        loan.setUser(user);

        // Save the loan in the database
        loanRepository.save(loan);

        // Create a contract for the loan
        contractService.createContractForLoan(loan, user);

        notificationService.sendNotification(userId, "Loan",
                 " - " + loan.getAmount());
        // Return the loan response
        return LoanResponse.select(loan);
    }

    // Get loan by ID
    @Override
    public LoanResponse getLoanById(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomMessageException("Loan not found"));

        return LoanResponse.select(loan);
    }


    @Override
    public Map<String, Object> search(Integer page, Integer size, String sortBy, String search) {

        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        ServiceHelper<Loan> serviceHelper = new ServiceHelper<>(Loan.class);
        return serviceHelper.getList(
                loanRepository.search(search,userId, serviceHelper.getPageable(sortBy, page, size)),
                page, size);
    }



    @Override
    public LoanResponse updateLoan(Long loanId, LoanRequest loanRequest) {
        // Retrieving the existing expense
        Loan existingLoan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomMessageException("Loan not found"));

        // Retrieving the user associated with the expense
        User user = existingLoan.getUser();

        // Validating that the expense amount is greater than zero
        if (loanRequest.getAmount() <= 0) {
            throw new CustomMessageException("Expense amount must be greater than zero.");
        }

        // Updating the expense details
        existingLoan.setAmount(loanRequest.getAmount());
        existingLoan.setRemainingAmount(loanRequest.getAmount());
        existingLoan.setDueDate(loanRequest.getDueDate());

        // Saving the updated expense
        loanRepository.save(existingLoan);


        // Returning the updated expense
        return LoanResponse.select(existingLoan);
    }

    // Repay loan
    @Override
    public LoanResponse repayLoan(Long loanId, Double amount) {
        // Retrieving the user from the SecurityContext (JWT)
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userPrincipal.getId();

        // Retrieving the User entity from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomMessageException("User not found with id: " + userId));

        // Retrieving the Loan entity from the database
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomMessageException("Loan not found"));

        // Checking if the amount being repaid is valid
        if (amount <= 0) {
            throw new CustomMessageException("Repayment amount must be greater than zero.");
        }

        // Checking if the loan has already been fully paid
        if (loan.getRemainingAmount() <= 0) {
            throw new CustomMessageException("Loan is already fully paid.");
        }

        // Checking if the user has enough balance to make the repayment
        if (user.getBalance() < amount) {
            throw new CustomMessageException("Insufficient balance to make the repayment.");
        }

        // Updating the paid amount and remaining amount for the loan
        loan.setPaidAmount(loan.getPaidAmount() + amount);
        loan.setRemainingAmount(loan.getRemainingAmount() - amount);

        // If the loan is fully paid, updating the status
        if (loan.getRemainingAmount() <= 0) {
            loan.setStatus(LoanStatus.PAID);
        }

        // Saving the updated loan
        loanRepository.save(loan);

        // Updating the user's balance by deducting the repayment amount
        user.setBalance(user.getBalance() - amount);
        userRepository.save(user);

        // Updating the contract status based on the repayment
        contractService.updateLoanRepaymentStatus(loan);

        // Returning the loan response
        return LoanResponse.select(loan);
    }


    // Delete loan
    @Override
    public void deleteLoan(Long loanId) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new CustomMessageException("Loan not found"));

        // Deleting the loan
        loanRepository.delete(loan);
    }
}
