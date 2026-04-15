package io.github.talelin.latticy.dto.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("分配权限DTO测试")
class DispatchPermissionDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            DispatchPermissionDTO dto = new DispatchPermissionDTO();

            dto.setGroupId(1);
            dto.setPermissionId(10);

            assertThat(dto.getGroupId()).isEqualTo(1);
            assertThat(dto.getPermissionId()).isEqualTo(10);
        }
    }
}
