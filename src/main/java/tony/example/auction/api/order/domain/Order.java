package tony.example.auction.api.order.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tony.example.auction.auth.domain.User;
import tony.example.auction.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>(); // 주문 상품 상세 목록 (주문 상품들)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    private Order(User user) {
        this.user = user;
        this.status = OrderStatus.NEW;
    }

    public static Order createOrder(User user, List<OrderItem>orderItems) {
        Order order = new Order(user);
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        return order;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem.associateOrder(this));
    }

    public void cancelOrder(){
        if(status == OrderStatus.CANCEL){
            throw new IllegalStateException("이미 취소된 주문입니다.");
        }
        this.status = OrderStatus.CANCEL;
    }

    public void completeOrder() {
        if(status == OrderStatus.COMPLETE){
            throw new IllegalStateException("이미 완료된 주문입니다.");
        }
        this.status = OrderStatus.COMPLETE;
    }
}
