package tony.example.auction.api.product.item.validator;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import tony.example.auction.api.product.item.repository.ItemRepository;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;

@Component
@RequiredArgsConstructor
@Slf4j
public class ItemValidator {
    private final ItemRepository itemRepository;

    public void isDuplicateItem(String itemCode) {
        if(itemRepository.existsByCode(itemCode)){
            log.error("[아이템 등록] 중복된 아이템 코드입니다. itemCode : {}", itemCode);
            throw new CustomException(ErrorCode.DUPLICATE_ITEM_CODE);
        }
    }

    public void validateCode(String itemCode) {
        if(itemCode.length() < 3 || itemCode.length() > 20) {
            log.error("[아이템 등록] 아이템 코드는 3자 이상 20자 이하로 입력해주세요. itemCode : {}", itemCode);
            throw new CustomException(ErrorCode.INVALID_ITEM_CODE);
        }
    }

    public void validatePriceAndQuantity(Integer price, Integer quantity) {
        if(price < 0) {
            log.error("[아이템 등록] 가격은 0 이상이어야 합니다. price : {}", price);
            throw new CustomException(ErrorCode.INVALID_ITEM_PRICE);
        }
        if(quantity < 0) {
            log.error("[아이템 등록] 수량은 0 이상이어야 합니다. quantity : {}", quantity);
            throw new CustomException(ErrorCode.INVALID_ITEM_QUANTITY);
        }
    }

//    public void validateSellerRole(Role sellerRole) {
//        // 판매자만 상품 등록 가능
//        if(sellerRole != Role.ROLE_SELLER) {
//            throw new CustomException(ErrorCode.ONLY_SELLER_CAN_REGISTER_ITEM);
//        }
//    }
}
