package com.example.boardserver.utils;

import java.security.MessageDigest;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class SHA256Util {

    public static final String ENCRYPTION_KEY = "SHA-256";

    public static String encryptionKey(String str){
        String SHA = null;

        MessageDigest sh; //message handle

       //암호화
        try {
            sh = MessageDigest.getInstance(ENCRYPTION_KEY);
            sh.update(str.getBytes());
            byte[] byteData = sh.digest();
            StringBuffer sb = new StringBuffer();
            for(byte byteDatum : byteData){
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Exception e) {
            log.error("SHA256 Error [{}]",e.getMessage());
            SHA = null;
        }
        return SHA;
    }
}
