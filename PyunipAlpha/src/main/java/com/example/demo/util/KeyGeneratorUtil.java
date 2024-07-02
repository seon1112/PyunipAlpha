package com.example.demo.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGeneratorUtil {

    public static void main(String[] args) throws Exception {
    	// AES 알고리즘을 사용하여 KeyGenerator 인스턴스를 생성
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 128, 192, 256비트 중 선택 가능
        SecretKey secretKey = keyGen.generateKey();

        // 생성된 키를 Base64로 인코딩하여 문자열로 변환
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated AES Key: " + encodedKey);
    }
}