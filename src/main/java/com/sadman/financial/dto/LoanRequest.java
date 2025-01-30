package com.sadman.financial.dto;

import com.sadman.financial.enums.LoanStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LoanRequest {
    private Double amount;
    private LocalDate dueDate;
    private LoanStatus status;
}
