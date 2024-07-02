package com.example.demo.util;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Component
public class EncryptionUtil {
    private static final String ALGORITHM = "AES"; // AES 알고리즘을 사용할 것을 명시

    /**
     * 데이터를 암호화하는 메서드
     * @param data 암호화할 데이터
     * @param key 암호화에 사용할 키 (Base64 인코딩된 문자열)
     * @return 암호화된 데이터 (Base64 인코딩된 문자열)
     * @throws Exception 암호화 과정에서 발생할 수 있는 예외
     */
    public static String encrypt(String data, String key) throws Exception {
        // Base64로 인코딩된 키를 디코딩하여 SecretKeySpec 객체 생성
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);
        
        // Cipher 인스턴스를 AES 알고리즘으로 초기화
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey); // 암호화 모드로 설정

        // 데이터를 암호화하고 Base64로 인코딩하여 반환
        return Base64.getUrlEncoder().encodeToString(cipher.doFinal(data.getBytes()));
    }

    /**
     * 데이터를 복호화하는 메서드
     * @param data 암호화된 데이터 (Base64 인코딩된 문자열)
     * @param key 복호화에 사용할 키 (Base64 인코딩된 문자열)
     * @return 복호화된 데이터 (원본 문자열)
     * @throws Exception 복호화 과정에서 발생할 수 있는 예외
     */
    public static String decrypt(String data, String key) throws Exception {
        // Base64로 인코딩된 키를 디코딩하여 SecretKeySpec 객체 생성
        SecretKeySpec secretKey = new SecretKeySpec(Base64.getDecoder().decode(key), ALGORITHM);

        // Cipher 인스턴스를 AES 알고리즘으로 초기화
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey); // 복호화 모드로 설정

        // 데이터를 복호화하고 문자열로 변환하여 반환
        return new String(cipher.doFinal(Base64.getUrlDecoder().decode(data)));
    }

    /**
     * AES 암호화에 사용할 키를 생성하는 메서드
     * @return 생성된 키 (Base64 인코딩된 문자열)
     * @throws Exception 키 생성 과정에서 발생할 수 있는 예외
     */
    public static String generateKey() throws Exception {
        // AES 알고리즘을 사용하여 KeyGenerator 인스턴스를 생성
        KeyGenerator keyGen = KeyGenerator.getInstance(ALGORITHM);
        
        // 키 크기를 128비트로 초기화
        keyGen.init(128);

        // 키 생성
        SecretKey secretKey = keyGen.generateKey();

        // 생성된 키를 Base64로 인코딩하여 문자열로 변환하여 반환
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }
}
