package com.sadman.financial.controller.Contract;

import com.sadman.financial.entity.Contract;
import com.sadman.financial.entity.Expense;
import com.sadman.financial.exceptions.CustomMessageException;
import com.sadman.financial.responses.ContractResponse;
import com.sadman.financial.service.impl.ContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sadman.financial.utils.ResponseBuilder.success;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Contract management")
@RequestMapping("/api/v1/financial-activities/contract")
public class ContractController {

    @Autowired
    private ContractService contractService;

    // Fetch contract for Income
    @GetMapping("/income/{incomeId}")
    @Operation(summary = "Get contract for income", description = "Fetch contract for the given income activity")
    @ApiResponse(responseCode = "200", description = "Contract successfully fetched")
    public ResponseEntity<JSONObject> getContractForIncome(@PathVariable Long incomeId) {
        Contract contract = contractService.getContractForIncome(incomeId);
        ContractResponse contractResponse = ContractResponse.selectContract(contract);
        return ok(success(contractResponse, "Successfully retrieved income contract").getJson());
    }

    // Fetch contract for Expense
    @GetMapping("/expense/{expenseId}")
    @Operation(summary = "Get contract for expense", description = "Fetch contract for the given expense activity")
    @ApiResponse(responseCode = "200", description = "Contract successfully fetched")
    public ResponseEntity<JSONObject> getContractForExpense(@PathVariable Long expenseId) {
        Contract contract = contractService.getContractForExpense(expenseId);

        ContractResponse contractResponse = ContractResponse.selectContract(contract);
        return ok(success(contractResponse, "Successfully retrieved expense contract").getJson());
    }

    // Fetch contract for Loan
    @GetMapping("/loan/{loanId}")
    @Operation(summary = "Get contract for loan", description = "Fetch contract for the given loan")
    @ApiResponse(responseCode = "200", description = "Contract successfully fetched")
    public ResponseEntity<JSONObject> getContractForLoan(@PathVariable Long loanId) {
        Contract contract = contractService.getContractForLoan(loanId);
        ContractResponse contractResponse = ContractResponse.selectContract(contract);
        return ok(success(contractResponse, "Successfully retrieved loan contract").getJson());
    }
}

