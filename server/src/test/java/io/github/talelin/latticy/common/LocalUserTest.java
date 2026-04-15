package io.github.talelin.latticy.common;

import io.github.talelin.latticy.model.UserDO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("本地用户工具类测试")
class LocalUserTest {

    @AfterEach
    void tearDown() {
        LocalUser.clearLocalUser();
    }

    @Nested
    @DisplayName("设置和获取用户测试")
    class SetAndGetUserTests {

        @Test
        @DisplayName("设置用户后获取 - 成功")
        void setAndGetLocalUser_ShouldWork() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            LocalUser.setLocalUser(user);
            UserDO result = LocalUser.getLocalUser();

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1);
            assertThat(result.getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("未设置用户时获取 - 返回null")
        void getLocalUser_WhenNotSet_ShouldReturnNull() {
            UserDO result = LocalUser.getLocalUser();

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("清理用户测试")
    class ClearUserTests {

        @Test
        @DisplayName("清理用户后获取 - 返回null")
        void clearLocalUser_ShouldRemoveUser() {
            UserDO user = new UserDO();
            user.setId(1);
            LocalUser.setLocalUser(user);

            LocalUser.clearLocalUser();
            UserDO result = LocalUser.getLocalUser();

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("泛型获取用户测试")
    class GenericGetUserTests {

        @Test
        @DisplayName("泛型获取用户 - 成功")
        void getLocalUser_WithType_ShouldWork() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            LocalUser.setLocalUser(user);
            UserDO result = LocalUser.getLocalUser(UserDO.class);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1);
        }
    }
}
