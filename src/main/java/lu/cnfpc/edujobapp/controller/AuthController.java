package lu.cnfpc.edujobapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lu.cnfpc.edujobapp.dto.request.LoginUserRequestDto;
import lu.cnfpc.edujobapp.dto.request.RegisterUserRequestDto;
import lu.cnfpc.edujobapp.dto.response.AuthUserResponseDto;
import lu.cnfpc.edujobapp.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Register a new user")
    @PostMapping("/register")
    public ResponseEntity<AuthUserResponseDto> register(@Valid @RequestBody RegisterUserRequestDto request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @Operation(summary = "Authenticate user and get token")
    @PostMapping("/login")
    public ResponseEntity<AuthUserResponseDto> login(@Valid @RequestBody LoginUserRequestDto request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
