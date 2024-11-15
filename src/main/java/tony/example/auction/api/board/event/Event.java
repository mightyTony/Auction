package tony.example.auction.api.board.event;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import tony.example.auction.auth.domain.User;
import tony.example.auction.common.BasePost;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Event extends BasePost {

    private LocalDateTime startDate; // 이벤트 시작일, 언제 시작할 지 모르니 LocalDateTime.now() 사용 X
    private LocalDateTime endDate; // 이벤트 종료일

    public Event(User author, String title, String content, LocalDateTime startDate, LocalDateTime endDate) {
        super(author, title, content);
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
