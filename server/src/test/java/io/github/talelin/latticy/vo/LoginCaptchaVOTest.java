package io.github.talelin.latticy.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("登录验证码VO测试")
class LoginCaptchaVOTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("全参构造函数")
        void allArgsConstructor_ShouldCreateObject() {
            LoginCaptchaVO vo = new LoginCaptchaVO("encryptedtag", "data:image/png;base64,abc");

            assertThat(vo.getTag()).isEqualTo("encryptedtag");
            assertThat(vo.getImage()).isEqualTo("data:image/png;base64,abc");
        }

        @Test
        @DisplayName("无参构造函数")
        void noArgsConstructor_ShouldCreateObject() {
            LoginCaptchaVO vo = new LoginCaptchaVO();

            assertThat(vo).isNotNull();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            LoginCaptchaVO vo = new LoginCaptchaVO();

            vo.setTag("newtag");
            vo.setImage("data:image/png;base64,newimage");

            assertThat(vo.getTag()).isEqualTo("newtag");
            assertThat(vo.getImage()).isEqualTo("data:image/png;base64,newimage");
        }
    }
}
