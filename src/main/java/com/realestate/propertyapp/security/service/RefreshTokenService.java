package com.realestate.propertyapp.security.service;

import com.realestate.propertyapp.security.entity.RefreshToken;
import com.realestate.propertyapp.security.repository.RefreshTokenRepository;
import com.realestate.propertyapp.user.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository repository;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpirationMs;

    public RefreshTokenService(RefreshTokenRepository repository) {
        this.repository = repository;
    }

    public RefreshToken create(User user) {
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiresAt(LocalDateTime.now().plusSeconds(refreshExpirationMs / 1000));
        //rt.setExpiresAt(LocalDateTime.now().plusDays(7));
        return repository.save(rt);
    }

    public RefreshToken validate(String token) {
        RefreshToken rt = repository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (rt.isRevoked()) {
            repository.delete(rt);
            throw new RuntimeException("Refresh token revoked");
        }

        if (rt.getExpiresAt().isBefore(LocalDateTime.now())) {
            repository.delete(rt);
            throw new RuntimeException("Refresh token expired");
        }

        return rt;
    }

    public void revoke(RefreshToken token) {
        token.setRevoked(true);
        repository.save(token);
    }
}
