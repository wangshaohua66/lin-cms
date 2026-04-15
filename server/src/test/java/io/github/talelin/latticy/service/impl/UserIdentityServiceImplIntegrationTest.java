package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.BaseIntegrationTest;
import io.github.talelin.latticy.model.UserIdentityDO;
import io.github.talelin.latticy.service.UserIdentityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@Rollback
@DisplayName("用户身份服务集成测试")
class UserIdentityServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserIdentityService userIdentityService;

    @Nested
    @DisplayName("创建身份测试")
    class CreateIdentityTests {

        @Test
        @DisplayName("创建身份 - 成功创建")
        void createIdentity_ShouldSucceed() {
            UserIdentityDO identity = userIdentityService.createIdentity(
                    100, "USERNAME_PASSWORD", "testuser", "password123");

            assertThat(identity).isNotNull();
            assertThat(identity.getUserId()).isEqualTo(100);
            assertThat(identity.getIdentityType()).isEqualTo("USERNAME_PASSWORD");
            assertThat(identity.getIdentifier()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("创建用户名密码身份 - 成功创建")
        void createUsernamePasswordIdentity_ShouldSucceed() {
            UserIdentityDO identity = userIdentityService.createUsernamePasswordIdentity(
                    101, "newuser", "password123");

            assertThat(identity).isNotNull();
            assertThat(identity.getUserId()).isEqualTo(101);
            assertThat(identity.getIdentifier()).isEqualTo("newuser");
            assertThat(identity.getCredential()).isNotNull();
        }
    }

    @Nested
    @DisplayName("验证用户名密码测试")
    class VerifyUsernamePasswordTests {

        @Test
        @DisplayName("验证用户名密码 - 正确密码")
        void verifyUsernamePassword_WhenCorrect_ShouldReturnTrue() {
            boolean result = userIdentityService.verifyUsernamePassword(1, "root", "123456");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("验证用户名密码 - 错误密码")
        void verifyUsernamePassword_WhenWrongPassword_ShouldReturnFalse() {
            boolean result = userIdentityService.verifyUsernamePassword(1, "root", "wrongpassword");

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("修改密码测试")
    class ChangePasswordTests {

        @Test
        @DisplayName("修改密码 - 成功修改")
        void changePassword_ShouldReturnTrue() {
            boolean result = userIdentityService.changePassword(1, "newpassword123");

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("修改用户名测试")
    class ChangeUsernameTests {

        @Test
        @DisplayName("修改用户名 - 成功修改")
        void changeUsername_ShouldReturnTrue() {
            boolean result = userIdentityService.changeUsername(1, "newroot");

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("修改用户名密码测试")
    class ChangeUsernamePasswordTests {

        @Test
        @DisplayName("修改用户名密码 - 成功修改")
        void changeUsernamePassword_ShouldReturnTrue() {
            boolean result = userIdentityService.changeUsernamePassword(1, "newroot", "newpassword123");

            assertThat(result).isTrue();
        }
    }
}
