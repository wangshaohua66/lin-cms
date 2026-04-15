package io.github.talelin.latticy.dto.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("新建分组DTO测试")
class NewGroupDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            NewGroupDTO dto = new NewGroupDTO();

            dto.setName("测试分组");
            dto.setInfo("测试分组信息");
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            assertThat(dto.getName()).isEqualTo("测试分组");
            assertThat(dto.getInfo()).isEqualTo("测试分组信息");
            assertThat(dto.getPermissionIds()).containsExactly(1, 2, 3);
        }

        @Test
        @DisplayName("Setter/Getter - 无权限ID")
        void setterGetter_WithoutPermissionIds_ShouldWorkCorrectly() {
            NewGroupDTO dto = new NewGroupDTO();

            dto.setName("测试分组");
            dto.setInfo("测试分组信息");

            assertThat(dto.getPermissionIds()).isNull();
        }

        @Test
        @DisplayName("Setter/Getter - 空权限ID列表")
        void setterGetter_WithEmptyPermissionIds_ShouldWorkCorrectly() {
            NewGroupDTO dto = new NewGroupDTO();
            dto.setPermissionIds(Collections.emptyList());

            assertThat(dto.getPermissionIds()).isEmpty();
        }

        @Test
        @DisplayName("Setter/Getter - 空值")
        void setterGetter_WithNullValues_ShouldWorkCorrectly() {
            NewGroupDTO dto = new NewGroupDTO();

            dto.setName(null);
            dto.setInfo(null);
            dto.setPermissionIds(null);

            assertThat(dto.getName()).isNull();
            assertThat(dto.getInfo()).isNull();
            assertThat(dto.getPermissionIds()).isNull();
        }

        @Test
        @DisplayName("Setter/Getter - 空字符串")
        void setterGetter_WithEmptyStrings_ShouldWorkCorrectly() {
            NewGroupDTO dto = new NewGroupDTO();

            dto.setName("");
            dto.setInfo("");

            assertThat(dto.getName()).isEmpty();
            assertThat(dto.getInfo()).isEmpty();
        }
    }
}
