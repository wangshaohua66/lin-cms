package io.github.talelin.latticy.common.configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginCaptchaProperties 单元测试
 * 测试点：登录验证码配置属性
 */
@DisplayName("登录验证码配置属性测试")
class LoginCaptchaPropertiesTest {

    private LoginCaptchaProperties properties;

    @BeforeEach
    void setUp() {
        properties = new LoginCaptchaProperties();
    }

    @Test
    @DisplayName("默认密钥长度 - 32字节")
    void defaultSecret_ShouldBe32Bytes() {
        // 测试点：验证默认密钥长度
        String secret = properties.getSecret();
        assertThat(secret).isNotNull();
        assertThat(secret.getBytes()).hasSize(32);
    }

    @Test
    @DisplayName("默认IV长度 - 16字节")
    void defaultIv_ShouldBe16Bytes() {
        // 测试点：验证默认IV长度
        String iv = properties.getIv();
        assertThat(iv).isNotNull();
        assertThat(iv.getBytes()).hasSize(16);
    }

    @Test
    @DisplayName("默认启用状态 - false")
    void defaultEnabled_ShouldBeFalse() {
        // 测试点：验证默认启用状态
        assertThat(properties.getEnabled()).isFalse();
    }

    @Test
    @DisplayName("设置有效密钥 - 16字节")
    void setSecret_With16Bytes_ShouldSet() {
        // 输入参数：16字节密钥
        // 预期结果：密钥被设置
        // 测试点：验证16字节密钥设置
        String secret16 = "1234567890123456";
        properties.setSecret(secret16);

        assertThat(properties.getSecret()).isEqualTo(secret16);
    }

    @Test
    @DisplayName("设置有效密钥 - 24字节")
    void setSecret_With24Bytes_ShouldSet() {
        // 输入参数：24字节密钥
        // 预期结果：密钥被设置
        // 测试点：验证24字节密钥设置
        String secret24 = "123456789012345678901234";
        properties.setSecret(secret24);

        assertThat(properties.getSecret()).isEqualTo(secret24);
    }

    @Test
    @DisplayName("设置有效密钥 - 32字节")
    void setSecret_With32Bytes_ShouldSet() {
        // 输入参数：32字节密钥
        // 预期结果：密钥被设置
        // 测试点：验证32字节密钥设置
        String secret32 = "12345678901234567890123456789012";
        properties.setSecret(secret32);

        assertThat(properties.getSecret()).isEqualTo(secret32);
    }

    @Test
    @DisplayName("设置无效密钥 - 其他长度")
    void setSecret_WithInvalidLength_ShouldNotSet() {
        // 输入参数：无效长度密钥
        // 预期结果：使用默认密钥
        // 测试点：验证无效密钥处理
        String defaultSecret = properties.getSecret();
        String invalidSecret = "12345"; // 5字节

        properties.setSecret(invalidSecret);

        assertThat(properties.getSecret()).isEqualTo(defaultSecret);
    }

    @Test
    @DisplayName("设置空密钥")
    void setSecret_WithEmptyString_ShouldNotSet() {
        // 输入参数：空字符串
        // 预期结果：使用默认密钥
        // 测试点：验证空字符串处理
        String defaultSecret = properties.getSecret();

        properties.setSecret("");

        assertThat(properties.getSecret()).isEqualTo(defaultSecret);
    }

    @Test
    @DisplayName("设置null密钥")
    void setSecret_WithNull_ShouldNotSet() {
        // 输入参数：null
        // 预期结果：使用默认密钥
        // 测试点：验证null处理
        String defaultSecret = properties.getSecret();

        properties.setSecret(null);

        assertThat(properties.getSecret()).isEqualTo(defaultSecret);
    }

    @Test
    @DisplayName("设置有效IV - 16字节")
    void setIv_With16Bytes_ShouldSet() {
        // 输入参数：16字节IV
        // 预期结果：IV被设置
        // 测试点：验证16字节IV设置
        String iv16 = "1234567890123456";
        properties.setIv(iv16);

        assertThat(properties.getIv()).isEqualTo(iv16);
    }

    @Test
    @DisplayName("设置无效IV - 其他长度")
    void setIv_WithInvalidLength_ShouldNotSet() {
        // 输入参数：无效长度IV
        // 预期结果：使用默认IV
        // 测试点：验证无效IV处理
        String defaultIv = properties.getIv();
        String invalidIv = "12345"; // 5字节

        properties.setIv(invalidIv);

        assertThat(properties.getIv()).isEqualTo(defaultIv);
    }

    @Test
    @DisplayName("设置空IV")
    void setIv_WithEmptyString_ShouldNotSet() {
        // 输入参数：空字符串
        // 预期结果：使用默认IV
        // 测试点：验证空字符串处理
        String defaultIv = properties.getIv();

        properties.setIv("");

        assertThat(properties.getIv()).isEqualTo(defaultIv);
    }

    @Test
    @DisplayName("设置null IV")
    void setIv_WithNull_ShouldNotSet() {
        // 输入参数：null
        // 预期结果：使用默认IV
        // 测试点：验证null处理
        String defaultIv = properties.getIv();

        properties.setIv(null);

        assertThat(properties.getIv()).isEqualTo(defaultIv);
    }

    @Test
    @DisplayName("设置启用状态 - true")
    void setEnabled_WithTrue_ShouldSet() {
        // 输入参数：true
        // 预期结果：启用状态被设置
        // 测试点：验证启用状态设置
        properties.setEnabled(true);

        assertThat(properties.getEnabled()).isTrue();
    }

    @Test
    @DisplayName("设置启用状态 - false")
    void setEnabled_WithFalse_ShouldSet() {
        // 输入参数：false
        // 预期结果：启用状态被设置
        // 测试点：验证禁用状态设置
        properties.setEnabled(true);
        properties.setEnabled(false);

        assertThat(properties.getEnabled()).isFalse();
    }
}
