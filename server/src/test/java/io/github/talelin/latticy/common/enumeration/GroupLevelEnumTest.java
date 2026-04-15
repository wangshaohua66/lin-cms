package io.github.talelin.latticy.common.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("分组级别枚举测试")
class GroupLevelEnumTest {

    @Nested
    @DisplayName("枚举值测试")
    class EnumValueTests {

        @Test
        @DisplayName("ROOT枚举值")
        void rootEnum_ShouldHaveCorrectValue() {
            assertThat(GroupLevelEnum.ROOT.getValue()).isEqualTo(1);
        }

        @Test
        @DisplayName("GUEST枚举值")
        void guestEnum_ShouldHaveCorrectValue() {
            assertThat(GroupLevelEnum.GUEST.getValue()).isEqualTo(2);
        }

        @Test
        @DisplayName("USER枚举值")
        void userEnum_ShouldHaveCorrectValue() {
            assertThat(GroupLevelEnum.USER.getValue()).isEqualTo(3);
        }
    }

    @Nested
    @DisplayName("枚举名称测试")
    class EnumNameTests {

        @Test
        @DisplayName("枚举名称")
        void enumNames_ShouldBeCorrect() {
            assertThat(GroupLevelEnum.ROOT.name()).isEqualTo("ROOT");
            assertThat(GroupLevelEnum.GUEST.name()).isEqualTo("GUEST");
            assertThat(GroupLevelEnum.USER.name()).isEqualTo("USER");
        }
    }

    @Nested
    @DisplayName("枚举遍历测试")
    class EnumIterationTests {

        @Test
        @DisplayName("遍历所有枚举值")
        void iterateEnum_ShouldHaveThreeValues() {
            GroupLevelEnum[] values = GroupLevelEnum.values();

            assertThat(values).hasSize(3);
            assertThat(values).containsExactly(GroupLevelEnum.ROOT, GroupLevelEnum.GUEST, GroupLevelEnum.USER);
        }
    }

    @Nested
    @DisplayName("valueOf测试")
    class ValueOfTests {

        @Test
        @DisplayName("根据名称获取枚举")
        void valueOf_ShouldReturnCorrectEnum() {
            assertThat(GroupLevelEnum.valueOf("ROOT")).isEqualTo(GroupLevelEnum.ROOT);
            assertThat(GroupLevelEnum.valueOf("GUEST")).isEqualTo(GroupLevelEnum.GUEST);
            assertThat(GroupLevelEnum.valueOf("USER")).isEqualTo(GroupLevelEnum.USER);
        }

        @Test
        @DisplayName("根据无效名称获取枚举 - 抛出异常")
        void valueOf_WithInvalidName_ShouldThrowException() {
            org.junit.jupiter.api.Assertions.assertThrows(
                    IllegalArgumentException.class,
                    () -> GroupLevelEnum.valueOf("INVALID")
            );
        }
    }
}
