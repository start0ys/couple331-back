package com.couple.back.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JwtTokenRequest {
    private String accessToken;
    private String refreshToken;
}
