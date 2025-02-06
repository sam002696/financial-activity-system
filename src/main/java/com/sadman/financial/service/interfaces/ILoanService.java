package com.sadman.financial.service.interfaces;

import com.sadman.financial.dto.LoanRequest;
import com.sadman.financial.responses.LoanResponse;

import java.util.Map;

public interface ILoanService {
    // Log a new loan
    LoanResponse logLoan(LoanRequest loanRequest);

    // Get loan by ID
    LoanResponse getLoanById(Long loanId);

    Map<String, Object> search(Integer page, Integer size, String sortBy, String search);

    LoanResponse updateLoan(Long loanId, LoanRequest loanRequest);

    // Repay loan
    LoanResponse repayLoan(Long loanId, Double amount);

    // Delete loan
    void deleteLoan(Long loanId);
}
