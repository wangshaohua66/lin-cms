package io.github.talelin.latticy.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * EncryptUtil 单元测试类
 */
@DisplayName("加密工具类测试")
class EncryptUtilTest {

    @Nested
    @DisplayName("加密测试")
    class EncryptTests {

        @Test
        @DisplayName("加密 - 正常加密")
        void encrypt_ShouldReturnEncryptedString() {
            String password = "password123";

            String encrypted = EncryptUtil.encrypt(password);

            assertThat(encrypted).isNotNull();
            assertThat(encrypted).isNotEmpty();
            assertThat(encrypted).isNotEqualTo(password);
        }

        @Test
        @DisplayName("加密 - 空字符串")
        void encrypt_WithEmptyString_ShouldReturnEncryptedString() {
            String encrypted = EncryptUtil.encrypt("");

            assertThat(encrypted).isNotNull();
            assertThat(encrypted).isNotEmpty();
        }

        @Test
        @DisplayName("加密 - null值应抛出异常")
        void encrypt_WithNull_ShouldThrowException() {
            org.junit.jupiter.api.Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> EncryptUtil.encrypt(null)
            );
        }

        @Test
        @DisplayName("加密 - 相同密码加密结果不同")
        void encrypt_WithSamePassword_ShouldReturnDifferentResults() {
            String password = "password123";

            String encrypted1 = EncryptUtil.encrypt(password);
            String encrypted2 = EncryptUtil.encrypt(password);

            assertThat(encrypted1).isNotEqualTo(encrypted2);
        }

        @Test
        @DisplayName("加密 - 长密码")
        void encrypt_WithLongPassword_ShouldEncrypt() {
            String longPassword = "a".repeat(100);

            String encrypted = EncryptUtil.encrypt(longPassword);

            assertThat(encrypted).isNotNull();
        }

        @Test
        @DisplayName("加密 - 包含特殊字符")
        void encrypt_WithSpecialCharacters_ShouldEncrypt() {
            String specialPassword = "p@ssw0rd!#$%^&*()";

            String encrypted = EncryptUtil.encrypt(specialPassword);

            assertThat(encrypted).isNotNull();
        }

        @Test
        @DisplayName("加密 - 中文密码")
        void encrypt_WithChineseCharacters_ShouldEncrypt() {
            String chinesePassword = "密码测试123";

            String encrypted = EncryptUtil.encrypt(chinesePassword);

            assertThat(encrypted).isNotNull();
        }
    }

    @Nested
    @DisplayName("验证测试")
    class VerifyTests {

        @Test
        @DisplayName("验证 - 正确密码")
        void verify_WithCorrectPassword_ShouldReturnTrue() {
            String password = "password123";
            String encrypted = EncryptUtil.encrypt(password);

            boolean result = EncryptUtil.verify(encrypted, password);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("验证 - 错误密码")
        void verify_WithWrongPassword_ShouldReturnFalse() {
            String password = "password123";
            String encrypted = EncryptUtil.encrypt(password);

            boolean result = EncryptUtil.verify(encrypted, "wrongpassword");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("验证 - 空密码")
        void verify_WithEmptyPassword_ShouldReturnFalse() {
            String encrypted = EncryptUtil.encrypt("password");

            boolean result = EncryptUtil.verify(encrypted, "");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("验证 - null密码")
        void verify_WithNullPassword_ShouldReturnFalse() {
            String encrypted = EncryptUtil.encrypt("password");

            boolean result = EncryptUtil.verify(encrypted, null);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("验证 - null加密字符串")
        void verify_WithNullEncrypted_ShouldReturnFalse() {
            boolean result = EncryptUtil.verify(null, "password");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("验证 - 空加密字符串")
        void verify_WithEmptyEncrypted_ShouldReturnFalse() {
            boolean result = EncryptUtil.verify("", "password");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("验证 - 无效格式加密字符串")
        void verify_WithInvalidEncryptedFormat_ShouldReturnFalse() {
            boolean result = EncryptUtil.verify("invalid_format", "password");

            assertThat(result).isFalse();
        }
    }
}
