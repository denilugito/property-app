package com.realestate.propertyapp.auth;

import com.realestate.propertyapp.auth.dto.LoginRequest;
import com.realestate.propertyapp.auth.dto.LoginResponse;
import com.realestate.propertyapp.auth.dto.RegisterRequest;
import com.realestate.propertyapp.security.dto.RefreshRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = service.login(
                request.getUsername(),
                request.getPassword()
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        service.register(
                request.username,
                request.password,
                request.role
        );
        return ResponseEntity.ok(Map.of("Msg","User registered successfully"));
    }

    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refresh (
            @RequestBody RefreshRequest request) {

        return ResponseEntity.ok(service.refresh(request.refreshToken()));
    }
}
