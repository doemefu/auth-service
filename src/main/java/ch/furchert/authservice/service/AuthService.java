package ch.furchert.authservice.service;

import ch.furchert.authservice.dto.LoginRequest;
import ch.furchert.authservice.dto.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest credentials);
    TokenResponse refreshToken(String refreshToken);
    boolean validateToken(String token);
}
