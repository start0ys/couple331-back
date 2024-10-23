package com.couple.back.service.impl;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonUtil;
import com.couple.back.common.JwtUtil;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.common.MailUtil;
import com.couple.back.common.RedisUtil;
import com.couple.back.common.SHA256;
import com.couple.back.model.JwtTokenRequest;
import com.couple.back.model.MailAuthRequest;
import com.couple.back.model.User;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.AuthService;

@Service
public class AuthServiceImpl implements AuthService{
    
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private JwtUtil jwtUtil;
    
    private final int AUTH_CODE_EXPIRATION_TIME = 180;
    
    private final String VERIFIED_EMAIL_KEY = "verifiedEmail:";
    private final String JWT_EMAIL_KEY = "jwtRefreshTokenEmail:";

    public ApiResponse<String> sendCode(MailAuthRequest mailAuthRequest) throws Exception {
        if(mailAuthRequest == null || mailAuthRequest.isEmailEmpty()) 
            throw new IllegalArgumentException("Parameter is Empty");
        String email = mailAuthRequest.getEmail();
        
        int mailCheck = userMapper.selectCountByEmail(email);
        if(mailCheck > 0) {
            return ApiResponseUtil.fail("이미 존재하는 이메일 입니다.");
        } else {
            String authCode =  getAuthCode();
            ResultStatus result = mailUtil.sendHtmlMail(email, "Couple331 회원가입 인증", getAuthCodeHtmlStr(authCode));
            if(result == ResultStatus.SUCCESS) {
                redisUtil.setDataExpire(email, authCode, AUTH_CODE_EXPIRATION_TIME);
                return ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, "");
            } else {
                return ApiResponseUtil.fail("인증코드 메일 발송이 실패하였습니다.");
            }
        }

    }

    private String getAuthCodeHtmlStr(String authCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("<div>");
        sb.append("임시 번호 : " + authCode);
        sb.append("</div>");
        return sb.toString();
    }


    
    private String getAuthCode() {
        int numberSize = 16;
        int groupSize = 4;
        
        StringBuilder buf = new StringBuilder(numberSize + (numberSize / groupSize) - 1);
        SecureRandom random = new SecureRandom();
        
        for (int i = 0; i < numberSize; i++) {
            if (i > 0 && i % groupSize == 0) {
                buf.append('-'); // 그룹 사이에 하이픈 추가
            }
            int index = random.nextInt(CommonConstants.CHARACTER_TABLE.length());
            buf.append(CommonConstants.CHARACTER_TABLE.charAt(index));
        }
        return buf.toString();
    }

    public ApiResponse<String> verifyCode(MailAuthRequest mailAuthRequest) throws Exception {
        if(mailAuthRequest == null || mailAuthRequest.validation()) 
            throw new IllegalArgumentException("Parameter is Empty");

        String email = mailAuthRequest.getEmail();
        String authCode = redisUtil.getData(email);
       
        if(StringUtils.isEmpty(authCode)) {
            return ApiResponseUtil.fail("인증코드가 만료되었습니다.");
        } else if(StringUtils.equals(authCode, mailAuthRequest.getAuthCode())) {
            redisUtil.deleteData(email);
            redisUtil.setDataExpire(VERIFIED_EMAIL_KEY + email, "Y", 600);
            return ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, "");
        } else {
            return ApiResponseUtil.fail("인증코드가 일치하지 않습니다.");
        }
    }

    public ApiResponse<JwtTokenRequest> loginUser(User loginData) throws Exception {
        if(loginData == null) 
            throw new IllegalArgumentException("Parameter is Empty");

        String email = loginData.getEmail();
        String password = loginData.getPassword();

        if(StringUtils.isAnyEmpty(email, password)) 
            return ApiResponseUtil.fail("이메일 또는 패스워드가 존재하지 않습니다.");

        User user = userMapper.selectDataByEmail(email);

        if(user == null) {
            return ApiResponseUtil.fail("존재하지 않는 아이디입니다.");
        } else {
            String salt = user.getSalt();
            String dbPw = user.getPassword();
            String encryptPw = SHA256.getEncrypt(password, salt);
            if(StringUtils.equals(dbPw, encryptPw)) {
                String accessToken = jwtUtil.generateToken(user);
                String refreshToken = jwtUtil.generateRefreshToken(user);
                redisUtil.setDataExpire(JWT_EMAIL_KEY + email, refreshToken, CommonUtil.convertToSeconds(7, TimeUnit.DAYS, false));
                return ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, new JwtTokenRequest(accessToken, refreshToken));
            } else {
                return ApiResponseUtil.fail("비밀번호를 확인해주세요.");
            }
        }
    }

    public boolean verifiedEmailCheck(String email) throws Exception {
        String verifiedEmail = redisUtil.getData(VERIFIED_EMAIL_KEY + email);
        return StringUtils.equals(verifiedEmail, "Y");
    }

    public ApiResponse<JwtTokenRequest> refreshAccessToken(JwtTokenRequest jwtTokenRequest) throws Exception {
        if (jwtTokenRequest == null)
            throw new IllegalArgumentException("Parameter is Empty");

        String refreshToken = jwtTokenRequest.getRefreshToken();
        String email = jwtUtil.extractEmail(refreshToken);

        // 리프레시 토큰과 이메일 검증
        if (StringUtils.isAnyEmpty(refreshToken, email)) {
            return ApiResponseUtil.fail("Refresh token or email is missing.");
        }

        // Redis에서 리프레시 토큰 유효성 검사
        String storedRefreshToken = redisUtil.getData(JWT_EMAIL_KEY + email);
        if (!StringUtils.equals(storedRefreshToken, refreshToken)) {
            return ApiResponseUtil.fail("Invalid refresh token.");
        }

        // 사용자 정보 조회
        User user = userMapper.selectDataByEmail(email);
        if (user == null) {
            return ApiResponseUtil.fail("User not found.");
        }

        // 새 액세스 토큰 생성
        String newAccessToken = jwtUtil.generateToken(user);
        if (StringUtils.isEmpty(newAccessToken)) {
            return ApiResponseUtil.fail(CommonConstants.ERROR_MESSAGE);
        }

        // 새 액세스 토큰 설정
        jwtTokenRequest.setAccessToken(newAccessToken);
        return ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, jwtTokenRequest);
    }

    public ResultStatus validateToken(String token) throws Exception {
        return StringUtils.isNotEmpty(token) && !jwtUtil.isTokenExpired(token) ? ResultStatus.SUCCESS : ResultStatus.FAIL;
    }

    public void logout(JwtTokenRequest jwtTokenRequest) throws Exception {
        if (jwtTokenRequest == null)
            throw new IllegalArgumentException("Parameter is Empty");

        String email = jwtUtil.extractEmail(jwtTokenRequest.getAccessToken());

        redisUtil.deleteData(JWT_EMAIL_KEY + email);
    }
}
