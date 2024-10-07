package edu.example.dev_2_cc.jwt;

import edu.example.dev_2_cc.exception.JWTException;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@Log4j2
public class JWTUtil {
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }   //양방향 대칭키 방식 사용 : HS256

    //MemberId 확인 메서드
    public String getMemberId(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("memberId", String.class);
    }

    //role 확인 메서드
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // 토큰의 만료 여부 확인
    public Boolean isExpired(String token) {
        try {
            Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
            return false; //유효한 토큰
        } catch (ExpiredJwtException e) {
            return true; //만료된 토큰
        } catch (JwtException e) {
            log.error("JWTException occurred: " + e.getMessage());
            throw new JWTException("Token is invalid" + e.getMessage());
        }
    }

    //토큰 payload에 정보 저장하여 토큰을 생성하는 메서드
    //AccessToken 생성
    public String
    createAccessToken(String memberId, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("memberId", memberId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs*1000))
                .signWith(secretKey)
                .compact();
    }
    // Refresh Token 생성
    public String createRefreshToken(Long expiredMs) {
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // Refresh Token 만료 시간 (더 길게 설정)
                .signWith(secretKey)
                .compact();
    }

    // Refresh Token으로 Access Token 재발급
    public String regenerateAccessToken(String refreshToken, String memberId, String role, Long accessTokenExpiredMs) {
        if (isExpired(refreshToken)) {
            throw new RuntimeException("Refresh Token is expired");
        }

        return createAccessToken(memberId, role, accessTokenExpiredMs);
    }

    public String regenerateRefreshToken(String refreshToken, String memberId, String role, Long refreshTokenExpiredMs) {
        if (isExpired(refreshToken)) {
            throw new RuntimeException("Refresh Token is expired");
        }
        return createRefreshToken(refreshTokenExpiredMs);
    }
}
