package tony.example.auction.api.order.domain;

import jakarta.persistence.*;
import tony.example.auction.api.product.item.domain.Item;

@Entity
@Table(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private int quantity;

    private Integer price;

}
