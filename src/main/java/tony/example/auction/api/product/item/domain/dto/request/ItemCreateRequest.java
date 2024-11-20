package tony.example.auction.api.product.item.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tony.example.auction.api.product.item.domain.ItemCategory;

@Schema(description = "상품 등록 요청 데이터")
@NoArgsConstructor
@Getter
public class ItemCreateRequest {
    @Schema(description = "상품 이름", example = "펜더 기타", required = true)
    @NotNull
    private String name;

    @Schema(description = "상품 코드(고유 값)", example = "Fender-JPN-TELE", required = true)
    @NotNull
    private String code;

    @Schema(description = "상품 카테고리", example = "GUITAR", required = true)
    @NotNull
    private ItemCategory category;

    @Schema(description = "상품 가격", example = "1000", required = true)
    @NotNull
    private Integer price;

    @Schema(description = "상품 재고 수량", example = "50", required = true)
    @NotNull
    private Integer quantity;

    @Schema(description = "상품 이미지 URL", example = "http://example.com/item-image.jpg", required = true)
    @NotNull
    private String imageUrl;

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
