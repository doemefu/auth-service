package ch.furchert.authservice.service;

import ch.furchert.authservice.dto.LoginRequest;
import ch.furchert.authservice.dto.TokenResponse;
import ch.furchert.authservice.util.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Optionally you may call the User Management Service via REST here.
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    // Optionally an HttpClient/RestTemplate/WebClient to call User Management
    // private final UserManagementClient userManagementClient;

    @Override
    public TokenResponse login(LoginRequest credentials) {
        // 1. Verify credentials with User Management (e.g., GET /users/validate, or manually check password).
        // 2. If valid, generate JWT and optional refresh token.
        String generatedJwt = jwtTokenProvider.generateToken(credentials.getUsername());
        // If you store refresh tokens, also generate/stash in DB.

        // Return token response
        return new TokenResponse(
                generatedJwt,
                "SOME_REFRESH_TOKEN",
                "Bearer",
                3600 // e.g., 1 hour
        );
    }

    @Override
    public TokenResponse refreshToken(String refreshToken) {
        // 1. Verify refresh token (if stored in DB or if it’s also JWT).
        // 2. Generate new access token (and possibly a new refresh token).
        String newAccessToken = jwtTokenProvider.generateToken("usernameFromRefreshToken");
        return new TokenResponse(
                newAccessToken,
                "NEW_REFRESH_TOKEN",
                "Bearer",
                3600
        );
    }

    @Override
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
}
