package com.sadman.financial.controller.Dashboard;

import com.sadman.financial.responses.DashboardResponse;
import com.sadman.financial.service.impl.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sadman.financial.utils.ResponseBuilder.success;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@Tag(name = "Dashboard activity")
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    // Getting Dashboard Data
    @GetMapping("/user")
    @Operation(summary = "Get Dashboard data", description = "Retrieve summary of financial data for the user", responses = {
            @ApiResponse(responseCode = "200", content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = DashboardResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<JSONObject> getDashboardData() {
        DashboardResponse dashboardData = dashboardService.getDashboardData();

        return ok(success(dashboardData, "retrieved dashboard data").getJson());
    }
}
