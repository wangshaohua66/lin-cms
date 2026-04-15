package io.github.talelin.latticy.dto.query;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("基础分页DTO测试")
class BasePageDTOTest {

    @Nested
    @DisplayName("Getter默认值测试")
    class GetterDefaultTests {

        @Test
        @DisplayName("getPage - null时返回默认值0")
        void getPage_WhenNull_ShouldReturnDefault() {
            BasePageDTO dto = new BasePageDTO();
            dto.setPage(null);

            assertThat(dto.getPage()).isEqualTo(0);
        }

        @Test
        @DisplayName("getCount - null时返回默认值10")
        void getCount_WhenNull_ShouldReturnDefault() {
            BasePageDTO dto = new BasePageDTO();
            dto.setCount(null);

            assertThat(dto.getCount()).isEqualTo(10);
        }

        @Test
        @DisplayName("getPage - 有值时返回设置值")
        void getPage_WhenSet_ShouldReturnValue() {
            BasePageDTO dto = new BasePageDTO();
            dto.setPage(5);

            assertThat(dto.getPage()).isEqualTo(5);
        }

        @Test
        @DisplayName("getCount - 有值时返回设置值")
        void getCount_WhenSet_ShouldReturnValue() {
            BasePageDTO dto = new BasePageDTO();
            dto.setCount(20);

            assertThat(dto.getCount()).isEqualTo(20);
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            BasePageDTO dto = new BasePageDTO();

            dto.setPage(2);
            dto.setCount(15);

            assertThat(dto.getPage()).isEqualTo(2);
            assertThat(dto.getCount()).isEqualTo(15);
        }
    }
}
