package tony.example.auction.api.board.review;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import tony.example.auction.api.board.sale.domain.Sale;
import tony.example.auction.auth.domain.User;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@TypeAlias("review")
public class Review {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // 리뷰 작성자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale; // 리뷰 작성한 상품 판매글

    @Column(nullable = false)
    private int rating; // 평점 ( 1 ~ 5 )

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comment; // 리뷰 내용

    private LocalDateTime reviewAt; // 리뷰 작성 시간

    public Review(User user, Sale sale, int rating, String comment) {
        this.user = user;
        this.sale = sale;
        this.rating = rating;
        this.comment = comment;
        this.reviewAt = LocalDateTime.now();
    }
}
