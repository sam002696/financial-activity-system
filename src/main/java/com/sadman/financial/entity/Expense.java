package com.sadman.financial.entity;

import com.sadman.financial.enums.ExpenseCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "expense")
public class Expense extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;

    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}