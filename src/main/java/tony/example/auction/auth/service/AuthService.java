package tony.example.auction.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tony.example.auction.common.Constrant;
import tony.example.auction.configuration.security.JwtTokenProvider;
import tony.example.auction.auth.domain.User;
import tony.example.auction.auth.domain.dto.request.JoinRequest;
import tony.example.auction.auth.domain.dto.response.TokenResponse;
import tony.example.auction.auth.repository.UserRepository;
import tony.example.auction.auth.validator.AuthValidator;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthValidator authValidator;

    // 로그인 처리 및 토큰 발급
    public TokenResponse sendTokens(String userId) {
        // 기존 리프레시 토큰 삭제
        if(Boolean.TRUE.equals(redisTemplate.hasKey(userId))) {
            redisTemplate.delete(userId);
        }

        String accessToken = jwtTokenProvider.createAccessToken(userId);
        String refreshToken = jwtTokenProvider.createRefreshToken(userId);

        // Redis에 리프레시 토큰 저장
        redisTemplate.opsForValue().set(userId, refreshToken, Constrant.REFRESH_TOKEN_VALID_TIME, TimeUnit.MILLISECONDS);

        return TokenResponse.of(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken, String userId) {
        String storedToken = (String) redisTemplate.opsForValue().get(userId);
        if(storedToken == null || !storedToken.equals(refreshToken)) {
            throw new RuntimeException("리프레시 토큰이 일치하지 않습니다.");
        }

        return jwtTokenProvider.createAccessToken(userId);
    }

    @Transactional
    public void join(JoinRequest request) {
        // 유효성 검사
        authValidator.validate(request);
        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        User user = User.forUser(
                request.getUserId(),
                request.getUsername(),
                encodedPassword,
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber());

        userRepository.save(user);
    }
}
