package ch.furchert.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
public class AuthorizationServerConfig {

    /**
     * Applies the standard Authorization Server security config
     * for the default OAuth2 endpoints (token, jwk, etc.).
     */
    @Bean
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer =
                OAuth2AuthorizationServerConfigurer.authorizationServer();

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults())
                .with(authorizationServerConfigurer, withDefaults());

        return http.build();
    }

    /**
     * Authorization Server Settings (like issuer).
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                // .issuer("https://auth.example.com")
                .build();
    }

    /**
     * We use a JdbcRegisteredClientRepository to store clients in authdb.
     * On startup, we can programmatically create & save the clients.
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        JdbcRegisteredClientRepository repo = new JdbcRegisteredClientRepository(jdbcTemplate);

        // Example: "frontend-client" with authorization_code + refresh_token
        RegisteredClient frontendClient = RegisteredClient.withId("frontend-client-id")
                .clientId("frontend-client")
                .clientSecret("{noop}frontend-secret")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("https://my-frontend-app/callback")
                .scope("openid")
                .scope("profile")
                .build();

        // Example: "hardware-device" with client_credentials flow
        RegisteredClient hardwareClient = RegisteredClient.withId("hardware-device-id")
                .clientId("hardware-device")
                .clientSecret("{noop}super-secure-device-secret")
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("iot.read")
                .scope("iot.write")
                .build();

        // Save them if not already present
        saveIfNotPresent(repo, frontendClient);
        saveIfNotPresent(repo, hardwareClient);

        return repo;
    }

    /**
     * Provide a JdbcOAuth2AuthorizationService to store tokens/authorizations in the database.
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(
            JdbcTemplate jdbcTemplate,
            RegisteredClientRepository registeredClientRepository
    ) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    private void saveIfNotPresent(JdbcRegisteredClientRepository repo, RegisteredClient client) {
        RegisteredClient existing = repo.findByClientId(client.getClientId());
        if (existing == null) {
            repo.save(client);
        }
    }
}
