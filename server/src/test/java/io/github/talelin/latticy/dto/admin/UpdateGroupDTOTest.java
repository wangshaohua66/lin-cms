package io.github.talelin.latticy.dto.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("更新分组DTO测试")
class UpdateGroupDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            UpdateGroupDTO dto = new UpdateGroupDTO();

            dto.setName("新分组名");
            dto.setInfo("新分组信息");

            assertThat(dto.getName()).isEqualTo("新分组名");
            assertThat(dto.getInfo()).isEqualTo("新分组信息");
        }

        @Test
        @DisplayName("Setter/Getter - 空值")
        void setterGetter_WithNullValues_ShouldWorkCorrectly() {
            UpdateGroupDTO dto = new UpdateGroupDTO();

            dto.setName(null);
            dto.setInfo(null);

            assertThat(dto.getName()).isNull();
            assertThat(dto.getInfo()).isNull();
        }

        @Test
        @DisplayName("Setter/Getter - 空字符串")
        void setterGetter_WithEmptyStrings_ShouldWorkCorrectly() {
            UpdateGroupDTO dto = new UpdateGroupDTO();

            dto.setName("");
            dto.setInfo("");

            assertThat(dto.getName()).isEmpty();
            assertThat(dto.getInfo()).isEmpty();
        }
    }
}
