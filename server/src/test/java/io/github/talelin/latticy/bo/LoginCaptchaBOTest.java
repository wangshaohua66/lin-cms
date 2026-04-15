package io.github.talelin.latticy.bo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("登录验证码BO测试")
class LoginCaptchaBOTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("全参构造函数")
        void allArgsConstructor_ShouldCreateObject() {
            LoginCaptchaBO bo = new LoginCaptchaBO("ABCD", 1234567890L);

            assertThat(bo.getCaptcha()).isEqualTo("ABCD");
            assertThat(bo.getExpired()).isEqualTo(1234567890L);
        }

        @Test
        @DisplayName("无参构造函数")
        void noArgsConstructor_ShouldCreateObject() {
            LoginCaptchaBO bo = new LoginCaptchaBO();

            assertThat(bo).isNotNull();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            LoginCaptchaBO bo = new LoginCaptchaBO();

            bo.setCaptcha("XYZ");
            bo.setExpired(System.currentTimeMillis());

            assertThat(bo.getCaptcha()).isEqualTo("XYZ");
            assertThat(bo.getExpired()).isNotNull();
        }
    }
}
