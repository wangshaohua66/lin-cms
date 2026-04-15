package io.github.talelin.latticy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("权限DO测试")
class PermissionDOTest {

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            PermissionDO permission = PermissionDO.builder()
                    .name("查看用户")
                    .module("用户管理")
                    .mount(true)
                    .build();

            assertThat(permission.getName()).isEqualTo("查看用户");
            assertThat(permission.getModule()).isEqualTo("用户管理");
            assertThat(permission.getMount()).isTrue();
        }

        @Test
        @DisplayName("Builder - mount为false")
        void builder_WithMountFalse_ShouldBuildSuccessfully() {
            PermissionDO permission = PermissionDO.builder()
                    .name("删除用户")
                    .module("用户管理")
                    .mount(false)
                    .build();

            assertThat(permission.getMount()).isFalse();
        }

        @Test
        @DisplayName("Builder - 最小字段")
        void builder_WithMinimalFields_ShouldBuildSuccessfully() {
            PermissionDO permission = PermissionDO.builder()
                    .name("查看用户")
                    .module("用户管理")
                    .build();

            assertThat(permission.getName()).isEqualTo("查看用户");
            assertThat(permission.getModule()).isEqualTo("用户管理");
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            PermissionDO permission = new PermissionDO();

            permission.setName("新增用户");
            permission.setModule("用户管理");
            permission.setMount(true);

            assertThat(permission.getName()).isEqualTo("新增用户");
            assertThat(permission.getModule()).isEqualTo("用户管理");
            assertThat(permission.getMount()).isTrue();
        }

        @Test
        @DisplayName("Setter/Getter - null值")
        void setterGetter_WithNullValues_ShouldWorkCorrectly() {
            PermissionDO permission = new PermissionDO();

            permission.setName(null);
            permission.setModule(null);
            permission.setMount(null);

            assertThat(permission.getName()).isNull();
            assertThat(permission.getModule()).isNull();
            assertThat(permission.getMount()).isNull();
        }
    }

    @Nested
    @DisplayName("Equals/HashCode测试")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Equals - 相同对象")
        void equals_WithSameObject_ShouldReturnTrue() {
            PermissionDO permission = PermissionDO.builder().name("test").build();

            assertThat(permission.equals(permission)).isTrue();
            assertThat(permission.hashCode()).isEqualTo(permission.hashCode());
        }

        @Test
        @DisplayName("Equals - null对象")
        void equals_WithNull_ShouldReturnFalse() {
            PermissionDO permission = PermissionDO.builder().name("test").build();

            assertThat(permission.equals(null)).isFalse();
        }

        @Test
        @DisplayName("Equals - 不同类型")
        void equals_WithDifferentType_ShouldReturnFalse() {
            PermissionDO permission = PermissionDO.builder().name("test").build();

            assertThat(permission.equals("string")).isFalse();
        }
    }

    @Nested
    @DisplayName("无参构造函数测试")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("无参构造函数 - 创建对象")
        void noArgsConstructor_ShouldCreateObject() {
            PermissionDO permission = new PermissionDO();

            assertThat(permission).isNotNull();
            assertThat(permission.getName()).isNull();
            assertThat(permission.getModule()).isNull();
            assertThat(permission.getMount()).isNull();
        }
    }
}
