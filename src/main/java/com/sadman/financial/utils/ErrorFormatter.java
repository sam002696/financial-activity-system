package com.sadman.financial.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

public class ErrorFormatter {

    // Method to format validation errors into the desired structure
    public static ErrorResponse formatValidationErrors(BindingResult result) {
        // Convert the binding result errors into a list of FieldError objects
        List<ErrorResponse.FieldError> errors = result.getFieldErrors().stream()
                .map(fieldError -> new ErrorResponse.FieldError(
                        fieldError.getField(),
                        fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        // Return the formatted error response
        return new ErrorResponse("error", "Validation failed", errors);
    }
}
