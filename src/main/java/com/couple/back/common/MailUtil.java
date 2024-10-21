package com.couple.back.common;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.couple.back.common.CommonConstants.ResultStatus;

@Service
public class MailUtil {
    
    @Value("${couple331.mail}")
    private String coupleMail;

    @Value("${couple331.mail.password}")
    private String coupleMailPassword;


    public ResultStatus sendHtmlMail(String email, String subject, String htmlString) throws Exception {
        if(StringUtils.isAnyEmpty(coupleMail, coupleMailPassword, email, htmlString)) 
            return ResultStatus.FAIL;
		
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
			return ResultStatus.ERROR;
		}
        return ResultStatus.SUCCESS;
    }
}
