package com.sadman.financial.entity;

import com.sadman.financial.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;


import java.util.List;

@Data
@Entity
@Table(name = "users")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;
    private String password;

    private Double balance = 0.0;

    @Enumerated(EnumType.STRING)
    private RoleName role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Income> incomes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Expense> expenses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Loan> loans;

}

