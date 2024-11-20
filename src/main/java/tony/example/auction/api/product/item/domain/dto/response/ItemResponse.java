package tony.example.auction.api.product.item.domain.dto.response;

import lombok.Builder;
import lombok.Data;
import tony.example.auction.api.product.item.domain.Item;
import tony.example.auction.api.product.item.domain.ItemCategory;

@Data
public class ItemResponse {
    private Long id;
    private String name;
    private String code;
    private ItemCategory category;
    private Integer price;
    private Integer quantity;
    private String imageUrl;

    @Builder
    public ItemResponse(Long id, String name, String code, ItemCategory category, Integer price, Integer quantity, String imageUrl) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public static ItemResponse from(Item item) {
        return ItemResponse.builder()
                .id(item.getId())
                .name(item.getName())
                .code(item.getCode())
                .category(item.getCategory())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .imageUrl(item.getImageUrl())
                .build();
    }
}
