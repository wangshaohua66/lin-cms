package io.github.talelin.latticy.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * 加密工具类
 * 使用 Spring Security 的 BCrypt 替代 jhash
 */
public class EncryptUtil {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 加密密码
     *
     * @param password 明文密码
     * @return 加密后的密码
     */
    public static String encrypt(String password) {
        return encoder.encode(password);
    }

    /**
     * 验证密码
     *
     * @param encryptedPassword 加密后的密码
     * @param rawPassword       明文密码
     * @return 是否匹配
     */
    public static boolean verify(String encryptedPassword, String rawPassword) {
        if (encryptedPassword == null || rawPassword == null) {
            return false;
        }
        return encoder.matches(rawPassword, encryptedPassword);
    }
}
