package io.github.talelin.latticy.common.constant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * IdentityConstant 单元测试
 * 测试点：身份认证常量
 */
@DisplayName("身份认证常量测试")
class IdentityConstantTest {

    @Test
    @DisplayName("用户名密码身份类型常量")
    void usernamePasswordIdentity_ShouldBeCorrectValue() {
        // 测试点：验证用户名密码身份类型常量值
        assertThat(IdentityConstant.USERNAME_PASSWORD_IDENTITY).isEqualTo("USERNAME_PASSWORD");
    }

    @Test
    @DisplayName("私有构造函数 - 工具类不可实例化")
    void privateConstructor_UtilityClass_ShouldNotBeInstantiated() {
        // 测试点：验证工具类私有构造函数
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<IdentityConstant> constructor = IdentityConstant.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
