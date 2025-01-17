package ch.furchert.authservice;

import ch.furchert.authservice.dto.LoginRequest;
import ch.furchert.authservice.dto.TokenResponse;
import ch.furchert.authservice.service.AuthService;
import ch.furchert.authservice.service.AuthServiceImpl;
import ch.furchert.authservice.util.JwtTokenProvider;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Test
    void testLogin() {
        JwtTokenProvider mockProvider = mock(JwtTokenProvider.class);
        when(mockProvider.generateToken("john")).thenReturn("fake-jwt-for-john");

        AuthService authService = new AuthServiceImpl(mockProvider);
        LoginRequest request = new LoginRequest();
        request.setUsername("john");
        request.setPassword("pass");

        TokenResponse response = authService.login(request);

        assertNotNull(response);
        assertEquals("fake-jwt-for-john", response.getAccessToken());
    }
}
