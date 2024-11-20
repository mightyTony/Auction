package tony.example.auction.common;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ApiResponse<T> {

    private boolean success;
    private T data;
    private String message;
    private String errorCode;
    private final LocalDateTime timestamp = LocalDateTime.now();

    // 성공 응답 생성자
    public ApiResponse(T data, String message) {
        this.success = true;
        this.data = data;
        this.message = message;
    }

    // 실패
    private ApiResponse(String message, String errorCode) {
        this.success = false;
        this.message = message;
        this.errorCode = errorCode;
    }

    // 성공 응답 생성
    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(data, message);
    }

    // 실패 응답
    public static <T> ApiResponse<T> fail(String message, String errorCode) {
        return new ApiResponse<>(message, errorCode);
    }

}
