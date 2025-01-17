package ch.furchert.authservice.config;

import ch.furchert.authservice.integration.UserManagementClient;
import ch.furchert.authservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class UserDetailsServiceConfig {

    private final UserManagementClient userManagementClient;

    /**
     * Provide a custom UserDetailsService that retrieves user info from the User Management Service.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            UserDto dto = userManagementClient.getUserByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

            // Build a standard Spring Security user with roles
            return User.builder()
                    .username(dto.getUsername())
                    .password(dto.getPasswordHash()) // must be hashed
                    .roles(dto.getRole().name())     // e.g., "ADMIN" or "USER"
                    .build();
        };
    }

    /**
     * Use a strong password encoder that matches the hashing method used in the UMS.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // If your UMS uses BCrypt, match here
        return new BCryptPasswordEncoder();
    }
}
