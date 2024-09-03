package com.couple.back.common;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.lang3.StringUtils;

public class SHA256 {
    public static String getSalt() {
        //1. Random, salt 생성
        SecureRandom sr = new SecureRandom();
        byte[] salt = new byte[20];

        //2. 난수 생성
        sr.nextBytes(salt);

        //3. byte To String (10진수 문자열로 변경)
        StringBuffer sb = new StringBuffer();
        for(byte b : salt) {
            sb.append(String.format("%02x", b));
        }

        return sb.toString();
    }

    // SHA-256 알고리즘 적용
    public static String getEncrypt(String pwd, String salt) {
        if(StringUtils.isAnyEmpty(pwd, salt)) return "";

        StringBuffer sb = new StringBuffer();
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update((pwd + salt).getBytes());
            byte[] pwdSalt = md.digest();

            for(byte b : pwdSalt) {
                sb.append(String.format("%02x", b));
            }


        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return sb.toString();
    }
}
