package tony.example.auction.api.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tony.example.auction.api.product.item.domain.Item;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "order_item")
public class OrderItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    private Integer quantity;

    private Integer price;

    public OrderItem(Item item, Integer quantity, Integer price) {
        this.item = item;
        this.quantity = quantity;
        this.price = price;
    }

    public static OrderItem createOrderItem(Item item, int quantity) {
        if(item.getQuantity() < quantity){
            throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);
        }
        item.decreaseQuantity(quantity);
        return new OrderItem(item, quantity, item.getPrice());
    }

    OrderItem associateOrder(Order order) {
        this.order = order;
        return this;
    }
}
