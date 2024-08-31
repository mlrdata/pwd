package com.pwd.password.service;

import com.pwd.password.exception.BusinessException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Scanner;

/**
 * AES加密解密
 *
 * @author cck
 * @date 2024/8/29 17:50
 */
@RestController
@RequestMapping("/pwd")
public class PasswordService {

    /**
     * 加密
     *
     * @param data     加密数据和秘钥
     */
    @PostMapping("/encryption")
    public void encryption(@RequestBody Map<String, String> data, HttpServletResponse response) {
        String plainText = data.get("plainText");
        String userKey = data.get("userKey");
        if (StringUtils.isEmpty(plainText) || StringUtils.isEmpty(userKey)) {
            throw new BusinessException("加密内容或秘钥不能为空");
        }

        String filePath = "pwd";  // 文件名

        byte[] secretKey = hashUserKey(userKey, 256);  // 使用SHA-256哈希处理用户输入的密钥
        String encryptedText = encryptString(plainText, secretKey);  // 使用处理后的密钥进行加密

        File file = new File(filePath);
        try {
            // 覆盖写入数据
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(encryptedText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 设置响应头信息
        response.setContentType("application/octet-stream");
        response.setContentLength((int) file.length());
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=pwd");

        try (FileInputStream fis = new FileInputStream(file); BufferedInputStream inputStream = new BufferedInputStream(fis); BufferedOutputStream outputStream = new BufferedOutputStream(response.getOutputStream())) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 解密
     *
     * @param file 加密后文件
     * @return 解密后内容
     */
    @PostMapping("/decrypt")
    public String decrypt(MultipartFile file, String userKey) {
        String decryptedText;
        try (Scanner scanner = new Scanner(file.getInputStream(), "UTF-8")) {
            String encryptedText;
            encryptedText = scanner.hasNext() ? scanner.next() : "";

            byte[] secretKey = hashUserKey(userKey, 256);  // 使用SHA-256哈希处理用户输入的密钥
            decryptedText = decryptString(encryptedText, secretKey);


        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException("解密失败，请检查文件和秘钥");
        }
        return decryptedText;
    }

    public static byte[] hashUserKey(String userKey, int keySize) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashedKey = digest.digest(userKey.getBytes(StandardCharsets.UTF_8));

            if (keySize == 128) {
                return Arrays.copyOf(hashedKey, 16);
            } else if (keySize == 192) {
                return Arrays.copyOf(hashedKey, 24);
            } else if (keySize == 256) {
                return hashedKey;  // SHA-256 的输出正好是 32 字节
            } else {
                throw new IllegalArgumentException("Unsupported key size: " + keySize);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error hashing user key.", e);
        }
    }

    public static String encryptString(String plainText, byte[] secretKey) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);

            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error while encrypting.", e);
        }
    }

    public static String decryptString(String encryptedText, byte[] secretKey) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);

            byte[] decodedValue = Base64.getDecoder().decode(encryptedText);
            byte[] decrypted = cipher.doFinal(decodedValue);
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error while decrypting.", e);
        }
    }


}
