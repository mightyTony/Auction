package tony.example.auction.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tony.example.auction.auth.domain.dto.response.UserInformationResponse;
import tony.example.auction.auth.service.UserService;
import tony.example.auction.auth.validator.AuthValidator;
import tony.example.auction.common.ApiResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;
    private final AuthValidator authValidator;

    // 특정 사용자 정보 조회 (userId)
    @PostMapping("/info")
    public ResponseEntity<ApiResponse<UserInformationResponse>>  getUserInformation(String userId) {
        // 본인 인지 확인
        authValidator.isYou(userId);

        // 사용자 정보 조회
        UserInformationResponse response = userService.getUserInformation(userId);

        return ResponseEntity.ok(ApiResponse.success(response, "사용자 정보 조회 성공"));
    }
}
