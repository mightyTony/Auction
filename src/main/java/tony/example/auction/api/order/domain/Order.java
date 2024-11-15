package tony.example.auction.api.order.domain;

import jakarta.persistence.*;
import tony.example.auction.auth.domain.User;
import tony.example.auction.common.BaseTimeEntity;

@Entity
@Table(name = "orders")
public class Order extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String status;
}
