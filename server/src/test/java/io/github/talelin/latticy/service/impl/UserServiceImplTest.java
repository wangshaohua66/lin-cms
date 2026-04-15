package io.github.talelin.latticy.service.impl;

import io.github.talelin.autoconfigure.exception.FailedException;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.common.configuration.LoginCaptchaProperties;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.mapper.UserGroupMapper;
import io.github.talelin.latticy.mapper.UserMapper;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.model.UserGroupDO;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.service.PermissionService;
import io.github.talelin.latticy.service.UserIdentityService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("UserService单元测试")
class UserServiceImplTest {

    @MockBean
    private UserIdentityService userIdentityService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserGroupMapper userGroupMapper;

    @MockBean
    private UserMapper userMapper;

    @MockBean
    private LoginCaptchaProperties captchaConfig;

    @MockBean
    private PermissionService permissionService;

    @Autowired
    private UserServiceImpl userService;

    @Nested
    @DisplayName("创建用户测试")
    class CreateUserTests {

        @Test
        @DisplayName("输入参数：有效用户DTO、非空分组；预期结果：返回创建的用户；测试点：正常创建用户，指定分组")
        void createUser_WithValidDTOAndGroups_ShouldReturnUser() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("testuser");
            dto.setEmail("test@example.com");
            dto.setPassword("password123");
            dto.setGroupIds(Arrays.asList(2, 3));

            when(userMapper.selectCountByUsername(eq("testuser"))).thenReturn(0);
            when(userMapper.selectCount(any())).thenReturn(0L);
            when(userMapper.insert(any(UserDO.class))).thenReturn(1);
            when(groupService.checkGroupExistById(anyInt())).thenReturn(true);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(userGroupMapper.insertBatch(anyList())).thenReturn(2);
            when(userIdentityService.createUsernamePasswordIdentity(anyInt(), anyString(), anyString())).thenReturn(null);

            UserDO result = userService.createUser(dto);

            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("testuser");
            verify(userGroupMapper, times(1)).insertBatch(anyList());
        }

        @Test
        @DisplayName("输入参数：用户DTO、不指定分组；预期结果：返回创建的用户、分配访客分组；测试点：创建用户不指定分组")
        void createUser_WithoutGroups_ShouldAssignGuestGroup() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("testuser");
            dto.setEmail("test@example.com");
            dto.setPassword("password123");
            dto.setGroupIds(Collections.emptyList());

