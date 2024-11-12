package tony.example.auction.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {

    private final int status;
    private final String name; // 에러 이름
    private final String code; // 에러 코드
    private final String message; // 에러 메시지

    public ErrorResponse(ErrorCode errorCode) {
        this.status = errorCode.getStatus().value();
        this.name = errorCode.name();
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }
}
