package tony.example.auction.api.product.item.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tony.example.auction.common.BaseTimeEntity;

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

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemOption> options; // 상품 옵션들

    public Item(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public void addOption(ItemOption option) {
        this.options.add(option);
    }
}
