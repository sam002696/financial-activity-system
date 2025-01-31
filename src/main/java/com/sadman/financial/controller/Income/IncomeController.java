package com.sadman.financial.controller.Income;

import com.sadman.financial.dto.IncomeRequest;
import com.sadman.financial.entity.Income;
import com.sadman.financial.service.impl.IncomeService;
import com.sadman.financial.utils.ErrorFormatter;
import com.sadman.financial.utils.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sadman.financial.utils.ResponseBuilder.error;
import static org.springframework.http.ResponseEntity.badRequest;

import static com.sadman.financial.utils.ResponseBuilder.success;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Income activity")
@RequestMapping("/api/v1/financial-activities/income")
public class IncomeController {

    @Autowired
    private IncomeService incomeService;

    @PostMapping("/create")
    @Operation(summary = "Log income",
            description = "Log a new income for the user and update their balance.", responses = {
            @ApiResponse(description = "Successfully logged income", responseCode = "200",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IncomeRequest.class)))
    })
    public ResponseEntity<?> logIncome(@RequestBody @Valid IncomeRequest incomeRequest, BindingResult result) {

        // If there are validation errors
        if (result.hasErrors()) {
            // Use ErrorFormatter to format the errors
            ErrorResponse errorResponse = ErrorFormatter.formatValidationErrors(result);
            return badRequest().body(errorResponse);
        }

        Income income = incomeService.logIncome(incomeRequest);

        return ok(success(null, "Successfully logged income").getJson());
    }
}
