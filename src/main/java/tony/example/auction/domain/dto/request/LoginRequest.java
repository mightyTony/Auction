package tony.example.auction.domain.dto.request;

import lombok.Data;
import lombok.Getter;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}
