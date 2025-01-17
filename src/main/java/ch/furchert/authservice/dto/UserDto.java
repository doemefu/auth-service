package ch.furchert.authservice.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String username;
    private String passwordHash;
    private Role role;

    public enum Role {
        ADMIN, USER
    }
}
