package com.realestate.propertyapp.auth;

import com.realestate.propertyapp.auth.dto.LoginResponse;
import com.realestate.propertyapp.security.JwtUtil;
import com.realestate.propertyapp.security.entity.RefreshToken;
import com.realestate.propertyapp.security.service.RefreshTokenService;
import com.realestate.propertyapp.user.entity.User;
import com.realestate.propertyapp.user.repository.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder,
                       JwtUtil jwtUtil, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    public LoginResponse login(String username, String password) {
        User user = userRepository.findByUsername(username).
                orElseThrow(() -> new BadCredentialsException("Invalid credentials"));

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Invalid credentials");
        }

        // Generate access token(JWT)
        String accessToken = jwtUtil.generateAccessToken(user.getUsername(), user.getRole());

        // Generate refresh token
        RefreshToken refreshToken = refreshTokenService.create(user);

        return new LoginResponse(
                accessToken,
                refreshToken.getToken());
    }

    public void register(String username, String password, String role) {

        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(encoder.encode(password)); // Bcrypt
        user.setRole(role);

        userRepository.save(user);
    }

    public LoginResponse refresh(String refreshToken) {

        // validate refresh token
        RefreshToken rt = refreshTokenService.validate(refreshToken);

        // Get user from refresh token
        User user = rt.getUser();

        // Generate new access token
        String newAccessToken = jwtUtil.generateAccessToken(user.getUsername(),
                user.getRole());

        // (Optional but recommended) Rotate refresh token
        refreshTokenService.revoke(rt);
        RefreshToken newRefreshToken = refreshTokenService.create(user);

        // Return both tokens
        return new LoginResponse(
            newAccessToken,
            newRefreshToken.getToken()
        );

    }
}
