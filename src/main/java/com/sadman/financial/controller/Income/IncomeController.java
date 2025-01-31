package com.sadman.financial.controller.Income;

import com.sadman.financial.dto.IncomeRequest;
import com.sadman.financial.entity.Income;
import com.sadman.financial.responses.IncomeResponse;
import com.sadman.financial.service.impl.IncomeService;
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

import com.sadman.financial.helpers.CommonDataHelper;

import static com.sadman.financial.utils.ResponseBuilder.paginatedSuccess;
import static org.springframework.http.ResponseEntity.badRequest;

import static com.sadman.financial.utils.ResponseBuilder.success;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Income activity")
@RequestMapping("/api/v1/financial-activities/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private CommonDataHelper commonDataHelper;




    @PostMapping("/create")
    @Operation(summary = "Log income",
            description = "Log a new income for the user and update their balance.", responses = {
            @ApiResponse(description = "Successfully logged income", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeRequest.class)))
    })
    public ResponseEntity<?> logIncome(@RequestBody @Valid IncomeRequest incomeRequest, BindingResult result) {

        // validation error handling
        if (result.hasErrors()) {
            ErrorResponse errorResponse = ErrorFormatter.formatValidationErrors(result);
            return badRequest().body(errorResponse);
        }

        Income income = incomeService.logIncome(incomeRequest);

        return ok(success(null, "Successfully logged income").getJson());
    }


    @GetMapping("/{incomeId}")
    @Operation(summary = "Get a single income", description = "Get an income by its ID", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = IncomeResponse.class))),
            @ApiResponse(responseCode = "404", description = "Income not found")
    })
    public ResponseEntity<JSONObject> getIncomeById(@PathVariable Long incomeId) {
        IncomeResponse incomeResponse = incomeService.getIncomeById(incomeId);
        return ok(success(incomeResponse, "Successfully retrieved income").getJson());
    }


    @GetMapping("/list")
    @Operation(summary = "show lists of all incomes", description = "show lists of all incomes")
    @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = IncomeResponse.class))
    })
    public ResponseEntity<JSONObject> lists(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                            @RequestParam(value = "size", defaultValue = "10") Integer size,
                                            @RequestParam(value = "sortBy", defaultValue = "") String sortBy,
                                            @RequestParam(value = "search", defaultValue = "") String search
    ) {

        PaginatedResponse response = new PaginatedResponse();
        Map<String, Object> map = incomeService.search(page, size, sortBy, search);
        List<Income> postList = (List<Income>) map.get("lists");
        List<IncomeResponse> responses = postList.stream().map(IncomeResponse::select).toList();
        commonDataHelper.getCommonData(page, size, map, response, responses);
        return ok(paginatedSuccess(response).getJson());
    }


    @PutMapping("/update/{incomeId}")
    @Operation(summary = "Update an income", responses = {
            @ApiResponse(description = "Successfully updated the income",
                    responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeRequest.class)))
    })
    public ResponseEntity<JSONObject> updateIncome(
            @PathVariable Long incomeId,
            @RequestBody IncomeRequest incomeRequest
    ) {
        IncomeResponse updatedIncome = incomeService.updateIncome(incomeId, incomeRequest);
        return ok(success(updatedIncome, "Income updated successfully").getJson());
    }



    @DeleteMapping("/delete/{incomeId}")
    @Operation(summary = "Delete an income", responses = {
            @ApiResponse(description = "Successfully deleted the income",
                    responseCode = "200")
    })
    public ResponseEntity<?> deleteIncomeById(@PathVariable Long incomeId) {
        incomeService.deleteIncomeById(incomeId);
        return ok(success(null, "Income deleted successfully").getJson());
    }



}
