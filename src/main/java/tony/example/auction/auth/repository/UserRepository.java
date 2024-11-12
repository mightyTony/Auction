package tony.example.auction.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tony.example.auction.auth.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);

    @Query("SELECT u FROM User u WHERE u.userId = :userId OR u.email =: email")
    boolean existsByUserIdOrEmail(String userId, String email);

    boolean existsByEmail(String email);

    boolean existsByUserId(String userId);

}
