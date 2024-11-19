package tony.example.auction.api.product.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tony.example.auction.auth.domain.Role;
import tony.example.auction.auth.domain.User;
import tony.example.auction.common.BaseTimeEntity;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Item extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // 상품 이름

    @Column(nullable = false, unique = true)
    private String code; // 상품 코드

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemCategory category; // 상품 카테고리

    @Column(nullable = false)
    private Integer price; // 가격

    @Column(nullable = false)
    private Integer quantity; // 재고

    @Column(nullable = false)
    private String imageUrl; // 상품 이미지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User seller; // 판매자 id

    public void decreaseQuantity(int quantity) {
        if(this.quantity < quantity){
            throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);
        }
        this.quantity -= quantity;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

    public static Item createItem(String code, String name, ItemCategory itemCategory, Integer price, Integer quantity, String imageUrl, User seller) {
        Item item = new Item();
        item.code = code;
        item.name = name;
        item.category = itemCategory;
        item.price = price;
        item.quantity = quantity;
        item.imageUrl = imageUrl;
        item.seller = seller;
        return item;
    }
}
