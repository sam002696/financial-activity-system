package com.sadman.financial.controller.User;

import com.sadman.financial.dto.UserRequest;
import com.sadman.financial.responses.UserResponse;
import com.sadman.financial.service.impl.UserService;
import com.sadman.financial.utils.ErrorFormatter;
import com.sadman.financial.utils.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import static com.sadman.financial.utils.ResponseBuilder.success;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "User management")
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get user by ID
    @GetMapping("/profile-info")
    @Operation(summary = "Get user profile", description = "Get the details of the logged-in user", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<JSONObject> getUserProfile() {
        UserResponse userResponse = userService.getUserProfile();  // Fetch user profile based on logged-in user
        return ok(success(userResponse, "Successfully retrieved user profile").getJson());
    }

    // Update user profile
    @PutMapping("/profile-update")
    @Operation(summary = "Update user profile", description = "Update user details", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserRequest.class)))
    })
    public ResponseEntity<?> updateUserProfile(
            @RequestBody @Valid UserRequest userRequest,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            ErrorResponse errorResponse = ErrorFormatter.formatValidationErrors(result);
            return badRequest().body(errorResponse);
        }

        UserResponse updatedUser = userService.updateUserProfile(userRequest);
        return ok(success(updatedUser, "User profile updated successfully").getJson());
    }
}
