package io.github.talelin.latticy.dto.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("分配多个权限DTO测试")
class DispatchPermissionsDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            DispatchPermissionsDTO dto = new DispatchPermissionsDTO();

            dto.setGroupId(1);
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            assertThat(dto.getGroupId()).isEqualTo(1);
            assertThat(dto.getPermissionIds()).hasSize(3);
        }
    }
}
