package tony.example.auction.api.product.item.domain.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import tony.example.auction.api.product.item.domain.ItemCategory;

@Getter
public class ItemCreateRequest {
    @NotNull
    private String name; // 상품 이름
    @NotNull
    private String code; // 상품 코드
    @NotNull
    private ItemCategory category; // 상품 카테고리
    @NotNull
    private Integer price; // 상품 가격
    @NotNull
    private Integer quantity; // 상품 수량
    @NotNull
    private String imageUrl; // 상품 이미지 URL

    // for test
    @Builder
    public ItemCreateRequest(String name, String code, ItemCategory category, Integer price, Integer quantity, String imageUrl) {
        this.name = name;
        this.code = code;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }
}
