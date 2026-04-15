package io.github.talelin.latticy.common.constant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("常量类测试")
class IdentityConstantTest {

    @Nested
    @DisplayName("常量值测试")
    class ConstantValueTests {

        @Test
        @DisplayName("用户名密码身份常量")
        void usernamePasswordIdentity_ShouldBeCorrect() {
            assertThat(IdentityConstant.USERNAME_PASSWORD_IDENTITY).isEqualTo("USERNAME_PASSWORD");
        }
    }
}
