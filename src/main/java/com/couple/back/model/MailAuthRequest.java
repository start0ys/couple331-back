package com.couple.back.model;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MailAuthRequest {
    private String email;
    private String authCode;

    public boolean isEmailEmpty() {
        return StringUtils.isEmpty(this.email);
    }

    public boolean validation() {
        return StringUtils.isAnyEmpty(this.email, this.authCode);
    }

}
