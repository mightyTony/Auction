package tony.example.auction.api.product.item.domain.dto.response;

import lombok.Getter;
import tony.example.auction.api.product.item.domain.Item;

@Getter
public class ItemCreateResponse {
    private String name; // 상품 이름

    private String code; // 상품 코드

    // ItemOption

    private Integer stock; // 재고
    private Integer price; // 가격
    private String optionName1; // 옵션 이름 1
    private String optionName2; // 옵션 이름 2

}
