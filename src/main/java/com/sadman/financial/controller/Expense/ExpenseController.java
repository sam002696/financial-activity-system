package com.sadman.financial.controller.Expense;
import com.sadman.financial.dto.ExpenseRequest;
import com.sadman.financial.entity.Expense;
import com.sadman.financial.helpers.CommonDataHelper;
import com.sadman.financial.responses.ExpenseResponse;
import com.sadman.financial.service.impl.ExpenseService;
import com.sadman.financial.utils.ErrorFormatter;
import com.sadman.financial.utils.ErrorResponse;
import com.sadman.financial.utils.PaginatedResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.sadman.financial.utils.ResponseBuilder.paginatedSuccess;
import static com.sadman.financial.utils.ResponseBuilder.success;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Expense activity")
@RequestMapping("/api/v1/financial-activities/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private CommonDataHelper commonDataHelper;

    // Logging new expense
    @PostMapping("/create")
    @Operation(summary = "Log expense", description = "Log a new expense for the user and update their balance.", responses = {
            @ApiResponse(description = "Successfully logged expense", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseRequest.class)))
    })
    public ResponseEntity<?> logExpense(@RequestBody @Valid ExpenseRequest expenseRequest, BindingResult result) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = ErrorFormatter.formatValidationErrors(result);
            return badRequest().body(errorResponse);
        }

        ExpenseResponse expenseResponse = expenseService.logExpense(expenseRequest);
        return ok(success(expenseResponse, "Successfully logged expense").getJson());
    }

    // Getting single expense by its id
    @GetMapping("/{expenseId}")
    @Operation(summary = "Get a single expense", description = "Get an expense by its ID", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ExpenseResponse.class))),
            @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    public ResponseEntity<JSONObject> getExpenseById(@PathVariable Long expenseId) {
        ExpenseResponse expenseResponse = expenseService.getExpenseById(expenseId);
        return ok(success(expenseResponse, "Successfully retrieved expense").getJson());
    }

    // Getting list of expenses
    @GetMapping("/list")
    @Operation(summary = "Show lists of all expenses", description = "Show lists of all expenses")
    @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ExpenseResponse.class))
    })
    public ResponseEntity<JSONObject> listExpenses(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size,
                                                   @RequestParam(value = "sortBy", defaultValue = "") String sortBy,
                                                   @RequestParam(value = "search", defaultValue = "") String search) {

        PaginatedResponse response = new PaginatedResponse();
        Map<String, Object> map = expenseService.search(page, size, sortBy, search);
        List<Expense> expenseList = (List<Expense>) map.get("lists");
        List<ExpenseResponse> responses = expenseList.stream().map(ExpenseResponse::select).toList();
        commonDataHelper.getCommonData(page, size, map, response, responses);
        return ok(paginatedSuccess(response).getJson());
    }

    // Updating an expense by its id
    @PutMapping("/update/{expenseId}")
    @Operation(summary = "Update an expense", responses = {
            @ApiResponse(description = "Successfully updated the expense",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExpenseRequest.class)))
    })
    public ResponseEntity<JSONObject> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseRequest expenseRequest
    ) {
        ExpenseResponse updatedExpense = expenseService.updateExpense(expenseId, expenseRequest);
        return ok(success(updatedExpense, "Expense updated successfully").getJson());
    }

    // Deleting an expense by its id
    @DeleteMapping("/delete/{expenseId}")
    @Operation(summary = "Delete an expense", responses = {
            @ApiResponse(description = "Successfully deleted the expense",
                    responseCode = "200")
    })
    public ResponseEntity<?> deleteExpenseById(@PathVariable Long expenseId) {
        expenseService.deleteExpenseById(expenseId);
        return ok(success(null, "Expense deleted successfully").getJson());
    }
}




