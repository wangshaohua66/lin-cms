package io.github.talelin.latticy.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.talelin.latticy.bo.LoginCaptchaBO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("验证码工具类测试")
class CaptchaUtilTest {

    @Nested
    @DisplayName("getRandomString测试")
    class GetRandomStringTests {

        @Test
        @DisplayName("获取随机字符串 - 默认长度")
        void getRandomString_ShouldReturnString() {
            String result = CaptchaUtil.getRandomString(4);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(4);
        }

        @Test
        @DisplayName("获取随机字符串 - 自定义长度")
        void getRandomString_WithCustomLength_ShouldReturnCorrectLength() {
            String result = CaptchaUtil.getRandomString(6);

            assertThat(result).hasSize(6);
        }

        @Test
        @DisplayName("获取随机字符串 - 零长度")
        void getRandomString_WithZeroLength_ShouldReturnEmpty() {
            String result = CaptchaUtil.getRandomString(0);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("getRandomCodeBase64测试")
    class GetRandomCodeBase64Tests {

        @Test
        @DisplayName("获取验证码Base64 - 成功")
        void getRandomCodeBase64_ShouldReturnBase64() throws Exception {
            String result = CaptchaUtil.getRandomCodeBase64("ABCD");

            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("AES加解密测试")
    class AesTests {

        @Test
        @DisplayName("AES加密 - 成功")
        void aesEncode_ShouldReturnEncodedString() throws GeneralSecurityException {
            String secret = "1234567890123456";
            String iv = "abcdef0123456789";
            String content = "test content";

            String encoded = CaptchaUtil.aesEncode(secret, iv, content);

            assertThat(encoded).isNotNull();
            assertThat(encoded).isNotEmpty();
        }

        @Test
        @DisplayName("AES解密 - 成功")
        void aesDecode_ShouldReturnDecodedString() throws GeneralSecurityException {
            String secret = "1234567890123456";
            String iv = "abcdef0123456789";
            String content = "test content";

            String encoded = CaptchaUtil.aesEncode(secret, iv, content);
            String decoded = CaptchaUtil.aesDecode(secret, iv, encoded);

            assertThat(decoded).isEqualTo(content);
        }
    }

    @Nested
    @DisplayName("getTag测试")
    class GetTagTests {

        @Test
        @DisplayName("获取标签 - 成功")
        void getTag_ShouldReturnTag() throws JsonProcessingException, GeneralSecurityException {
            String captcha = "ABCD";
            String secret = "1234567890123456";
            String iv = "abcdef0123456789";

            String tag = CaptchaUtil.getTag(captcha, secret, iv);

            assertThat(tag).isNotNull();
            assertThat(tag).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("decodeTag测试")
    class DecodeTagTests {

        @Test
        @DisplayName("解码标签 - 成功")
        void decodeTag_ShouldReturnCaptchaBO() throws JsonProcessingException, GeneralSecurityException {
            String captcha = "ABCD";
            String secret = "1234567890123456";
            String iv = "abcdef0123456789";

            String tag = CaptchaUtil.getTag(captcha, secret, iv);
            LoginCaptchaBO result = CaptchaUtil.decodeTag(secret, iv, tag);

            assertThat(result).isNotNull();
            assertThat(result.getCaptcha()).isEqualTo(captcha);
            assertThat(result.getExpired()).isNotNull();
        }
    }
}
