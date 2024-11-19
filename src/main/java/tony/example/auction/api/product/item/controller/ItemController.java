package tony.example.auction.api.product.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tony.example.auction.api.product.item.domain.dto.request.ItemCreateRequest;
import tony.example.auction.api.product.item.service.ItemService;
import tony.example.auction.auth.domain.User;
import tony.example.auction.common.ApiResponse;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/items")
public class ItemController {

    // TODO
    /**
     * ## 상품 관리
     * - [ ] **POST** `/api/items` - 새로운 상품 등록 (`name`, `description`, `code`)
     * - [ ] **GET** `/api/items` - 전체 상품 목록 조회
     * - [ ] **POST** `/api/items/details` - 특정 상품의 상세 정보 조회 (`itemId`)
     * - [ ] **PUT** `/api/items/{id}` - 상품 정보 수정 (`name`, `description`, `code`)
     * - [ ] **DELETE** `/api/items/{id}` - 상품 삭제 (`{id}`)
     */
    private final ItemService itemService;

    @PostMapping()
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponse<Void>> createItem(@RequestBody @Valid ItemCreateRequest request, String userId) {
        User seller = itemService.findSeller(userId);
        itemService.createItem(request, seller);

        return ResponseEntity.ok(ApiResponse.success(null, "상품 등록 성공"));
    }



}
