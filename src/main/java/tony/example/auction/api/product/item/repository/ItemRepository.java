package tony.example.auction.api.product.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tony.example.auction.api.product.item.domain.Item;

import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    //@Query("SELECT i FROM Item i where i.code =: code")
    boolean existsByCode(String code);

    //@Query("SELECT i FROM Item i JOIN FETCH i.seller WHERE i.code = :code")
    Optional<Item> findByCode(@Param("code") String code);
}
