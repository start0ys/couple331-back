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
    private String nickname;
    private String status;
    private String salt;
    private String gender;
    private String userRole;
    private Long coupleId;
}

