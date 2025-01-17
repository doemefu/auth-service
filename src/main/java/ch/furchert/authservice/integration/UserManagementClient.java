package ch.furchert.authservice.integration;

import ch.furchert.authservice.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserManagementClient {

    private final WebClient.Builder webClientBuilder;

    private static final String USER_MGMT_BASE_URL = "http://user-management-service:8080";

    public Optional<UserDto> getUserByUsername(String username) {
        try {
            return Optional.ofNullable(
                    webClientBuilder.build()
                            .get()
                            .uri(USER_MGMT_BASE_URL + "/users/search/findByUsername?username={username}", username)
                            .retrieve()
                            .bodyToMono(UserDto.class)
                            .block()
            );
        } catch (Exception e) {
            // handle exception or return Optional.empty()
            return Optional.empty();
        }
    }
}
