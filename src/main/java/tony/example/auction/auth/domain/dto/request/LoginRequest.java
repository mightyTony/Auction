package tony.example.auction.auth.domain.dto.request;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}
