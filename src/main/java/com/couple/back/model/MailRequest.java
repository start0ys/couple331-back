package com.couple.back.model;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailRequest {
    private String email;
    private String verificationCode;

    public boolean isEmailEmpty() {
        return StringUtils.isEmpty(this.email);
    }

    public boolean validation() {
        return StringUtils.isAnyEmpty(this.email, this.verificationCode);
    }

}
