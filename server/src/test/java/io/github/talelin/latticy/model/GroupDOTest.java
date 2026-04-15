package io.github.talelin.latticy.model;

import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("分组DO测试")
class GroupDOTest {

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            GroupDO group = GroupDO.builder()
                    .name("测试分组")
                    .info("测试分组信息")
                    .level(GroupLevelEnum.USER)
                    .build();

            assertThat(group.getName()).isEqualTo("测试分组");
            assertThat(group.getInfo()).isEqualTo("测试分组信息");
            assertThat(group.getLevel()).isEqualTo(GroupLevelEnum.USER);
        }

        @Test
        @DisplayName("Builder - ROOT级别")
        void builder_WithRootLevel_ShouldBuildSuccessfully() {
            GroupDO group = GroupDO.builder()
                    .name("root")
                    .info("超级管理员")
                    .level(GroupLevelEnum.ROOT)
                    .build();

            assertThat(group.getLevel()).isEqualTo(GroupLevelEnum.ROOT);
        }

        @Test
        @DisplayName("Builder - GUEST级别")
        void builder_WithGuestLevel_ShouldBuildSuccessfully() {
            GroupDO group = GroupDO.builder()
                    .name("guest")
                    .info("游客")
                    .level(GroupLevelEnum.GUEST)
                    .build();

            assertThat(group.getLevel()).isEqualTo(GroupLevelEnum.GUEST);
        }

        @Test
        @DisplayName("Builder - 最小字段")
        void builder_WithMinimalFields_ShouldBuildSuccessfully() {
            GroupDO group = GroupDO.builder()
                    .name("测试分组")
                    .build();

            assertThat(group.getName()).isEqualTo("测试分组");
            assertThat(group.getInfo()).isNull();
            assertThat(group.getLevel()).isNull();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            GroupDO group = new GroupDO();

            group.setName("新分组");
            group.setInfo("新分组信息");
            group.setLevel(GroupLevelEnum.USER);

            assertThat(group.getName()).isEqualTo("新分组");
            assertThat(group.getInfo()).isEqualTo("新分组信息");
            assertThat(group.getLevel()).isEqualTo(GroupLevelEnum.USER);
        }

        @Test
        @DisplayName("Setter/Getter - 空字符串")
        void setterGetter_WithEmptyStrings_ShouldWorkCorrectly() {
            GroupDO group = new GroupDO();

            group.setName("");
            group.setInfo("");

            assertThat(group.getName()).isEmpty();
            assertThat(group.getInfo()).isEmpty();
        }

        @Test
        @DisplayName("Setter/Getter - null级别")
        void setterGetter_WithNullLevel_ShouldWorkCorrectly() {
            GroupDO group = new GroupDO();
            group.setLevel(null);

            assertThat(group.getLevel()).isNull();
        }
    }

    @Nested
    @DisplayName("Equals/HashCode测试")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Equals - 相同对象")
        void equals_WithSameObject_ShouldReturnTrue() {
            GroupDO group = GroupDO.builder().name("test").build();

            assertThat(group.equals(group)).isTrue();
            assertThat(group.hashCode()).isEqualTo(group.hashCode());
        }

        @Test
        @DisplayName("Equals - null对象")
        void equals_WithNull_ShouldReturnFalse() {
            GroupDO group = GroupDO.builder().name("test").build();

            assertThat(group.equals(null)).isFalse();
        }

        @Test
        @DisplayName("Equals - 不同类型")
        void equals_WithDifferentType_ShouldReturnFalse() {
            GroupDO group = GroupDO.builder().name("test").build();

            assertThat(group.equals("string")).isFalse();
        }
    }

    @Nested
    @DisplayName("无参构造函数测试")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("无参构造函数 - 创建对象")
        void noArgsConstructor_ShouldCreateObject() {
            GroupDO group = new GroupDO();

            assertThat(group).isNotNull();
            assertThat(group.getName()).isNull();
            assertThat(group.getInfo()).isNull();
            assertThat(group.getLevel()).isNull();
        }
    }
}
