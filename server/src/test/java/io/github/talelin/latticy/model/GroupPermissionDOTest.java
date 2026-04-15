package io.github.talelin.latticy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("分组权限DO测试")
class GroupPermissionDOTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("构造函数 - groupId和permissionId")
        void constructor_WithGroupIdAndPermissionId_ShouldCreateObject() {
            GroupPermissionDO groupPermission = new GroupPermissionDO(1, 2);

            assertThat(groupPermission.getGroupId()).isEqualTo(1);
            assertThat(groupPermission.getPermissionId()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            GroupPermissionDO groupPermission = new GroupPermissionDO(1, 2);

            groupPermission.setId(1);
            groupPermission.setGroupId(10);
            groupPermission.setPermissionId(20);

            assertThat(groupPermission.getId()).isEqualTo(1);
            assertThat(groupPermission.getGroupId()).isEqualTo(10);
            assertThat(groupPermission.getPermissionId()).isEqualTo(20);
        }
    }
}
