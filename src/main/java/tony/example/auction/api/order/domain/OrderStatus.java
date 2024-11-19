package tony.example.auction.api.order.domain;

public enum OrderStatus {
//    PAYMENT_WAIT, // 결제 대기
//    PAYMENT, // 결제 완료
    NEW, // 신규
    DELIVERY, // 배송
    COMPLETE, // 완료
    CANCEL, // 취소
}
