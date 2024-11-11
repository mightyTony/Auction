package tony.example.auction.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tony.example.auction.common.Constrant;
import tony.example.auction.configuration.security.JwtTokenProvider;
import tony.example.auction.domain.Role;
import tony.example.auction.domain.User;
import tony.example.auction.domain.dto.response.TokenResponse;
import tony.example.auction.repository.UserRepository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    public Role getUserRole(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return user.getRole();
    }

    // 아이디, 비번 체크
    public boolean authCheck(String userId, String rawPassword) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        if(!passwordEncoder.matches(rawPassword, user.getPassword())) {
            return false;
        }
        return true;
    }

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
}
