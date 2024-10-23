package com.couple.back.common;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couple.back.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
    
    // JWT 생성 및 검증을 위한 키
	@Value("${jwt.security.key}")
    private String securityKey;


    // JWT 토큰 생성
    public String generateToken(User user) {
        // return generateToken(user, CommonUtil.convertToSeconds(1, TimeUnit.HOURS, true));
        return generateToken(user, CommonUtil.convertToSeconds(1, TimeUnit.MINUTES, true));
    }

    // Refresh Token 생성
    public String generateRefreshToken(User user) {
        return generateToken(user, CommonUtil.convertToSeconds(7, TimeUnit.DAYS, true));
    }

    // JWT 토큰 생성
    private String generateToken(User user, long expirationTime) {
        if(user == null || user.validation())
            return null;

        // 비밀번호 정보 초기화
        user.setPassword("");
        user.setSalt("");
        
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, securityKey) // 암호화 알고리즘 및 키 세팅
                .setSubject(user.getEmail()) // JWT 제목
                .setIssuedAt(new Date()) // 생성일
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료일  현재시간에서 만료시간 동안 유효
                // 유저 정보
                .addClaims(CommonUtil.convertToMap(user))
                .compact();
    }

    // JWT 토큰에서 사용자 정보 추출
    public String extractEmail(String token) {
        return extractAllClaims(token).getSubject();
    }

    // JWT 토큰에서 만료 정보 추출
    public Date extractExpiration(String token) {
        return extractAllClaims(token).getExpiration();
    }

      // JWT 토큰에서 사용자 정보 추출
      public User extractUser(String token) {
        Claims claims = extractAllClaims(token);
        return convertMapToUser(claims);
    }

    // JWT가 만료되었는지 확인
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // JWT 토큰 검증
    public Boolean validateToken(String token, String email) {
        final String extractedUsername = extractEmail(token);
        return (StringUtils.equals(extractedUsername, email) && !isTokenExpired(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(securityKey).parseClaimsJws(token).getBody();
    }

    private User convertMapToUser(Claims claims) {
        ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        return objectMapper.convertValue(claims, User.class);
    }
    
}
