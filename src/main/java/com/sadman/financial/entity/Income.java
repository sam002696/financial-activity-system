package com.sadman.financial.entity;

import com.sadman.financial.enums.IncomeCategory;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
@Entity
@Table(name = "income")
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;
    private String source;
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    private IncomeCategory category;

    @OneToMany(mappedBy = "income", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Contract> contracts;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
