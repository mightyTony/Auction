package tony.example.auction.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // ACCOUNT
    USER_NOT_FOUND(HttpStatus.UNAUTHORIZED, "ACCOUNT-001", "사용자를 찾을 수 없습니다."),
    //USERNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "ACCOUNT-002", "이미 존재하는 사용자명입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "ACCOUNT-003", "이미 존재하는 이메일입니다."),
    PHONE_NUMBER_ALREADY_EXISTS(HttpStatus.CONFLICT, "ACCOUNT-004", "이미 존재하는 전화번호입니다."),
    ONLY_SELLER_CAN_REGISTER_ITEM(HttpStatus.FORBIDDEN, "ACCOUNT-005", "판매자만 상품을 등록할 수 있습니다."),

    // AUTH
    AUTHENTICATION_FAILED(HttpStatus.UNAUTHORIZED, "AUTH-001", "아이디 또는 비밀번호가 일치하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "AUTH-002", "유효하지 않거나 만료된 Refresh Token"),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.FORBIDDEN, "AUTH-003", "Refresh Token을 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-004", "접근 권한이 없습니다."),

    // Chat
    NOT_FOUND_CHAT_ROOM(HttpStatus.NOT_FOUND, "CHAT-001", "채팅방을 찾을 수 없습니다."),

    // ITEM
    DUPLICATE_ITEM(HttpStatus.CONFLICT,"ITEM-001","이미 존재하는 상품 입니다." ),
    DUPLICATE_ITEM_CODE(HttpStatus.CONFLICT,"ITEM-002", "이미 존재하는 상품 코드 입니다." ),
    NOT_ENOUGH_QUANTITY(HttpStatus.CONFLICT,"ITEM-003", "재고 수량이 부족 합니다." ),
    INVALID_ITEM_CODE(HttpStatus.BAD_REQUEST,"ITEM-004","아이템 코드는 3자 이상 20자 이하로 입력해주세요" ),
    INVALID_ITEM_PRICE(HttpStatus.BAD_REQUEST, "ITEM-005","가격은 0원 이상 이어야 합니다." ),
    INVALID_ITEM_QUANTITY(HttpStatus.BAD_REQUEST,"ITEM-006" ,"수량은 0개 이상 이어야 합니다." ),;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
