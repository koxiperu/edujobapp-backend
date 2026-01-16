package lu.cnfpc.edujobapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lu.cnfpc.edujobapp.dto.request.CompanyRequest;
import lu.cnfpc.edujobapp.dto.response.ApplicationResponse;
import lu.cnfpc.edujobapp.dto.response.CompanyResponse;
import lu.cnfpc.edujobapp.service.ApplicationService;
import lu.cnfpc.edujobapp.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/institutions")
@Tag(name = "Institution", description = "Institution/Company management APIs")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private ApplicationService applicationService;

    @Operation(summary = "Get all institutions for the logged-in user")
    @GetMapping
    public ResponseEntity<List<CompanyResponse>> getMyCompanies() {
        return ResponseEntity.ok(companyService.getMyCompanies());
    }

    @Operation(summary = "Create a new institution")
    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.createCompany(request));
    }

    @Operation(summary = "Get a single institution by ID")
    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompanyById(@PathVariable Long id) {
        return ResponseEntity.ok(companyService.getCompanyById(id));
    }

    @Operation(summary = "Update an institution")
    @PutMapping("/{id}")
    public ResponseEntity<CompanyResponse> updateCompany(@PathVariable Long id, @Valid @RequestBody CompanyRequest request) {
        return ResponseEntity.ok(companyService.updateCompany(id, request));
    }

    @Operation(summary = "Delete an institution")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }
    
    @Operation(summary = "Get all applications for a specific institution")
    @GetMapping("/{id}/applications")
    public ResponseEntity<List<ApplicationResponse>> getApplicationsByCompany(@PathVariable Long id) {
        // Access control check handled in Service or implicit by data ownership check
        // Ideally verify access to company first
        companyService.getCompanyById(id);
        return ResponseEntity.ok(applicationService.getApplicationsByCompany(id));
    }
}
