package io.github.talelin.latticy.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EncryptUtil 单元测试
 * 测试点：密码加密和验证功能
 */
@DisplayName("加密工具类测试")
class EncryptUtilTest {

    private static final String PASSWORD = "password123";
    private static final String ANOTHER_PASSWORD = "anotherpassword456";

    @Test
    @DisplayName("加密密码 - 正常情况")
    void encrypt_WithValidPassword_ShouldReturnEncryptedString() {
        // 输入参数：明文密码
        // 预期结果：返回加密后的密码字符串
        // 测试点：验证密码加密功能
        String encrypted = EncryptUtil.encrypt(PASSWORD);

        assertThat(encrypted).isNotNull();
        assertThat(encrypted).isNotEqualTo(PASSWORD);
        assertThat(encrypted).startsWith("$2a$"); // BCrypt加密格式
    }

    @Test
    @DisplayName("加密密码 - 空字符串")
    void encrypt_WithEmptyString_ShouldReturnEncryptedString() {
        // 输入参数：空字符串
        // 预期结果：返回加密后的字符串
        // 测试点：验证空字符串加密
        String encrypted = EncryptUtil.encrypt("");

        assertThat(encrypted).isNotNull();
        assertThat(encrypted).isNotEmpty();
    }

    @Test
    @DisplayName("加密密码 - null值")
    void encrypt_WithNull_ShouldThrowException() {
        // 输入参数：null
        // 预期结果：抛出异常（可能是NullPointerException或IllegalArgumentException）
        // 测试点：验证null值处理
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            EncryptUtil.encrypt(null);
        });
    }

    @Test
    @DisplayName("验证密码 - 正确密码")
    void verify_WithCorrectPassword_ShouldReturnTrue() {
        // 输入参数：加密后的密码和正确的明文密码
        // 预期结果：返回true
        // 测试点：验证密码验证功能
        String encrypted = EncryptUtil.encrypt(PASSWORD);

        boolean result = EncryptUtil.verify(encrypted, PASSWORD);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("验证密码 - 错误密码")
    void verify_WithWrongPassword_ShouldReturnFalse() {
        // 输入参数：加密后的密码和错误的明文密码
        // 预期结果：返回false
        // 测试点：验证错误密码处理
        String encrypted = EncryptUtil.encrypt(PASSWORD);

        boolean result = EncryptUtil.verify(encrypted, ANOTHER_PASSWORD);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("验证密码 - null加密密码")
    void verify_WithNullEncryptedPassword_ShouldReturnFalse() {
        // 输入参数：null加密密码和明文密码
        // 预期结果：返回false
        // 测试点：验证null加密密码处理
        boolean result = EncryptUtil.verify(null, PASSWORD);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("验证密码 - null明文密码")
    void verify_WithNullRawPassword_ShouldReturnFalse() {
        // 输入参数：加密后的密码和null明文密码
        // 预期结果：返回false
        // 测试点：验证null明文密码处理
        String encrypted = EncryptUtil.encrypt(PASSWORD);

        boolean result = EncryptUtil.verify(encrypted, null);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("验证密码 - 两个null参数")
    void verify_WithBothNull_ShouldReturnFalse() {
        // 输入参数：两个null参数
        // 预期结果：返回false
        // 测试点：验证双null参数处理
        boolean result = EncryptUtil.verify(null, null);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("加密和验证 - 完整流程")
    void encryptAndVerify_FullFlow_ShouldWork() {
        // 输入参数：明文密码
        // 预期结果：加密后能够正确验证
        // 测试点：验证完整加密验证流程
        String originalPassword = "mySecretPassword123";
        String encrypted = EncryptUtil.encrypt(originalPassword);

        assertThat(EncryptUtil.verify(encrypted, originalPassword)).isTrue();
        assertThat(EncryptUtil.verify(encrypted, "wrongpassword")).isFalse();
    }

    @Test
    @DisplayName("加密 - 不同次加密结果不同")
    void encrypt_DifferentTimes_ShouldReturnDifferentResults() {
        // 输入参数：相同的明文密码
        // 预期结果：每次加密结果不同（因为使用了随机盐）
        // 测试点：验证BCrypt随机盐机制
        String encrypted1 = EncryptUtil.encrypt(PASSWORD);
        String encrypted2 = EncryptUtil.encrypt(PASSWORD);

        assertThat(encrypted1).isNotEqualTo(encrypted2);
        // 但两者都能验证通过
        assertThat(EncryptUtil.verify(encrypted1, PASSWORD)).isTrue();
        assertThat(EncryptUtil.verify(encrypted2, PASSWORD)).isTrue();
    }

    @Test
    @DisplayName("加密 - 长密码")
    void encrypt_WithLongPassword_ShouldWork() {
        // 输入参数：长密码（72字符以内）
        // 预期结果：正常加密
        // 测试点：验证长密码处理
        String longPassword = "thisIsAVeryLongPasswordThatShouldStillWorkWithBCryptEncryption123";

        String encrypted = EncryptUtil.encrypt(longPassword);

        assertThat(encrypted).isNotNull();
        assertThat(EncryptUtil.verify(encrypted, longPassword)).isTrue();
    }

    @Test
    @DisplayName("加密 - 特殊字符密码")
    void encrypt_WithSpecialCharacters_ShouldWork() {
        // 输入参数：包含特殊字符的密码
        // 预期结果：正常加密和验证
        // 测试点：验证特殊字符处理
        String specialPassword = "p@ssw0rd!#$%^&*()";

        String encrypted = EncryptUtil.encrypt(specialPassword);

        assertThat(encrypted).isNotNull();
        assertThat(EncryptUtil.verify(encrypted, specialPassword)).isTrue();
    }
}
