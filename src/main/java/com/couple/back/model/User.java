package com.couple.back.model;


import org.apache.commons.lang3.StringUtils;

import com.couple.back.common.Auditable;

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

    public enum GenderType {Man, Woman};

    public boolean validation(boolean isCreate) {
        return StringUtils.isAnyEmpty(this.email, this.password, this.salt, this.gender) || (isCreate && StringUtils.isEmpty(this.name));
    }

    public GenderType getGender() {
        if(StringUtils.isEmpty(this.gender)) 
            return null;

        switch (this.gender) {
            case "01":
                return GenderType.Man;
            case "02":
                return GenderType.Woman;
            default:
                return null;
        }
    }

}

