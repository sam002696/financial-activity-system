package com.sadman.financial.responses;

import com.sadman.financial.entity.Contract;
import com.sadman.financial.enums.ContractStatus;
import com.sadman.financial.enums.ContractType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ContractResponse {

    private Long id;
    private ContractType contractType;
    private String terms;
    private ContractStatus status;
    private LocalDate startDate;
    private LocalDate dueDate;
    private Double paidAmount;
    private Double remainingAmount;
    private UserResponse user;

    public static ContractResponse selectContract(Contract contract) {

        UserResponse userResponse = UserResponse.select(contract.getUser());

        return new ContractResponse(
                contract.getId(),
                contract.getContractType(),
                contract.getTerms(),
                contract.getStatus(),
                contract.getStartDate(),
                contract.getDueDate(),
                contract.getPaidAmount(),
                contract.getRemainingAmount(),
                userResponse
        );
    }
}
