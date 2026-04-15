package io.github.talelin.latticy.dto.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("修改密码DTO测试")
class ChangePasswordDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            ChangePasswordDTO dto = new ChangePasswordDTO();

            dto.setOldPassword("oldpass123");
            dto.setNewPassword("newpass123");
            dto.setConfirmPassword("newpass123");

            assertThat(dto.getOldPassword()).isEqualTo("oldpass123");
            assertThat(dto.getNewPassword()).isEqualTo("newpass123");
            assertThat(dto.getConfirmPassword()).isEqualTo("newpass123");
        }
    }

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("无参构造函数")
        void noArgsConstructor_ShouldCreateObject() {
            ChangePasswordDTO dto = new ChangePasswordDTO();

            assertThat(dto).isNotNull();
        }
    }
}
