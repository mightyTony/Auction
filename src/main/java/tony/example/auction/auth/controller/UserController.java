package tony.example.auction.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tony.example.auction.auth.domain.dto.request.UserInformationUpdateRequest;
import tony.example.auction.auth.domain.dto.response.UserInformationResponse;
import tony.example.auction.auth.service.UserService;
import tony.example.auction.auth.validator.AuthValidator;
import tony.example.auction.common.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@Slf4j
public class UserController {

    private final UserService userService;
    private final AuthValidator authValidator;

    // 특정 사용자 정보 조회 (userId)
    @Operation(
            summary = "사용자 정보 조회",
            description = "특정 사용자의 정보를 조회합니다. 본인 확인을 통해 접근 가능하며, 사용자 ID를 입력받아 처리합니다."
    )
    @PostMapping("/info")
    public ResponseEntity<ApiResponse<UserInformationResponse>>  getUserInformation(@RequestParam(name = "userId") String userId) {
        // 본인 인지 확인
        authValidator.isYou(userId);

        // 사용자 정보 조회
        UserInformationResponse response = userService.getUserInformation(userId);

        return ResponseEntity.ok(ApiResponse.success(response, "사용자 정보 조회 성공"));
    }

    // 사용자 정보 수정 /api/users/{id}`
    @Operation(
            summary = "사용자 정보 수정",
            description = "사용자 정보를 수정. 본인 확인 후 사용자 정보를 수정합니다."
    )
    @PatchMapping("/info/update/{userId}")
    public ResponseEntity<ApiResponse<UserInformationResponse>> updateUserInformation(
            @PathVariable("userId") String userId,
            @RequestBody UserInformationUpdateRequest request) {
        // 본인 인지 확인
        authValidator.isYou(userId);
        // 사용자 정보 수정
        UserInformationResponse response = userService.updateUserInformation(userId, request);

        log.info("[유저 정보 수정] 유저 아이디 : {}", response.getUserId());
        return ResponseEntity.ok(ApiResponse.success(response, "사용자 정보 수정 성공"));
    }

    // 사용자 삭제 /api/users/{id}
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable String userId) {
        // 본인 인지 확인
        authValidator.isYou(userId);
        // 사용자 삭제
        userService.deleteUser(userId);
        log.info("[유저 삭제] 유저 아이디 : {}", userId);
        return ResponseEntity.ok(ApiResponse.success(null, "사용자 삭제 성공"));
    }
}
