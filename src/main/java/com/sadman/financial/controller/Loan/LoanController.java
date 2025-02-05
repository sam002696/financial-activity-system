package com.sadman.financial.controller.Loan;
import com.sadman.financial.dto.LoanRequest;
import com.sadman.financial.entity.Loan;
import com.sadman.financial.helpers.CommonDataHelper;
import com.sadman.financial.responses.LoanResponse;
import com.sadman.financial.service.impl.LoanService;
import com.sadman.financial.utils.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.sadman.financial.utils.ResponseBuilder.paginatedSuccess;
import static com.sadman.financial.utils.ResponseBuilder.success;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Loan activity")
@RequestMapping("/api/v1/financial-activities/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private CommonDataHelper commonDataHelper;

    // Logging a new loan
    @PostMapping("/create")
    @Operation(summary = "Log a new loan",
            description = "Create a loan with terms including repayment schedule and due date.",
            responses = {
            @ApiResponse(description = "Successfully logged loan", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoanRequest.class)))
    })
    public ResponseEntity<?> logLoan(@RequestBody LoanRequest loanRequest) {
        LoanResponse loanResponse = loanService.logLoan(loanRequest);
        return ok(success(loanResponse, "Successfully logged loan").getJson());
    }

    // Getting a single loan by ID
    @GetMapping("/{loanId}")
    @Operation(summary = "Get a single loan", description = "Retrieve a loan by its ID", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanResponse.class))),
            @ApiResponse(responseCode = "404", description = "Loan not found")
    })
    public ResponseEntity<JSONObject> getLoanById(@PathVariable Long loanId) {
        LoanResponse loanResponse = loanService.getLoanById(loanId);
        return ok(success(loanResponse, "Successfully retrieved loan").getJson());
    }

    // Getting list of loans
    @GetMapping("/list")
    @Operation(summary = "Show lists of all loans", description = "Show lists of all loans")
    @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LoanResponse.class))
    })
    public ResponseEntity<JSONObject> listLoans(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "sortBy", defaultValue = "") String sortBy,
                                                   @RequestParam(value = "search", defaultValue = "") String search) {

        PaginatedResponse response = new PaginatedResponse();
        Map<String, Object> map = loanService.search(page, size, sortBy, search);
        List<Loan> loanList = (List<Loan>) map.get("lists");
        List<LoanResponse> responses = loanList.stream().map(LoanResponse::select).toList();
        commonDataHelper.getCommonData(page, size, map, response, responses);
        return ok(paginatedSuccess(response).getJson());
    }

    // Repaying a loan by its id
    @PutMapping("/repay/{loanId}")
    @Operation(summary = "Repay loan", description = "Repay a loan and update the status and balance", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = LoanResponse.class)))
    })
    public ResponseEntity<JSONObject> repayLoan(@PathVariable Long loanId, @RequestParam Double amount) {
        LoanResponse updatedLoan = loanService.repayLoan(loanId, amount);
        return ok(success(updatedLoan, "Loan repayment successful").getJson());
    }

    // Deleting a loan by its id
    @DeleteMapping("/delete/{loanId}")
    @Operation(summary = "Delete a loan", description = "Delete a loan by its ID", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted loan")
    })
    public ResponseEntity<?> deleteLoan(@PathVariable Long loanId) {
        loanService.deleteLoan(loanId);
        return ok(success(null, "Loan deleted successfully").getJson());
    }
}

