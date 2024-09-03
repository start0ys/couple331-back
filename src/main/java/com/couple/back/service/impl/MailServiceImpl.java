package com.couple.back.service.impl;

import java.security.SecureRandom;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couple.back.common.CommonConstants.ResultStatus;
import com.couple.back.model.MailRequest;
import com.couple.back.mybatis.MailMapper;
import com.couple.back.mybatis.UserMapper;
import com.couple.back.service.MailService;


@Service
public class MailServiceImpl implements MailService {
    
    @Value("${couple331.mail}")
    private String coupleMail;

    @Value("${couple331.mail.password}")
    private String coupleMailPassword;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MailMapper mailMapper;

    private static final String CHARACTER_TABLE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public ResultStatus sendHtmlMail(String email, String subject, String htmlString) throws Exception {
        if(StringUtils.isAnyEmpty(coupleMail, coupleMailPassword, email, htmlString)) 
            return ResultStatus.Fail;
		
		HtmlEmail mail = new HtmlEmail();

		mail.setCharset("euc-kr"); // 한글 인코딩
		mail.setHostName("smtp.gmail.com"); //SMTP서버 설정
		mail.setSmtpPort(465); //SMPT서버 포트번호
		
		mail.setAuthentication(coupleMail, coupleMailPassword);
		mail.setSSLOnConnect(true);
		mail.setStartTLSEnabled(true);
		
		try {
			mail.addTo(email, "USER"); // 수신자 추가
			mail.setFrom(coupleMail, "Couple331"); // 발신자 추가
			mail.setSubject(subject); // 메일 제목
			mail.setHtmlMsg(htmlString); // 메일 본문
			
			mail.send(); // 메일 보내기
		} catch (Exception e) {
			return ResultStatus.Error;
		}
        return ResultStatus.Success;
    }
    
    
    public String sendVerificationCode(MailRequest mailRequest) throws Exception {
        if(mailRequest.isEmailEmpty()) return null;
        String email = mailRequest.getEmail();
        
        int mailCheck = userMapper.selectCountByEmail(email);
        if(mailCheck > 0) {
            return "03";
        } else {
            String verificationCode =  getVerificationCode(); // front 쪽으로 가져가서 사용자가 입력한 임시번호값과 동일한지 체크 해야함
            ResultStatus result = sendHtmlMail(email, "Couple331 회원가입 인증", getVerificationCodeHtmlStr(verificationCode));
            if(result == ResultStatus.Success) {
                mailRequest.setVerificationCode(verificationCode);
                mailMapper.insertData(mailRequest);
                scheduleVerificationCodeCleanup(mailRequest);
                return "01";
            } else {
                return "02";
            }
        }

    }

    private String getVerificationCodeHtmlStr(String verificationCode) {
        StringBuffer sb = new StringBuffer();
        sb.append("<div>");
        sb.append("임시 번호 : " + verificationCode);
        sb.append("</div>");
        return sb.toString();
    }


    
    private String getVerificationCode() {
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

    private void scheduleVerificationCodeCleanup(MailRequest mailRequest) {
        long time = 3 * 60 * 1000; // 3분
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    mailMapper.deleteData(mailRequest);
                } catch (Exception e) {}
            } 
        };

        timer.schedule(task, time);
    }

    public ResultStatus verifyCode(MailRequest mailRequest) throws Exception {
        if(mailRequest.validation()) return null;

        int verificationCodeCheck = mailMapper.selectCountByVerificationCode(mailRequest);
       
        return verificationCodeCheck > 0 ? ResultStatus.Success : ResultStatus.Fail;
    }
}
