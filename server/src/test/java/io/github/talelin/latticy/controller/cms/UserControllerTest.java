package io.github.talelin.latticy.controller.cms;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.autoconfigure.exception.ParameterException;
import io.github.talelin.core.token.DoubleJWT;
import io.github.talelin.core.token.Tokens;
import io.github.talelin.latticy.common.LocalUser;
import io.github.talelin.latticy.common.configuration.LoginCaptchaProperties;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.LoginDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.service.UserIdentityService;
import io.github.talelin.latticy.service.UserService;
import io.github.talelin.latticy.vo.LoginCaptchaVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("UserController单元测试")
class UserControllerTest {

    @MockBean
    private UserService userService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private UserIdentityService userIdentityService;

    @MockBean
    private DoubleJWT jwt;

    @MockBean
    private LoginCaptchaProperties captchaConfig;

    @Autowired
    private UserController userController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Nested
    @DisplayName("用户注册测试")
    class RegisterTests {

        @Test
        @DisplayName("输入参数：有效RegisterDTO；预期结果：返回CreatedVO；测试点：管理员创建用户")
        void register_WithValidDTO_ShouldReturnCreatedVO() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("newuser");
            dto.setPassword("password123");
            dto.setEmail("test@example.com");

            when(userService.createUser(any(RegisterDTO.class))).thenReturn(new UserDO());

            var result = userController.register(dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(11);
            verify(userService, times(1)).createUser(any(RegisterDTO.class));
        }
    }

    @Nested
    @DisplayName("用户登录测试")
    class LoginTests {

        @Test
        @DisplayName("输入参数：需要验证码但未提供；预期结果：抛出ParameterException；测试点：验证码必填校验")
        void login_WithoutCaptcha_ShouldThrowException() {
            LoginDTO dto = new LoginDTO();
            dto.setUsername("testuser");
            dto.setPassword("password123");
            dto.setCaptcha("");

            when(captchaConfig.getEnabled()).thenReturn(true);

            assertThrows(ParameterException.class, () -> userController.login(dto, null));
        }

        @Test
        @DisplayName("输入参数：验证码错误；预期结果：抛出ParameterException；测试点：验证码验证失败")
        void login_WithWrongCaptcha_ShouldThrowException() {
            LoginDTO dto = new LoginDTO();
            dto.setUsername("testuser");
            dto.setPassword("password123");
            dto.setCaptcha("wrong");

            when(captchaConfig.getEnabled()).thenReturn(true);
            when(userService.verifyCaptcha(eq("wrong"), eq("tag123"))).thenReturn(false);

            assertThrows(ParameterException.class, () -> userController.login(dto, "tag123"));
        }

        @Test
        @DisplayName("输入参数：不存在的用户名；预期结果：抛出NotFoundException；测试点：用户不存在场景")
        void login_WithNonExistentUser_ShouldThrowNotFoundException() {
            LoginDTO dto = new LoginDTO();
            dto.setUsername("nonexistent");
            dto.setPassword("password123");

            when(captchaConfig.getEnabled()).thenReturn(false);
            when(userService.getUserByUsername(eq("nonexistent"))).thenReturn(null);

            assertThrows(NotFoundException.class, () -> userController.login(dto, "tag"));
        }

        @Test
        @DisplayName("输入参数：密码错误；预期结果：抛出ParameterException；测试点：密码错误场景")
        void login_WithWrongPassword_ShouldThrowParameterException() {
            LoginDTO dto = new LoginDTO();
            dto.setUsername("testuser");
            dto.setPassword("wrongpass");

            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            when(captchaConfig.getEnabled()).thenReturn(false);
            when(userService.getUserByUsername(eq("testuser"))).thenReturn(user);
            when(userIdentityService.verifyUsernamePassword(eq(1), eq("testuser"), eq("wrongpass"))).thenReturn(false);

            assertThrows(ParameterException.class, () -> userController.login(dto, "tag"));
        }
    }

