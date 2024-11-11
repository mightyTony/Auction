package tony.example.auction.configuration.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 특정 URI에 대해 JWT 토큰 검사 건너뛰기
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/healthcheck") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-resource") ||
                requestURI.startsWith("/api/v1/auth") ||
                requestURI.startsWith("/sign-api")
        ) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtTokenProvider.resolveToken(request);
        log.info("[doFilterInternal] 토큰 추출 완료 token: {}", token);

        log.info("[doFilterInternal] 토큰 유효성 검사 시작");
        if(token != null && jwtTokenProvider.validateToken(token)) {
            log.info("[doFilterInternal] 토큰 유효성 검사 완료");
            Authentication authentication = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("[doFilterInternal] token 유효성 체크 완료");
        }
        filterChain.doFilter(request, response);
    }
}
