package com.sadman.financial.entity;

import com.sadman.financial.enums.ContractStatus;
import com.sadman.financial.enums.ContractType;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "contract")
public class Contract extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ContractType contractType;

    private String terms;

    @Enumerated(EnumType.STRING)
    private ContractStatus status;

    private LocalDate startDate;
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "income_id")
    private Income income;

    @ManyToOne
    @JoinColumn(name = "expense_id")
    private Expense expense;

    @ManyToOne
    @JoinColumn(name = "loan_id")
    private Loan loan;

    // Additional fields like repaymentAmount, remainingBalance, etc., for loan contracts
    private Double paidAmount;
    private Double remainingAmount;
}
