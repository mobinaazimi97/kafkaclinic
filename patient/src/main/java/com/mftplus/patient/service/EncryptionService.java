package com.mftplus.patient.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

//@Service
//@Slf4j
public class EncryptionService {
//    @Value("${encryption.key}")
//    private String encryptionKey;
//
//    public String encrypt(String data) throws Exception {
//        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//        return Base64.getEncoder().encodeToString(encrypted);
//    }
//
//    public String decrypt(String encryptedData) throws Exception {
//        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
//        return new String(decrypted, StandardCharsets.UTF_8);
//    }
//
//    public EncryptionService() {
//        log.info("START EncryptionService constructor:");
//    }
//
//    public String encrypt(String data) throws Exception {
//        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] encrypted = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
//        return Base64.getEncoder().encodeToString(encrypted);
//    }
//
//    public String decrypt(String encryptedData) throws Exception {
//        SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(StandardCharsets.UTF_8), "AES");
//        Cipher cipher = Cipher.getInstance("AES");
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
//        return new String(decrypted, StandardCharsets.UTF_8);
//    }
}