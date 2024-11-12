package tony.example.auction.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tony.example.auction.common.Constrant;
import tony.example.auction.configuration.security.JwtTokenProvider;
import tony.example.auction.auth.domain.dto.request.JoinRequest;
import tony.example.auction.auth.domain.dto.request.LoginRequest;
import tony.example.auction.auth.domain.dto.response.TokenResponse;
import tony.example.auction.auth.service.AuthService;
import tony.example.auction.auth.validator.AuthValidator;

@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "인증 관련 API")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthService authService;
    private final AuthValidator authValidator;

    @Operation(summary = "회원가입", description = "회원가입을 수행하여 새로운 사용자를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "409", description = "이미 존재하는 사용자 ID 또는 이메일", content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/join")
    public ResponseEntity<?> join(@RequestBody JoinRequest request) {
        authService.join(request);
        return ResponseEntity.ok().body("회원가입 성공");
    }

    @Operation(summary = "로그인", description = "아이디와 비밀번호로 로그인하여 AccessToken과 Refresh Token을 발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "401", description = "아이디 또는 비밀번호가 일치하지 않습니다.")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) {
        // 아이디, 비번 검증
        authValidator.authCheck(request.getUserId(), request.getPassword());

        // 인증 성공 시 AccessToken, Refresh Token 생성, 발급
        TokenResponse tokenResponse = authService.sendTokens(request.getUserId());

        // 쿠키 생성
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", tokenResponse.getRefreshToken())
                .httpOnly(true)
                .maxAge(Constrant.REFRESH_TOKEN_VALID_TIME)
                .path("/")
                .secure(true) // https only
                .sameSite("none")
                .build();

        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        return ResponseEntity.ok(TokenResponse.sendAccessToken(tokenResponse.getAccessToken()));
        // return ResponseEntity.ok().body(TokenResponse.of(accessToken, refreshToken));
        // return ResponseEntity.ok().body(new TokenResponse(accessToken, refreshToken));
    }

    @Operation(summary = "로그아웃", description = "로그아웃하고 Refresh Token 쿠키를 삭제합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그아웃 성공"),
            @ApiResponse(responseCode = "400", description = "로그아웃 실패")
    })
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = jwtTokenProvider.resolveToken(request);

        if(accessToken != null && jwtTokenProvider.validateToken(accessToken)) {
            jwtTokenProvider.logout(accessToken);

            ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                    .httpOnly(true)
                    .maxAge(0)
                    .path("/")
                    .secure(true)
                    .sameSite("none")
                    .build();

            response.addHeader("Set-Cookie", refreshTokenCookie.toString());

            return ResponseEntity.ok("로그아웃 성공");
        } else {
            return ResponseEntity.badRequest().body("로그아웃 실패");
        }
    }

    // 재발급
    @Operation(summary = "Access Token 재발급", description = "유효한 Refresh Token을 사용하여 새로운 Access Token을 재발급합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "403", description = "유효하지 않거나 만료된 Refresh Token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken, HttpServletResponse response) {
        if(refreshToken == null) {
            throw new RuntimeException("리프레시 토큰이 존재하지 않습니다.");
        }
        log.info("refreshToken: {}", refreshToken);

        String userId = jwtTokenProvider.getUsername(refreshToken);
        String newAccessToken = authService.refreshAccessToken(refreshToken, userId);

        return ResponseEntity.ok(TokenResponse.sendAccessToken(newAccessToken));
    }
}
