package tony.example.auction.configuration.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web
                .ignoring().requestMatchers(
                        "/h2-console/**",
                        "/favicon.ico",
                        "/error",
                        "/css/**",
                        "/js/**",
                        "/img/**",
                        "/swagger-ui/**",
                        "/swagger-resources/**",
                        "/v3/api-docs/**"
                        );
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        //FIXME 배포 시 변경 필요
        //corsConfig.setAllowedOrigins(List.of("http://localhost:8080"));
        corsConfig.setAllowedOriginPatterns(List.of("*"));

        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        // header
        corsConfig.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .cors(cors -> cors.configurationSource(corsConfiguration()))
                .httpBasic(basic -> basic.disable())
                .csrf((csrf) -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/swagger-ui/**", "/swagger-resources/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations())
                )
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/","/api/v1/auth/**").permitAll()
                        .anyRequest().permitAll()
                )
                .exceptionHandling(except -> except
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
