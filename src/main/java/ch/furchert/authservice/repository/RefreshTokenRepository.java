package ch.furchert.authservice.repository;

import ch.furchert.authservice.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByToken(String token);
    void deleteByToken(String token);
}
