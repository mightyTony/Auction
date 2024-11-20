package tony.example.auction.api.product.item.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    @Operation(
            summary = "상품 등록",
            description = "판매자가 새로운 상품을 등록합니다. 이 API는 ROLE_SELLER 권한이 있어야 사용할 수 있습니다."
    )
    @PostMapping()
    @PreAuthorize("hasRole('ROLE_SELLER')")
    public ResponseEntity<ApiResponse<Void>> createItem(@RequestBody @Valid ItemCreateRequest request, @AuthenticationPrincipal User user) {
        //User seller = itemService.findSeller(userId);
        itemService.createItem(request, user);

        return ResponseEntity.ok(ApiResponse.success(null, "상품 등록 성공"));
    }



}
