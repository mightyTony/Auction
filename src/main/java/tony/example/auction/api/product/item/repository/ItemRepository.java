package tony.example.auction.api.product.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tony.example.auction.api.product.item.domain.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    boolean existsByCode(String code);

    Optional<Item> findByCode(String code);
}