            when(userMapper.selectCountByUsername(eq("testuser"))).thenReturn(0);
            when(userMapper.selectCount(any())).thenReturn(0L);
            when(userMapper.insert(any(UserDO.class))).thenReturn(1);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.GUEST))).thenReturn(3);

            UserDO result = userService.createUser(dto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：空邮箱的用户DTO；预期结果：返回创建的用户、邮箱为null；测试点：邮箱为空处理")
        void createUser_WithEmptyEmail_ShouldSetToNull() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("testuser");
            dto.setEmail("");
            dto.setPassword("password123");

            when(userMapper.selectCountByUsername(eq("testuser"))).thenReturn(0);
            when(userMapper.selectCount(any())).thenReturn(0L);
            when(userMapper.insert(any(UserDO.class))).thenReturn(1);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.GUEST))).thenReturn(3);

            UserDO result = userService.createUser(dto);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isNull();
        }

        @Test
        @DisplayName("输入参数：重复的用户名；预期结果：抛出ForbiddenException；测试点：用户名重复校验")
        void createUser_WithDuplicateUsername_ShouldThrowForbiddenException() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("existinguser");
            dto.setPassword("password123");

            when(userMapper.selectCountByUsername(eq("existinguser"))).thenReturn(1);

            assertThrows(ForbiddenException.class, () -> userService.createUser(dto));
        }

        @Test
        @DisplayName("输入参数：重复的邮箱；预期结果：抛出ForbiddenException；测试点：邮箱重复校验")
        void createUser_WithDuplicateEmail_ShouldThrowForbiddenException() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("newuser");
            dto.setEmail("existing@example.com");
            dto.setPassword("password123");

            when(userMapper.selectCountByUsername(eq("newuser"))).thenReturn(0);
            when(userMapper.selectCount(any())).thenReturn(1L);

            assertThrows(ForbiddenException.class, () -> userService.createUser(dto));
        }

        @Test
        @DisplayName("输入参数：包含root分组的DTO；预期结果：抛出ForbiddenException；测试点：root分组权限控制")
        void createUser_WithRootGroup_ShouldThrowForbiddenException() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("testuser");
            dto.setPassword("password123");
            dto.setGroupIds(Arrays.asList(1));

            when(userMapper.selectCountByUsername(eq("testuser"))).thenReturn(0);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);

            assertThrows(ForbiddenException.class, () -> userService.createUser(dto));
        }

        @Test
        @DisplayName("输入参数：包含不存在分组的DTO；预期结果：抛出NotFoundException；测试点：分组存在性校验")
        void createUser_WithNonExistentGroup_ShouldThrowNotFoundException() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("testuser");
            dto.setPassword("password123");
            dto.setGroupIds(Arrays.asList(999));

            when(userMapper.selectCountByUsername(eq("testuser"))).thenReturn(0);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(groupService.checkGroupExistById(eq(999))).thenReturn(false);

            assertThrows(NotFoundException.class, () -> userService.createUser(dto));
        }
    }

    @Nested
    @DisplayName("修改密码测试")
    class ChangePasswordTests {
        @Test
        @DisplayName("输入参数：null的DTO；预期结果：抛出异常；测试点：空参数处理")
        void changeUserPassword_WithNullDTO_ShouldThrowException() {
            assertThrows(Exception.class, () -> userService.changeUserPassword(null));
        }
    }

    @Nested
    @DisplayName("用户存在性检查测试")
    class UserExistenceTests {

        @Test
        @DisplayName("输入参数：存在的用户ID；预期结果：返回true；测试点：通过ID检查用户存在")
        void checkUserExistById_ShouldReturnCorrectResult() {
            when(userMapper.selectCountById(eq(1))).thenReturn(1);
            when(userMapper.selectCountById(eq(999))).thenReturn(0);

            assertThat(userService.checkUserExistById(1)).isTrue();
            assertThat(userService.checkUserExistById(999)).isFalse();
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"nonexistent"})
        @DisplayName("输入参数：null/空/不存在的用户名；预期结果：返回false；测试点：用户名不存在场景")
        void checkUserExistByUsername_WithNullOrEmpty_ShouldReturnFalse(String username) {
            boolean result = userService.checkUserExistByUsername(username);

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("输入参数：存在的用户名；预期结果：返回true；测试点：用户名存在场景")
        void checkUserExistByUsername_ShouldReturnCorrectResult() {
            when(userMapper.selectCountByUsername(eq("existing"))).thenReturn(1);

            boolean result = userService.checkUserExistByUsername("existing");

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("用户查询测试")
    class UserQueryTests {
        @Test
        @DisplayName("输入参数：存在的用户名；预期结果：返回用户；测试点：根据用户名查询用户")
        void getUserByUsername_WithExistingUser_ShouldReturnUser() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            when(userMapper.selectOne(any())).thenReturn(user);

            UserDO result = userService.getUserByUsername("testuser");

            assertThat(result).isNotNull();
            assertThat(result.getUsername()).isEqualTo("testuser");
        }

        @Test
        @DisplayName("输入参数：不存在的用户名；预期结果：返回null；测试点：用户名不存在场景")
        void getUserByUsername_WithNonExistentUser_ShouldReturnNull() {
            when(userMapper.selectOne(any())).thenReturn(null);

            UserDO result = userService.getUserByUsername("nonexistent");

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("用户权限查询测试")
    class UserPermissionTests {

        @Test
        @DisplayName("输入参数：用户ID；预期结果：返回用户权限列表；测试点：查询用户权限")
        void getUserPermissions_WithValidUserId_ShouldReturnPermissions() {
            List<Integer> groupIds = Arrays.asList(1, 2);
            List<PermissionDO> permissions = Arrays.asList(new PermissionDO(), new PermissionDO());

            when(groupService.getUserGroupIdsByUserId(eq(1))).thenReturn(groupIds);
            when(permissionService.getPermissionByGroupIds(eq(groupIds))).thenReturn(permissions);

            List<PermissionDO> result = userService.getUserPermissions(1);

            assertThat(result).hasSize(2);
            verify(permissionService, times(1)).getPermissionByGroupIds(eq(groupIds));
        }

        @Test
        @DisplayName("输入参数：无分组的用户ID；预期结果：返回空列表；测试点：用户无分组场景")
        void getUserPermissions_WithEmptyGroups_ShouldReturnEmptyList() {
            when(groupService.getUserGroupIdsByUserId(eq(999))).thenReturn(Collections.emptyList());

            List<PermissionDO> result = userService.getUserPermissions(999);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("输入参数：用户ID和模块名；预期结果：返回模块权限列表；测试点：按模块查询权限")
        void getUserPermissionsByModule_WithValidParams_ShouldReturnFilteredPermissions() {
            List<Integer> groupIds = Arrays.asList(1, 2);
            List<PermissionDO> permissions = Collections.singletonList(new PermissionDO());

            when(groupService.getUserGroupIdsByUserId(eq(1))).thenReturn(groupIds);
            when(permissionService.getPermissionByGroupIdsAndModule(eq(groupIds), eq("user"))).thenReturn(permissions);

            List<PermissionDO> result = userService.getUserPermissionsByModule(1, "user");

            assertThat(result).hasSize(1);
        }
    }

    @Nested
    @DisplayName("超级管理员测试")
    class RootUserTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回超级管理员用户ID；测试点：获取超级管理员ID")
        void getRootUserId_WithExistingRoot_ShouldReturnUserId() {
            UserGroupDO userGroup = new UserGroupDO(1, 1);

            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(userGroupMapper.selectOne(any())).thenReturn(userGroup);

            Integer result = userService.getRootUserId();

            assertThat(result).isEqualTo(1);
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回0；测试点：不存在超级管理员场景")
        void getRootUserId_WithoutRoot_ShouldReturnZero() {
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(0);

            Integer result = userService.getRootUserId();

            assertThat(result).isZero();
        }
    }

    @Nested
    @DisplayName("用户信息更新测试")
    class UserInfoUpdateTests {

        @Test
        @DisplayName("输入参数：更新信息DTO；预期结果：返回更新后的用户；测试点：更新用户基本信息")
        void updateUserInfo_WithValidDTO_ShouldReturnUpdatedUser() {
            UpdateInfoDTO dto = new UpdateInfoDTO();
            dto.setUsername("newname");
            dto.setEmail("new@example.com");

            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("oldname");

            try (var mockedStatic = mockStatic(LocalUser.class)) {
                mockedStatic.when(LocalUser::getLocalUser).thenReturn(user);
                when(userMapper.selectCountByUsername(eq("newname"))).thenReturn(0);
                when(userIdentityService.changeUsername(eq(1), eq("newname"))).thenReturn(true);
                when(userMapper.updateById(any(UserDO.class))).thenReturn(1);

                UserDO result = userService.updateUserInfo(dto);

                assertThat(result).isNotNull();
                assertThat(result.getUsername()).isEqualTo("newname");
            }
        }

        @Test
        @DisplayName("输入参数：用户名已存在；预期结果：抛出ForbiddenException；测试点：用户名重复校验")
        void updateUserInfo_WithDuplicateUsername_ShouldThrowException() {
            UpdateInfoDTO dto = new UpdateInfoDTO();
            dto.setUsername("existing");

            UserDO user = new UserDO();
            user.setId(1);

            try (var mockedStatic = mockStatic(LocalUser.class)) {
                mockedStatic.when(LocalUser::getLocalUser).thenReturn(user);
                when(userMapper.selectCountByUsername(eq("existing"))).thenReturn(1);

                assertThrows(ForbiddenException.class, () -> userService.updateUserInfo(dto));
            }
        }
    }

    @Nested
    @DisplayName("密码修改测试")
    class PasswordChangeTests {

        @Test
        @DisplayName("输入参数：正确的旧密码；预期结果：返回用户；测试点：修改密码成功")
        void changeUserPassword_WithCorrectOldPassword_ShouldReturnUser() {
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword("oldpass");
            dto.setNewPassword("newpass");
            dto.setConfirmPassword("newpass");

            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            try (var mockedStatic = mockStatic(LocalUser.class)) {
                mockedStatic.when(LocalUser::getLocalUser).thenReturn(user);
                when(userIdentityService.verifyUsernamePassword(eq(1), eq("testuser"), eq("oldpass"))).thenReturn(true);
                when(userIdentityService.changePassword(eq(1), eq("newpass"))).thenReturn(true);

                UserDO result = userService.changeUserPassword(dto);

                assertThat(result).isNotNull();
            }
        }

        @Test
        @DisplayName("输入参数：错误的旧密码；预期结果：抛出ParameterException；测试点：旧密码错误场景")
        void changeUserPassword_WithWrongOldPassword_ShouldThrowException() {
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword("wrongpass");
            dto.setNewPassword("newpass");

            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            try (var mockedStatic = mockStatic(LocalUser.class)) {
                mockedStatic.when(LocalUser::getLocalUser).thenReturn(user);
                when(userIdentityService.verifyUsernamePassword(eq(1), eq("testuser"), eq("wrongpass"))).thenReturn(false);

                assertThrows(ParameterException.class, () -> userService.changeUserPassword(dto));
            }
        }

        @Test
        @DisplayName("输入参数：密码更新失败；预期结果：抛出FailedException；测试点：密码更新失败场景")
        void changeUserPassword_WithUpdateFailure_ShouldThrowException() {
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword("oldpass");
            dto.setNewPassword("newpass");

            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            try (var mockedStatic = mockStatic(LocalUser.class)) {
                mockedStatic.when(LocalUser::getLocalUser).thenReturn(user);
                when(userIdentityService.verifyUsernamePassword(eq(1), eq("testuser"), eq("oldpass"))).thenReturn(true);
                when(userIdentityService.changePassword(eq(1), eq("newpass"))).thenReturn(false);

                assertThrows(FailedException.class, () -> userService.changeUserPassword(dto));
            }
        }
    }

    @Nested
    @DisplayName("验证码测试")
    class CaptchaTests {

        @Test
        @DisplayName("输入参数：null验证码；预期结果：返回false；测试点：null验证码处理")
        void verifyCaptcha_WithNullCaptcha_ShouldReturnFalse() {
            boolean result = userService.verifyCaptcha(null, "tag");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回LoginCaptchaVO；测试点：生成验证码")
        void generateCaptcha_ShouldReturnCaptchaVO() throws Exception {
            when(captchaConfig.getSecret()).thenReturn("1234567890123456");
            when(captchaConfig.getIv()).thenReturn("1234567890123456");

            assertThat(userService.generateCaptcha()).isNotNull();
        }
    }
}
