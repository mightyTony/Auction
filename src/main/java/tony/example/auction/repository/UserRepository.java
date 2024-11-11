package tony.example.auction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tony.example.auction.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserId(String userId);
}
