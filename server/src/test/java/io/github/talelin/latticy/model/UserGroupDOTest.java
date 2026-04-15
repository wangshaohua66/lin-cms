package io.github.talelin.latticy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("用户分组DO测试")
class UserGroupDOTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("构造函数 - userId和groupId")
        void constructor_WithUserIdAndGroupId_ShouldCreateObject() {
            UserGroupDO userGroup = new UserGroupDO(1, 2);

            assertThat(userGroup.getUserId()).isEqualTo(1);
            assertThat(userGroup.getGroupId()).isEqualTo(2);
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            UserGroupDO userGroup = new UserGroupDO(1, 2);

            userGroup.setId(1);
            userGroup.setUserId(10);
            userGroup.setGroupId(20);

            assertThat(userGroup.getId()).isEqualTo(1);
            assertThat(userGroup.getUserId()).isEqualTo(10);
            assertThat(userGroup.getGroupId()).isEqualTo(20);
        }
    }
}
