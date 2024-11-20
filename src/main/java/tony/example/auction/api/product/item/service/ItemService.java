package tony.example.auction.api.product.item.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tony.example.auction.api.product.item.domain.Item;
import tony.example.auction.api.product.item.domain.dto.request.ItemCreateRequest;
import tony.example.auction.api.product.item.domain.dto.response.ItemResponse;
import tony.example.auction.api.product.item.repository.ItemRepository;
import tony.example.auction.api.product.item.validator.ItemValidator;
import tony.example.auction.auth.domain.Role;
import tony.example.auction.auth.domain.User;
import tony.example.auction.auth.repository.UserRepository;
import tony.example.auction.auth.validator.AuthValidator;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;
    private final AuthValidator authValidator;
    private final UserRepository userRepository;
    /**
     * ## 상품 관리
     * - [ ] **POST** `/api/items` - 새로운 상품 등록 (`name`, `description`, `code`)
     * - [ ] **GET** `/api/items` - 전체 상품 목록 조회
     * - [ ] **POST** `/api/items/details` - 특정 상품의 상세 정보 조회 (`itemId`)
     * - [ ] **PUT** `/api/items/{id}` - 상품 정보 수정 (`name`, `description`, `code`)
     * - [ ] **DELETE** `/api/items/{id}` - 상품 삭제 (`{id}`)
     */

    //@RequestBody @Valid ItemCreateRequest request
    // 새로운 상품 등록
    //@PreAuthorize("hasRole('ROLE_SELLER')")
    @Transactional
    public void createItem(ItemCreateRequest request, User seller) {
        // 검증 로직 (상품 코드 중복, 상품 코드 형식, 판매자 권한)
        itemValidator.isDuplicateItem(request.getCode());
        itemValidator.validateCode(request.getCode());
//        Role sellerRole = authValidator.checkUserRoleReturnRole(seller.getUserId());
//        itemValidator.validateSellerRole(sellerRole);

        Item item = Item.createItem(
                request.getCode(),
                request.getName(),
                request.getCategory(),
                request.getPrice(),
                request.getQuantity(),
                request.getImageUrl(),
                0,
                seller);
        itemRepository.save(item);

        log.info("[ItemService] createItem - 상품 등록 성공 ITEM : {} , {}", item.getCode(), item.getName());
    }

    public User findSeller(String userId) {
        return userRepository.findByUserId(userId)
                .filter(user -> user.getRole().equals(Role.ROLE_SELLER))
                .orElseThrow(()-> new CustomException(ErrorCode.ONLY_SELLER_CAN_REGISTER_ITEM));
    }

    public Page<ItemResponse> getItemPages(int page, int size, String sort, String direction) {
        Sort sortOrder = Sort.by(Sort.Direction.fromString(direction), sort);
        // page
        Pageable pageable = PageRequest.of(page, size, sortOrder);

        Page<Item> itemsPage = itemRepository.findAll(pageable);

        return itemsPage.map(ItemResponse::from);

    }

//    public Page<Item> findAllItems() {
//        return itemRepository.findAllItems();
//        Pageable pageable = Pageable.unpaged();
//        //PageRequest
//    }
}


