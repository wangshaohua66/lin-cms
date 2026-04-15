package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.latticy.BaseIntegrationTest;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.vo.LoginCaptchaVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Transactional
@Rollback
@DisplayName("用户服务集成测试")
class UserServiceImplIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Nested
    @DisplayName("创建用户测试")
    class CreateUserTests {

        @Test
        @DisplayName("创建用户 - 成功创建")
        void createUser_ShouldSucceed() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("newuser");
            dto.setPassword("password123");
            dto.setConfirmPassword("password123");
            dto.setEmail("newuser@example.com");

            UserDO user = userService.createUser(dto);

            assertThat(user).isNotNull();
            assertThat(user.getId()).isPositive();
            assertThat(user.getUsername()).isEqualTo("newuser");
        }

        @Test
        @DisplayName("创建用户 - 用户名已存在")
        void createUser_WhenUsernameExists_ShouldThrowException() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("root");
            dto.setPassword("password123");

            assertThatThrownBy(() -> userService.createUser(dto))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("创建用户 - 邮箱已存在")
        void createUser_WhenEmailExists_ShouldThrowException() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("uniqueuser");
            dto.setPassword("password123");
            dto.setConfirmPassword("password123");
            dto.setEmail("test@example.com");

            assertThatThrownBy(() -> userService.createUser(dto))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("创建用户 - 指定分组")
        void createUser_WithGroupIds_ShouldSucceed() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("userwithgroup");
            dto.setPassword("password123");
            dto.setGroupIds(Arrays.asList(2));

            UserDO user = userService.createUser(dto);

            assertThat(user).isNotNull();
            List<GroupDO> groups = userService.getUserGroups(user.getId());
            assertThat(groups).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("更新用户信息测试")
    class UpdateUserInfoTests {

        @Test
        @DisplayName("更新用户信息 - 成功更新")
        void updateUserInfo_ShouldSucceed() {
            UserDO testUser = new UserDO();
            testUser.setUsername("testupdateuser");
            testUser.setNickname("测试更新");
            LocalUser.setLocalUser(testUser);

            UpdateInfoDTO dto = new UpdateInfoDTO();
            dto.setNickname("更新后的昵称");

            UserDO updated = userService.updateUserInfo(dto);

            assertThat(updated.getNickname()).isEqualTo("更新后的昵称");
        }
    }

    @Nested
    @DisplayName("修改密码测试")
    class ChangePasswordTests {

        @Test
        @DisplayName("修改密码 - 成功修改")
        void changePassword_ShouldSucceed() {
            RegisterDTO registerDTO = new RegisterDTO();
            registerDTO.setUsername("pwduser");
            registerDTO.setPassword("oldpassword123");
            UserDO user = userService.createUser(registerDTO);

            LocalUser.setLocalUser(user);

            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword("oldpassword123");
            dto.setNewPassword("newpassword123");

            UserDO result = userService.changeUserPassword(dto);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("获取用户分组测试")
    class GetUserGroupsTests {

        @Test
        @DisplayName("获取用户分组 - 成功获取")
        void getUserGroups_ShouldReturnGroups() {
            List<GroupDO> groups = userService.getUserGroups(1);

            assertThat(groups).isNotEmpty();
        }

        @Test
        @DisplayName("获取用户分组 - 用户无分组")
        void getUserGroups_WhenNoGroups_ShouldReturnEmpty() {
            List<GroupDO> groups = userService.getUserGroups(9999);

            assertThat(groups).isEmpty();
        }
    }

    @Nested
    @DisplayName("获取用户权限测试")
    class GetUserPermissionsTests {

        @Test
        @DisplayName("获取用户权限 - 成功获取")
        void getUserPermissions_ShouldReturnPermissions() {
            List<PermissionDO> permissions = userService.getUserPermissions(1);

            assertThat(permissions).isNotEmpty();
        }

        @Test
        @DisplayName("获取用户权限 - 用户无权限")
        void getUserPermissions_WhenNoPermissions_ShouldReturnEmpty() {
            List<PermissionDO> permissions = userService.getUserPermissions(9999);

            assertThat(permissions).isEmpty();
        }

        @Test
        @DisplayName("按模块获取用户权限 - 成功获取")
        void getUserPermissionsByModule_ShouldReturnPermissions() {
            List<PermissionDO> permissions = userService.getUserPermissionsByModule(1, "图书");

            assertThat(permissions).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("检查用户存在测试")
    class CheckUserExistTests {

        @Test
        @DisplayName("检查用户存在 - 用户名存在")
        void checkUserExistByUsername_WhenExists_ShouldReturnTrue() {
            boolean exists = userService.checkUserExistByUsername("root");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("检查用户存在 - 用户名不存在")
        void checkUserExistByUsername_WhenNotExists_ShouldReturnFalse() {
            boolean exists = userService.checkUserExistByUsername("nonexistent");

            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("检查用户存在 - 邮箱存在")
        void checkUserExistByEmail_WhenExists_ShouldReturnTrue() {
            boolean exists = userService.checkUserExistByEmail("test@example.com");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("检查用户存在 - 邮箱不存在")
        void checkUserExistByEmail_WhenNotExists_ShouldReturnFalse() {
            boolean exists = userService.checkUserExistByEmail("nonexistent@example.com");

            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("检查用户存在 - ID存在")
        void checkUserExistById_WhenExists_ShouldReturnTrue() {
            boolean exists = userService.checkUserExistById(1);

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("检查用户存在 - ID不存在")
        void checkUserExistById_WhenNotExists_ShouldReturnFalse() {
            boolean exists = userService.checkUserExistById(9999);

            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("获取用户测试")
    class GetUserTests {

        @Test
        @DisplayName("根据用户名获取用户 - 存在")
        void getUserByUsername_WhenExists_ShouldReturnUser() {
            UserDO user = userService.getUserByUsername("root");

            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isEqualTo("root");
        }

        @Test
        @DisplayName("根据用户名获取用户 - 不存在")
        void getUserByUsername_WhenNotExists_ShouldReturnNull() {
            UserDO user = userService.getUserByUsername("nonexistent");

            assertThat(user).isNull();
        }
    }

    @Nested
    @DisplayName("验证码测试")
    class CaptchaTests {

        @Test
        @DisplayName("生成验证码 - 成功生成")
        void generateCaptcha_ShouldReturnCaptchaVO() throws Exception {
            LoginCaptchaVO captcha = userService.generateCaptcha();

            assertThat(captcha).isNotNull();
            assertThat(captcha.getTag()).isNotNull();
            assertThat(captcha.getImage()).isNotNull();
        }

        @Test
        @DisplayName("验证验证码 - 错误标签")
        void verifyCaptcha_WhenInvalidTag_ShouldReturnFalse() {
            boolean result = userService.verifyCaptcha("test", "invalidtag");

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("获取Root用户ID测试")
    class GetRootUserIdTests {

        @Test
        @DisplayName("获取Root用户ID - 成功获取")
        void getRootUserId_ShouldReturnId() {
            Integer rootUserId = userService.getRootUserId();

            assertThat(rootUserId).isNotNull();
        }
    }
}
