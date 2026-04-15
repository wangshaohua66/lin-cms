package io.github.talelin.latticy.dto.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("重置密码DTO测试")
class ResetPasswordDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            ResetPasswordDTO dto = new ResetPasswordDTO();

            dto.setNewPassword("newpass123");
            dto.setConfirmPassword("newpass123");

            assertThat(dto.getNewPassword()).isEqualTo("newpass123");
            assertThat(dto.getConfirmPassword()).isEqualTo("newpass123");
        }
    }
}
