package tony.example.auction.api.board.sale.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tony.example.auction.api.board.review.Review;
import tony.example.auction.api.product.item.domain.Item;
import tony.example.auction.auth.domain.User;
import tony.example.auction.common.BasePost;
import tony.example.auction.common.PostImage;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
// 판매 글
public class Sale extends BasePost {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; // 상품 정보

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images; // 판매글 이미지들

    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews;

    public Sale(User author, String title, String content, Item item) {
        super(author, title, content);
        this.item = item;
    }

}
