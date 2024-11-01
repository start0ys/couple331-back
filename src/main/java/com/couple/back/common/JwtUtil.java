package com.couple.back.common;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couple.back.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
    
    // JWT 생성 및 검증을 위한 키
	@Value("${jwt.security.key}")
    private String securityKey;


    // JWT 토큰 생성
    public String generateToken(User user, String authCode) {
        // return generateToken(user, CommonUtil.convertToSeconds(1, TimeUnit.HOURS, true));
        return generateToken(user, authCode, CommonUtil.convertToSeconds(1, TimeUnit.MINUTES, true));
    }

    // Refresh Token 생성
    public String generateRefreshToken(User user, String authCode) {
        // return generateToken(user, CommonUtil.convertToSeconds(1, TimeUnit.DAYS, true));
        return generateToken(user, authCode, CommonUtil.convertToSeconds(2, TimeUnit.MINUTES, true));
    }

    // JWT 토큰 생성
    private String generateToken(User user, String authCode, long expirationTime) {
        if(user == null) 
            return null;

        // 비밀번호 정보 초기화
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("userId", user.getUserId());
        userMap.put("email", user.getEmail());
        userMap.put("name", user.getName());
        userMap.put("gender", user.getGender());
        userMap.put("nickname", user.getNickname());
        userMap.put("userRole", user.getUserRole());
        userMap.put("authCode", authCode);
        
        
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, securityKey) // 암호화 알고리즘 및 키 세팅
                .setSubject(user.getEmail()) // JWT 제목
                .setIssuedAt(new Date()) // 생성일
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 만료일  현재시간에서 만료시간 동안 유효
                // 유저 정보
                .addClaims(userMap)
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

    public String extractAuthCode(String token) {
        Claims claims = extractAllClaims(token);
        return claims != null && claims.get("authCode") !=null ? claims.get("authCode").toString() : "";
    }

    // JWT가 만료되었는지 확인
    public Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        } catch (Exception e) {
            return true;
        }
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
        if(claims == null)
            return null;

        User user = new User();
        user.setUserId(claims.get("userId") != null ? Long.parseLong(claims.get("userId").toString()) : null);
        user.setEmail(claims.get("email") != null ? claims.get("email").toString() : "");
        user.setName(claims.get("name") != null ? claims.get("name").toString() : "");
        user.setGender(claims.get("gender") != null ? claims.get("gender").toString() : "");
        user.setNickname(claims.get("nickname") != null ? claims.get("nickname").toString() : "");
        user.setUserRole(claims.get("userRole") != null ? claims.get("userRole").toString() : "");
    
        return user;
    }
    
}
