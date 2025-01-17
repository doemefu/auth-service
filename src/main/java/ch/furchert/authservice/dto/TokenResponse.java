package ch.furchert.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TokenResponse {
    private String accessToken;
    private String refreshToken; // if you implement refresh tokens
    private String tokenType;    // e.g., "Bearer"
    private long expiresIn;      // optional, e.g., seconds until expiration
}
