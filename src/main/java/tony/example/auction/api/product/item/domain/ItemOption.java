package tony.example.auction.api.product.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "item_option")
public class ItemOption {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @Column(nullable = false)
    private Integer stock; // 재고

    @Column(nullable = false)
    private Integer price; // 가격

    private String optionName1; // 옵션 이름 1

    private String optionName2; // 옵션 이름 2

    public static ItemOption noOptionItem(Item item, Integer stock, Integer price) {
        ItemOption itemOption = new ItemOption();
        itemOption.item = item;
        itemOption.stock = stock;
        itemOption.price = price;
        return itemOption;
    }

    public static ItemOption oneOptionItem(Item item, Integer stock, Integer price, String optionName1) {
        ItemOption itemOption = new ItemOption();
        itemOption.item = item;
        itemOption.stock = stock;
        itemOption.price = price;
        itemOption.optionName1 = optionName1;
        return itemOption;
    }

    public static ItemOption twoOptionItem(Item item, Integer stock, Integer price, String optionName1, String optionName2) {
        ItemOption itemOption = new ItemOption();
        itemOption.item = item;
        itemOption.stock = stock;
        itemOption.price = price;
        itemOption.optionName1 = optionName1;
        itemOption.optionName2 = optionName2;
        return itemOption;
    }


}
