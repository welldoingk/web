package com.kmpc.web.member.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.kmpc.web.member.dto.MemberSessionDto;
import com.kmpc.web.util.RedisUtil;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class TokenProvider  {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private final RedisUtil redisUtil;

    private final long tokenValidityInSecond;
    public final static long TOKEN_VALIDATION_SECOND = 1000L * 600; // 10분
    public final static long REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 60 * 60 * 24 * 7; // 7일
    //액세스토큰과 리프레쉬 토큰의 만료시간 설정

    final static public String ACCESS_TOKEN_NAME = "accessToken";
    final static public String REFRESH_TOKEN_NAME = "refreshToken";
    //토큰 이름 설정
    @Value("${jwt.secret}")
    private String SECRET_KEY;
	//키 발급에 사용될 시크릿키 (yaml 파일에서 값을 가져온다.)
    public TokenProvider(RedisUtil redisUtil, @Value("${jwt.token-validity-in-seconds}") long tokenValidityInSecond) {
        this.redisUtil = redisUtil;
        this.tokenValidityInSecond = tokenValidityInSecond;
    }

    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    } //서명키를 시크릿키를 바탕으로 암호화해 설정한다.

    public Claims extractAllClaims(String token) throws ExpiredJwtException {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    } // 식별을 위해 저장했거나 저장된 값들을 다시 가져오기 위한 메소드(압축해제와 비슷하다.)

    public String getUsername(String token) {
        return extractAllClaims(token).get("username", String.class);
    } //payload 에서 유저이름을 가져온다.

    public Boolean isTokenExpired(String token) {
        final Date expiration = extractAllClaims(token).getExpiration();
        return expiration.before(new Date());
    } //토큰이 만료되었는지 확인해주는 메소드

    public String generateToken(MemberSessionDto member) {
        return doGenerateToken(member.getUsername(), TOKEN_VALIDATION_SECOND);
    }

    public String generateRefreshToken(MemberSessionDto member) {
        return doGenerateToken(member.getUsername(), REFRESH_TOKEN_VALIDATION_SECOND);
    }

    public String doGenerateToken(String username, long expireTime) {

        Claims claims = Jwts.claims();
        claims.put("username", username);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();

        return jwt;
    } //토큰을 생성해주는 메소드 (claim에 식별을 위한 다양한 정보를 저장할 수 있다.)

    public Long getExpireTime(String token) {
        return extractAllClaims(token).getExpiration().getTime();
    } //토큰의 만료시간을 반환해주는 메소드

    public Boolean validateToken(String token) {
        if (redisUtil.hasKeyBlackList("LOGOUT_"+token)) {
            return false;
        }
        return true;
    } //토큰을 검증해주는 메소드. 탈취당한 토큰이라면 접근을 거부시킨다.
}