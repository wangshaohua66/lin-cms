package io.github.talelin.latticy.dto.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("登录DTO测试")
class LoginDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            LoginDTO dto = new LoginDTO();

            dto.setUsername("testuser");
            dto.setPassword("password123");
            dto.setCaptcha("ABCD");

            assertThat(dto.getUsername()).isEqualTo("testuser");
            assertThat(dto.getPassword()).isEqualTo("password123");
            assertThat(dto.getCaptcha()).isEqualTo("ABCD");
        }

        @Test
        @DisplayName("Setter/Getter - 无验证码")
        void setterGetter_WithoutCaptcha_ShouldWorkCorrectly() {
            LoginDTO dto = new LoginDTO();

            dto.setUsername("testuser");
            dto.setPassword("password123");

            assertThat(dto.getCaptcha()).isNull();
        }

        @Test
        @DisplayName("Setter/Getter - 空值")
        void setterGetter_WithNullValues_ShouldWorkCorrectly() {
            LoginDTO dto = new LoginDTO();

            dto.setUsername(null);
            dto.setPassword(null);
            dto.setCaptcha(null);

            assertThat(dto.getUsername()).isNull();
            assertThat(dto.getPassword()).isNull();
            assertThat(dto.getCaptcha()).isNull();
        }

        @Test
        @DisplayName("Setter/Getter - 空字符串")
        void setterGetter_WithEmptyStrings_ShouldWorkCorrectly() {
            LoginDTO dto = new LoginDTO();

            dto.setUsername("");
            dto.setPassword("");
            dto.setCaptcha("");

            assertThat(dto.getUsername()).isEmpty();
            assertThat(dto.getPassword()).isEmpty();
            assertThat(dto.getCaptcha()).isEmpty();
        }
    }

    @Nested
    @DisplayName("无参构造函数测试")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("无参构造函数 - 创建对象")
        void noArgsConstructor_ShouldCreateObject() {
            LoginDTO dto = new LoginDTO();

            assertThat(dto).isNotNull();
            assertThat(dto.getUsername()).isNull();
            assertThat(dto.getPassword()).isNull();
            assertThat(dto.getCaptcha()).isNull();
        }
    }
}
