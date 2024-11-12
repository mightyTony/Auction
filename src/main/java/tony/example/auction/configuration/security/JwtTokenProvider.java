package tony.example.auction.configuration.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import tony.example.auction.common.Constrant;
import tony.example.auction.domain.Role;
import tony.example.auction.domain.User;
import tony.example.auction.exception.CustomException;
import tony.example.auction.exception.ErrorCode;
import tony.example.auction.repository.UserRepository;
import tony.example.auction.service.AuthService;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final UserDetailsServiceImpl userDetailsService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserRepository userRepository;

    private static final Key secreyKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public Role getUserRole(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return user.getRole();
    }

    // create token
    public String createAccessToken(String userId) {
        log.info("[createToken] 토큰 생성 시작");
        // Role 조회
        Role role = getUserRole(userId);

        // Claims에 id, role 추가
        Claims claims = Jwts.claims().setSubject(userId);
        claims.put("role", role);
        Date now = new Date();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Constrant.ACCESS_TOKEN_VALID_TIME))
                .signWith(secreyKey, SignatureAlgorithm.HS256)
                .compact();

        log.info("[createToken] 토큰 생성 완료");
        return token;
    }

    // refresh token
    public String createRefreshToken(String userId) {
        log.info("[createRefreshToken] 리프레시 토큰 생성 시작");
        Claims claims = Jwts.claims().setSubject(userId);
        Date now = new Date();

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + Constrant.REFRESH_TOKEN_VALID_TIME))
                .signWith(secreyKey, SignatureAlgorithm.HS256)
                .compact();

        log.info("[createRefreshToken] 리프레시 토큰 생성 완료");

        // redis에 refresh token 저장
        redisTemplate.opsForValue().set(userId, refreshToken, Constrant.REFRESH_TOKEN_VALID_TIME, TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    public String getUsername(String token) {
        log.info("[getUsername] 토큰에서 username 조회 시작");
        String username = Jwts.parserBuilder().setSigningKey(secreyKey).build().parseClaimsJws(token).getBody().getSubject();
        log.info("[getUsername] 토큰에서 username 조회 완료");
        return username;
    }

    public Authentication getAuthentication(String token) {
        log.info("[getAuthentication] 토큰에서 Authentication 조회 시작");
        String username = getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        log.info("[resolveToken] 토큰에서 username 조회 시작 token: {}", request.getHeader("Authorization"));
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }else {
            return null;
        }
    }

    public boolean validateToken(String token) {
        log.info("[validateToken] 토큰 유효성 검사 시작");
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secreyKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("[validateToken] 토큰 유효성 검사 실패");
            return false;
        }
    }

    public String reissueAccessToken(String refreshToken, String userId) {
        // refresh token 유효성 검사
        if(!validateRefreshToken(refreshToken, userId)) {
            log.error("[reissueAccessToken] 리프레시 토큰 유효성 검사 실패");
            throw new IllegalArgumentException("리프레시 토큰이 유효하지 않습니다.");
        }

        // Role 조회
        UserDetails userDetails = userDetailsService.loadUserByUsername(userId);
        //Role role = ((User) userDetails).getRole();

        return createAccessToken(userId);
    }

    // Refresh token 유효성 검사, redis에 저장된 refresh token과 비교
    public boolean validateRefreshToken(String refreshToken, String userId) {
        log.info("[validateRefreshToken] 리프레시 토큰 유효성 검사 시작");
        try{
            // redis에 저장된 refresh token 조회
            String storedToken = (String) redisTemplate.opsForValue().get(userId);
            if(storedToken == null || !storedToken.equals(refreshToken)) {
                log.error("[validateRefreshToken] 리프레시 토큰 없음, 또는 일치하지 않음");
                return false;
            }

            // 토큰의 만료 시간 검증
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(secreyKey).build().parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            log.error("[validateRefreshToken] 리프레시 토큰 유효성 검사 실패!");
            return false;
        }
    }

    // 로그아웃 시 토큰 블랙리스트 처리
    public void logout(String accessToken) {
        long expiration = getExpiration(accessToken);  // 토큰 만료 시간 계산
        redisTemplate.opsForValue().set(accessToken, "blacklisted", expiration, TimeUnit.MILLISECONDS);
    }

    private long getExpiration(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secreyKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        Date expiration = claims.getExpiration();
        return expiration.getTime() - System.currentTimeMillis();
    }


}
