package com.couple.back.service.impl;

import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.couple.back.common.ApiResponse;
import com.couple.back.common.ApiResponseUtil;
import com.couple.back.common.CommonConstants;
import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.common.MailUtil;
import com.couple.back.common.RedisUtil;
import com.couple.back.common.SHA256;
import com.couple.back.model.MailAuthRequest;
import com.couple.back.model.User;
import com.couple.back.model.MailAuthRequest.AuthCodeStatus;
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

    public AuthCodeStatus sendCode(MailAuthRequest mailAuthRequest) throws Exception {
        if(mailAuthRequest.isEmailEmpty()) 
        throw new IllegalArgumentException("Email Not Find");
        String email = mailAuthRequest.getEmail();
        
        int mailCheck = userMapper.selectCountByEmail(email);
        if(mailCheck > 0) {
            return AuthCodeStatus.ALREADY_EXISTS;
        } else {
            String authCode =  getAuthCode();
            ResultStatus result = mailUtil.sendHtmlMail(email, "Couple331 회원가입 인증", getAuthCodeHtmlStr(authCode));
            if(result == ResultStatus.SUCCESS) {
                // TODO Redis에 저장
                return AuthCodeStatus.SUCCESS;
            } else {
                return AuthCodeStatus.FAIL;
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

    public ResultStatus verifyCode(MailAuthRequest mailAuthRequest) throws Exception {
        if(mailAuthRequest.validation()) return null;

        int authCodeCheck = 1; // TODO Redis값 과 체크
       
        return authCodeCheck > 0 ? ResultStatus.SUCCESS : ResultStatus.FAIL;
    }

    public ApiResponse<String> loginUser(User loginData) throws Exception {
        if(loginData == null) 
            return ApiResponseUtil.fail("로그인 데이터가 없습니다.");

        String email = loginData.getEmail();
        String password = loginData.getPassword();

        if(StringUtils.isAnyEmpty(email, password)) 
            return ApiResponseUtil.fail("이메일 또는 패스워드가 존재하지 않습니다.");

        User user = userMapper.selectDataByEmail(email);


        // String token = "";
        //int exprTime = 3600000;


        if(user == null) {
            return ApiResponseUtil.fail("존재하지 않는 아이디입니다.");
        } else {
            String salt = user.getSalt();
            String dbPw = user.getPassword();
            String encryptPw = SHA256.getEncrypt(password, salt);
            if(StringUtils.equals(dbPw, encryptPw)) {
                // token = tokenProvider.createToken(user);
                // jwt로 token할지 고민중
                return ApiResponseUtil.success(CommonConstants.SUCCESS_MESSAGE, "");
            } else {
                return ApiResponseUtil.fail("비밀번호를 확인해주세요.");
            }
        }
    }
}
