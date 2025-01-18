package ch.furchert.authservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DefaultSecurityConfig {

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Open the Authorization Server's endpoints.
                        // (They'll be handled by AuthorizationServerConfig anyway.)
                        .requestMatchers(
                                "/oauth2/**",
                                "/actuator/**",
                                "/swagger-ui/**",
                                "/api-docs/**",
                                "/swagger-ui.html")
                        .permitAll() // Adjust access as needed
                        .anyRequest().authenticated()
                )
                // We can allow formLogin or HTTP Basic, etc.
                .formLogin(Customizer.withDefaults());

        return http.build();
    }
}
