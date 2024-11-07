package com.couple.back.service.impl;

import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonConstants.DateType;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.common.CommonUtil;
import com.couple.back.common.JwtUtil;
import com.couple.back.common.MailUtil;
import com.couple.back.common.RedisUtil;
import com.couple.back.common.SHA256;
import com.couple.back.dto.JwtTokenResponse;
import com.couple.back.dto.LoginRequest;
import com.couple.back.dto.MailAuthRequest;
import com.couple.back.exception.DuplicateLoginException;
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
    private final String CHARACTER_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final String VERIFIED_EMAIL_KEY = "verifiedEmail:";
    private final String JWT_EMAIL_KEY = "jwtRefreshTokenEmail:";

    public ApiResponse<String> sendCode(MailAuthRequest mailAuthRequest) throws Exception {
        if(mailAuthRequest == null || StringUtils.isEmpty(mailAuthRequest.getEmail())) 
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
            int index = random.nextInt(CHARACTER_TABLE.length());
            buf.append(CHARACTER_TABLE.charAt(index));
        }
        return buf.toString();
    }

    public ApiResponse<String> verifyCode(MailAuthRequest mailAuthRequest) throws Exception {
        if(mailAuthRequest == null || StringUtils.isAnyEmpty(mailAuthRequest.getEmail(), mailAuthRequest.getAuthCode())) 
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

    public ApiResponse<JwtTokenResponse> loginUser(LoginRequest loginData) throws Exception {
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
                if(redisUtil.existData(JWT_EMAIL_KEY + email)) {
                    if(StringUtils.isEmpty(loginData.getDuplicateLoginYn())) {
                        throw new DuplicateLoginException("이미 로그인 되어있는 ID 입니다.");
                    } else {
                        redisUtil.deleteData(JWT_EMAIL_KEY + email);
                    }
                }
                String authCode = getAuthCode();
                String accessToken = jwtUtil.generateToken(user, authCode);
                String refreshToken = jwtUtil.generateRefreshToken(user, authCode);
                redisUtil.setDataExpire(JWT_EMAIL_KEY + email, refreshToken, CommonUtil.convertToSeconds(1, DateType.DAYS, false));
                return ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, new JwtTokenResponse(accessToken, refreshToken));
            } else {
                return ApiResponseUtil.fail("비밀번호를 확인해주세요.");
            }
        }
    }

    public boolean verifiedEmailCheck(String email) throws Exception {
        String verifiedEmail = redisUtil.getData(VERIFIED_EMAIL_KEY + email);
        return StringUtils.equals(verifiedEmail, "Y");
    }

    public ApiResponse<JwtTokenResponse> refreshAccessToken(String authorizationHeader) throws Exception {
        String refreshToken = CommonUtil.getAccessToken(authorizationHeader);
        if (StringUtils.isEmpty(refreshToken))
            throw new IllegalArgumentException("Parameter is Empty");

        if(jwtUtil.isTokenExpired(refreshToken)) {
            return ApiResponseUtil.fail("Refresh token이 만료되었습니다.");
        }

        String email = jwtUtil.extractEmail(refreshToken);

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
        String authCode = getAuthCode();
        String newAccessToken = jwtUtil.generateToken(user, authCode);
        if (StringUtils.isEmpty(newAccessToken)) {
            return ApiResponseUtil.fail(CommonConstants.ERROR_MESSAGE);
        }

        // 새 액세스 토큰 설정
        return ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, new JwtTokenResponse(newAccessToken, refreshToken));
    }

    public ResultStatus validateToken(String authorizationHeader) throws Exception {
        String accessToken = CommonUtil.getAccessToken(authorizationHeader);
        if (StringUtils.isEmpty(accessToken))
            throw new IllegalArgumentException("Parameter is Empty");

        if(jwtUtil.isTokenExpired(accessToken)) {
            return ResultStatus.FAIL;
        } 

        String email = jwtUtil.extractEmail(accessToken);
        String storedRefreshToken = redisUtil.getData(JWT_EMAIL_KEY + email);

        if(StringUtils.isEmpty(storedRefreshToken) || jwtUtil.isTokenExpired(storedRefreshToken)) {
            return ResultStatus.FAIL;
        }

        String authCode = jwtUtil.extractAuthCode(accessToken);
        String storedAuthCode = jwtUtil.extractAuthCode(storedRefreshToken);

        return StringUtils.equals(authCode, storedAuthCode) ? ResultStatus.SUCCESS : ResultStatus.FAIL;
    }

    public void logout(String authorizationHeader) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.clearContext();
        }

        String accessToken = CommonUtil.getAccessToken(authorizationHeader);
        if (StringUtils.isEmpty(accessToken) || jwtUtil.isTokenExpired(accessToken))
            return;

        String email = jwtUtil.extractEmail(accessToken);
        redisUtil.deleteData(JWT_EMAIL_KEY + email);
    }
}
