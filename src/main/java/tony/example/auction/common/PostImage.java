package tony.example.auction.common;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tony.example.auction.api.board.sale.domain.Sale;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "post_image")
public class PostImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale; // 연결된 판매 게시글

    @Column(nullable = false)
    private String imageUrl; // 이미지 URL

    @Column(name = "is_main")
    private boolean isMain; // 대표 이미지 여부

    public PostImage(Sale sale, String imageUrl, boolean isMain) {
        this.sale = sale;
        this.imageUrl = imageUrl;
        this.isMain = isMain;
    }
}
