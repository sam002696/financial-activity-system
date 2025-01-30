package com.sadman.financial.entity;

import com.sadman.financial.enums.LoanStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private Double paidAmount;
    private Double remainingAmount;
    private LocalDate dueDate;
    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}

