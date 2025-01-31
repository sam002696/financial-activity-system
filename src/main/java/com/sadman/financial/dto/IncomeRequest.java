package com.sadman.financial.dto;

import com.sadman.financial.enums.IncomeCategory;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDate;

@Data
public class IncomeRequest {

    @NotBlank(message = "Income source is required")
    private String source;

    @Min(value = 0, message = "Income amount cannot be negative")
    private Double amount;

    @Enumerated(EnumType.STRING)
    private IncomeCategory category; // Enum for income category

    private LocalDate date; // Date of income
}
