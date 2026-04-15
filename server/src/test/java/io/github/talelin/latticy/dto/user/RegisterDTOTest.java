package io.github.talelin.latticy.dto.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("注册DTO测试")
class RegisterDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            RegisterDTO dto = new RegisterDTO();

            dto.setUsername("testuser");
            dto.setEmail("test@example.com");
            dto.setPassword("password123");
            dto.setConfirmPassword("password123");
            dto.setGroupIds(Arrays.asList(1, 2));

            assertThat(dto.getUsername()).isEqualTo("testuser");
            assertThat(dto.getEmail()).isEqualTo("test@example.com");
            assertThat(dto.getPassword()).isEqualTo("password123");
            assertThat(dto.getConfirmPassword()).isEqualTo("password123");
            assertThat(dto.getGroupIds()).containsExactly(1, 2);
        }

        @Test
        @DisplayName("Setter/Getter - 空值")
        void setterGetter_WithNullValues_ShouldWorkCorrectly() {
            RegisterDTO dto = new RegisterDTO();

            dto.setUsername(null);
            dto.setEmail(null);
            dto.setPassword(null);
            dto.setConfirmPassword(null);
            dto.setGroupIds(null);

            assertThat(dto.getUsername()).isNull();
            assertThat(dto.getEmail()).isNull();
            assertThat(dto.getPassword()).isNull();
            assertThat(dto.getConfirmPassword()).isNull();
            assertThat(dto.getGroupIds()).isNull();
        }

        @Test
        @DisplayName("Setter/Getter - 空字符串")
        void setterGetter_WithEmptyStrings_ShouldWorkCorrectly() {
            RegisterDTO dto = new RegisterDTO();

            dto.setUsername("");
            dto.setEmail("");
            dto.setPassword("");
            dto.setConfirmPassword("");

            assertThat(dto.getUsername()).isEmpty();
            assertThat(dto.getEmail()).isEmpty();
            assertThat(dto.getPassword()).isEmpty();
            assertThat(dto.getConfirmPassword()).isEmpty();
        }

        @Test
        @DisplayName("Setter/Getter - 空分组列表")
        void setterGetter_WithEmptyGroupIds_ShouldWorkCorrectly() {
            RegisterDTO dto = new RegisterDTO();
            dto.setGroupIds(Collections.emptyList());

            assertThat(dto.getGroupIds()).isEmpty();
        }
    }

    @Nested
    @DisplayName("无参构造函数测试")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("无参构造函数 - 创建对象")
        void noArgsConstructor_ShouldCreateObject() {
            RegisterDTO dto = new RegisterDTO();

            assertThat(dto).isNotNull();
            assertThat(dto.getUsername()).isNull();
            assertThat(dto.getEmail()).isNull();
            assertThat(dto.getPassword()).isNull();
            assertThat(dto.getConfirmPassword()).isNull();
            assertThat(dto.getGroupIds()).isNull();
        }
    }
}
