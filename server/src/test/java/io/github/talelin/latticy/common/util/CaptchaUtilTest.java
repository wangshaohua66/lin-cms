package io.github.talelin.latticy.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.talelin.latticy.bo.LoginCaptchaBO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CaptchaUtil 单元测试
 * 测试点：验证码生成、加密解密等功能
 */
@DisplayName("验证码工具类测试")
class CaptchaUtilTest {

    private static final String SECRET = "12345678901234567890123456789012"; // 32字节
    private static final String IV = "1234567890123456"; // 16字节

    @Test
    @DisplayName("获取随机字符串 - 默认长度")
    void getRandomString_WithDefaultLength_ShouldReturnString() {
        // 输入参数：默认长度
        // 预期结果：返回指定长度的随机字符串
        // 测试点：验证随机字符串生成
        String result = CaptchaUtil.getRandomString(CaptchaUtil.RANDOM_STR_NUM);

        assertThat(result).isNotNull();
        assertThat(result).hasSize(CaptchaUtil.RANDOM_STR_NUM);
    }

    @Test
    @DisplayName("获取随机字符串 - 自定义长度")
    void getRandomString_WithCustomLength_ShouldReturnString() {
        // 输入参数：自定义长度6
        // 预期结果：返回6位随机字符串
        // 测试点：验证自定义长度处理
        String result = CaptchaUtil.getRandomString(6);

        assertThat(result).hasSize(6);
    }

    @Test
    @DisplayName("获取随机字符串 - 负数长度")
    void getRandomString_WithNegativeLength_ShouldReturnFullLength() {
        // 输入参数：负数长度
        // 预期结果：返回完整长度字符串
        // 测试点：验证负数长度处理
        String result = CaptchaUtil.getRandomString(-1);

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("获取随机字符串 - 零长度")
    void getRandomString_WithZeroLength_ShouldReturnFullLength() {
        // 输入参数：0长度
        // 预期结果：返回完整长度字符串
        // 测试点：验证零长度处理
        String result = CaptchaUtil.getRandomString(0);

        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("获取验证码Base64 - 正常情况")
    void getRandomCodeBase64_WithValidCode_ShouldReturnBase64() throws Exception {
        // 输入参数：验证码字符串
        // 预期结果：返回Base64编码的图片
        // 测试点：验证验证码图片生成
        String code = "ABCD";

        String result = CaptchaUtil.getRandomCodeBase64(code);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("获取Tag - 正常情况")
    void getTag_WithValidParams_ShouldReturnEncryptedTag() throws Exception {
        // 输入参数：验证码、密钥、偏移量
        // 预期结果：返回加密的tag
        // 测试点：验证tag生成
        String code = "ABCD";

        String result = CaptchaUtil.getTag(code, SECRET, IV);

        assertThat(result).isNotNull();
        assertThat(result).isNotEmpty();
    }

    @Test
    @DisplayName("解码Tag - 正常情况")
    void decodeTag_WithValidTag_ShouldReturnCaptchaBO() throws Exception {
        // 输入参数：有效的tag
        // 预期结果：返回解码后的CaptchaBO
        // 测试点：验证tag解码
        String code = "ABCD";
        String tag = CaptchaUtil.getTag(code, SECRET, IV);

        LoginCaptchaBO result = CaptchaUtil.decodeTag(SECRET, IV, tag);

        assertThat(result).isNotNull();
        assertThat(result.getCaptcha()).isEqualTo(code);
        assertThat(result.getExpired()).isNotNull();
    }

    @Test
    @DisplayName("AES加密解密 - 正常情况")
    void aesEncodeAndDecode_WithValidContent_ShouldWork() throws GeneralSecurityException {
        // 输入参数：明文内容
        // 预期结果：加密后能够正确解密
        // 测试点：验证AES加解密
        String content = "test content for encryption";

        String encrypted = CaptchaUtil.aesEncode(SECRET, IV, content);
        String decrypted = CaptchaUtil.aesDecode(SECRET, IV, encrypted);

        assertThat(encrypted).isNotEqualTo(content);
        assertThat(decrypted).isEqualTo(content);
    }

    @Test
    @DisplayName("AES加密 - 空字符串")
    void aesEncode_WithEmptyString_ShouldWork() throws GeneralSecurityException {
        // 输入参数：空字符串
        // 预期结果：正常加密解密
        // 测试点：验证空字符串处理
        String content = "";

        String encrypted = CaptchaUtil.aesEncode(SECRET, IV, content);
        String decrypted = CaptchaUtil.aesDecode(SECRET, IV, encrypted);

        assertThat(decrypted).isEqualTo(content);
    }

    @Test
    @DisplayName("AES加密 - 中文字符")
    void aesEncode_WithChineseCharacters_ShouldWork() throws GeneralSecurityException {
        // 输入参数：中文字符串
        // 预期结果：正常加密解密
        // 测试点：验证中文字符处理
        String content = "中文测试内容";

        String encrypted = CaptchaUtil.aesEncode(SECRET, IV, content);
        String decrypted = CaptchaUtil.aesDecode(SECRET, IV, encrypted);

        assertThat(decrypted).isEqualTo(content);
    }

    @Test
    @DisplayName("AES加密 - 特殊字符")
    void aesEncode_WithSpecialCharacters_ShouldWork() throws GeneralSecurityException {
        // 输入参数：特殊字符
        // 预期结果：正常加密解密
        // 测试点：验证特殊字符处理
        String content = "!@#$%^&*()_+-=[]{}|;':\",./<>?";

        String encrypted = CaptchaUtil.aesEncode(SECRET, IV, content);
        String decrypted = CaptchaUtil.aesDecode(SECRET, IV, encrypted);

        assertThat(decrypted).isEqualTo(content);
    }

    @Test
    @DisplayName("私有构造函数 - 工具类不可实例化")
    void privateConstructor_UtilityClass_ShouldNotBeInstantiated() {
        // 测试点：验证工具类私有构造函数
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<CaptchaUtil> constructor = CaptchaUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
