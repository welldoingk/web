package com.kmpc.web.configuration;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.kmpc.web.member.security.jwt.JwtAccessDeniedHandler;
import com.kmpc.web.member.security.jwt.JwtAuthenticationEntryPoint;
import com.kmpc.web.member.security.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

        private final TokenProvider tokenProvider;
        private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
        private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

        // PasswordEncoder는 BCryptPasswordEncoder를 사용
        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
                httpSecurity
                                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                                .csrf(CsrfConfigurer::disable)

                                .exceptionHandling(handlingConfigurer -> handlingConfigurer
                                                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                                                .accessDeniedHandler(jwtAccessDeniedHandler))

                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/api/authenticate", "/api/signup", "/favicon.ico")
                                                .permitAll()
                                                .requestMatchers("/**").permitAll()
                                                .requestMatchers(PathRequest.toH2Console()).permitAll()
                                                .anyRequest().authenticated())

                                .sessionManagement(configurer -> configurer
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                                .headers(head -> head
                                                .frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                                .formLogin(login -> login.loginPage("/login")
                                                .loginProcessingUrl("/api/authenticate")
                                                .defaultSuccessUrl("/", true)
                                                .permitAll());

                                // .apply(new JwtSecurityConfig(tokenProvider)); // JwtFilter를 addFilterBefore로 등록했던
                                                                              // JwtSecurityConfig class 적용

                return httpSecurity.build();
        }

}