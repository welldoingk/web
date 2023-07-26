package com.kmpc.web.configuration;

import com.kmpc.web.jwt.JwtAuthFilter;
import com.kmpc.web.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SpringSecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        // resources 자원 접근 허용
        return (web) -> web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        /* csrf 설정 해제. */
        http.csrf(CsrfConfigurer::disable);

        /* JWT 사용 위해 기존의 세션 방식 인증 해제 */
        http.sessionManagement(configurer -> configurer
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        /* URL Mapping */
        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/**", "/**/**").permitAll()
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                // .anyRequest().authenticated()
        );

        http.headers(headers -> headers
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin
                ));

        http.addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        /*
         * 로그인 페이지를 /login으로 설정한다.
         * 1. 인증이 되지 않은 사용자가 permitAll()페이지가 아닌 페이지에 접근할 때 /login으로 강제 이동 시킨다.
         * 2. 이때의 인증은 위에 필터에 등록해 놓은 JWT 토큰의 유무(유효성 검증) 기준이다.
         */
        http.formLogin(login -> login.loginPage("/login"));

        /* 인가 (권한 인증) 실패 시 아래의 핸들러 작동 ex) 멤버인데 -> VIP 멤버의 페이지를 접근하는 경우 */
        http.exceptionHandling(handlingConfigurer -> handlingConfigurer
                .accessDeniedPage("/forbidden"));

        return http.build();
    }

}