    @Nested
    @DisplayName("验证码测试")
    class CaptchaTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回LoginCaptchaVO；测试点：验证码启用时生成验证码")
        void userCaptcha_WhenEnabled_ShouldReturnCaptchaVO() throws Exception {
            LoginCaptchaVO vo = new LoginCaptchaVO();
            vo.setTag("tag123");

            when(captchaConfig.getEnabled()).thenReturn(true);
            when(userService.generateCaptcha()).thenReturn(vo);

            LoginCaptchaVO result = userController.userCaptcha();

            assertThat(result).isNotNull();
            assertThat(result.getTag()).isEqualTo("tag123");
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回空LoginCaptchaVO；测试点：验证码禁用时返回空")
        void userCaptcha_WhenDisabled_ShouldReturnEmptyVO() throws Exception {
            when(captchaConfig.getEnabled()).thenReturn(false);

            LoginCaptchaVO result = userController.userCaptcha();

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("用户信息更新测试")
    class UserInfoUpdateTests {

        @Test
        @DisplayName("输入参数：有效UpdateInfoDTO；预期结果：返回UpdatedVO；测试点：更新用户基本信息")
        void update_WithValidDTO_ShouldReturnUpdatedVO() {
            UpdateInfoDTO dto = new UpdateInfoDTO();
            dto.setUsername("newname");
            dto.setEmail("new@example.com");

            when(userService.updateUserInfo(any(UpdateInfoDTO.class))).thenReturn(new UserDO());

            var result = userController.update(dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(6);
            verify(userService, times(1)).updateUserInfo(any(UpdateInfoDTO.class));
        }
    }

    @Nested
    @DisplayName("修改密码测试")
    class ChangePasswordTests {

        @Test
        @DisplayName("输入参数：有效ChangePasswordDTO；预期结果：返回UpdatedVO；测试点：修改用户密码")
        void updatePassword_WithValidDTO_ShouldReturnUpdatedVO() {
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword("oldpass");
            dto.setNewPassword("newpass");
            dto.setConfirmPassword("newpass");

            when(userService.changeUserPassword(any(ChangePasswordDTO.class))).thenReturn(new UserDO());

            var result = userController.updatePassword(dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(4);
            verify(userService, times(1)).changeUserPassword(any(ChangePasswordDTO.class));
        }
    }

    @Nested
    @DisplayName("用户权限查询测试")
    class PermissionTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回UserPermissionVO；测试点：查询当前用户权限")
        void getPermissions_ShouldReturnUserPermissionVO() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            List<Map<String, List<Map<String, String>>>> permissions = new ArrayList<>();
            Map<String, List<Map<String, String>>> module = new HashMap<>();
            module.put("user", new ArrayList<>());
            permissions.add(module);

            try (var mockedStatic = mockStatic(LocalUser.class)) {
                mockedStatic.when(LocalUser::getLocalUser).thenReturn(user);
                when(groupService.checkIsRootByUserId(eq(1))).thenReturn(true);
                when(userService.getStructuralUserPermissions(eq(1))).thenReturn(permissions);

                var result = userController.getPermissions();

                assertThat(result).isNotNull();
                assertThat(result.getAdmin()).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("用户信息查询测试")
    class InformationTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回UserInfoVO；测试点：查询当前用户信息")
        void getInformation_ShouldReturnUserInfoWithGroups() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            GroupDO group1 = new GroupDO();
            group1.setId(1);
            group1.setName("admin");

            try (var mockedStatic = mockStatic(LocalUser.class)) {
                mockedStatic.when(LocalUser::getLocalUser).thenReturn(user);
                when(groupService.getUserGroupsByUserId(eq(1))).thenReturn(Collections.singletonList(group1));

                var result = userController.getInformation();

                assertThat(result).isNotNull();
                assertThat(result.getUsername()).isEqualTo("testuser");
                assertThat(result.getGroups()).hasSize(1);
            }
        }
    }
}
