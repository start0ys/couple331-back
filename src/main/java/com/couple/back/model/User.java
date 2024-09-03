package com.couple.back.model;


import org.apache.commons.lang3.StringUtils;

import com.couple.back.common.Auditable;
import com.couple.back.common.CommonConstants.GenderType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User extends Auditable {
    private Long userId;
    private String email;
    private String password;
    private String userDesc;
    private String name;
    private String status;
    private String salt;
    private String gender;
    private Long coupleId;

    public boolean validation(boolean isCreate) {
        return StringUtils.isAnyEmpty(this.email, this.password, this.salt, this.gender) || (isCreate && StringUtils.isEmpty(this.name));
    }

    public GenderType getGender() {
        return StringUtils.equals(this.gender, "01") ? GenderType.Man : GenderType.Woman;
    }

}

