package lu.cnfpc.edujobapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lu.cnfpc.edujobapp.dto.response.DashboardResponse;
import lu.cnfpc.edujobapp.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Dashboard statistics APIs")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @Operation(summary = "Get dashboard statistics for the logged-in user")
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboardStats() {
        return ResponseEntity.ok(dashboardService.getUserStats());
    }
}
