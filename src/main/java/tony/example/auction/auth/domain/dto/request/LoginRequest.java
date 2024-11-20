package tony.example.auction.auth.domain.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Schema(description = "로그인 요청 데이터")
@Data
public class LoginRequest {
    @Schema(description = "사용자 ID", example = "user123", required = true)
    @NotNull
    private String userId;
    @Schema(description = "비밀번호", example = "password123", required = true)
    @NotNull
    private String password;
}
