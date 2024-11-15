package tony.example.auction.api.board.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tony.example.auction.auth.domain.User;
import tony.example.auction.common.BasePost;

@Entity
@Table(name = "notice")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 공지 사항 게시판
public class Notice extends BasePost {
    private int priority; // 중요도 (0: 일반, 1: 중요)

    public Notice(User author, String title, String content, int priority) {
        super(author, title, content);
        this.priority = priority;
    }
}
