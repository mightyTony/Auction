package tony.example.auction.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {

    private final boolean success = true;
    private T data;
    private String message;
    private final LocalDateTime timestamp = LocalDateTime.now();

    public ApiResponse(T data, String message) {
        this.data = data;
        this.message = message;
    }

    // 성공 응답 생성
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }


}
