package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.constant.IdentityConstant;
import io.github.talelin.latticy.common.util.EncryptUtil;
import io.github.talelin.latticy.mapper.UserIdentityMapper;
import io.github.talelin.latticy.model.UserIdentityDO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("UserIdentityServiceImpl单元测试")
class UserIdentityServiceImplTest {

    @MockBean
    private UserIdentityMapper userIdentityMapper;

    @Autowired
    private UserIdentityServiceImpl userIdentityService;

    @Nested
    @DisplayName("创建用户身份测试")
    class CreateIdentityTests {

        @Test
        @DisplayName("输入参数：用户ID、类型、标识、凭证；预期结果：返回创建的UserIdentityDO；测试点：创建用户身份")
        void createIdentity_WithSeparateParams_ShouldReturnCreated() {
            doAnswer(invocation -> {
                UserIdentityDO arg = invocation.getArgument(0);
                arg.setId(1);
                return 1;
            }).when(userIdentityMapper).insert(any(UserIdentityDO.class));

            UserIdentityDO result = userIdentityService.createIdentity(
                    1, IdentityConstant.USERNAME_PASSWORD_IDENTITY, "testuser", "encrypted_pass"
            );

            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(1);
            assertThat(result.getIdentifier()).isEqualTo("testuser");
            verify(userIdentityMapper, times(1)).insert(any(UserIdentityDO.class));
        }

        @Test
        @DisplayName("输入参数：UserIdentityDO对象；预期结果：返回创建的对象；测试点：通过DO对象创建身份")
        void createIdentity_WithDO_ShouldReturnCreated() {
            UserIdentityDO identity = new UserIdentityDO();
            identity.setUserId(1);
            identity.setIdentifier("testuser");

            when(userIdentityMapper.insert(any(UserIdentityDO.class))).thenReturn(1);

            UserIdentityDO result = userIdentityService.createIdentity(identity);

            assertThat(result).isNotNull();
            assertThat(result.getUserId()).isEqualTo(1);
        }

        @Test
        @DisplayName("输入参数：用户ID、用户名、密码；预期结果：返回创建的身份（密码加密）；测试点：创建用户名密码身份并加密")
        void createUsernamePasswordIdentity_ShouldEncryptPassword() {
            when(userIdentityMapper.insert(any(UserIdentityDO.class))).thenReturn(1);

            UserIdentityDO result = userIdentityService.createUsernamePasswordIdentity(
                    1, "testuser", "plain_password"
            );

            assertThat(result).isNotNull();
            assertThat(result.getIdentityType()).isEqualTo(IdentityConstant.USERNAME_PASSWORD_IDENTITY);
            assertThat(result.getCredential()).isNotEqualTo("plain_password");
        }
    }

    @Nested
    @DisplayName("密码验证测试")
    class PasswordVerifyTests {

        @Test
        @DisplayName("输入参数：用户ID、用户名、正确密码；预期结果：返回true；测试点：密码验证成功")
        void verifyUsernamePassword_WithCorrectPassword_ShouldReturnTrue() {
            UserIdentityDO identity = new UserIdentityDO();
            identity.setCredential(EncryptUtil.encrypt("correct_pass"));

            when(userIdentityMapper.selectOne(any(QueryWrapper.class))).thenReturn(identity);

            boolean result = userIdentityService.verifyUsernamePassword(1, "testuser", "correct_pass");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：用户ID、用户名、错误密码；预期结果：返回false；测试点：密码验证失败")
        void verifyUsernamePassword_WithWrongPassword_ShouldReturnFalse() {
            UserIdentityDO identity = new UserIdentityDO();
            identity.setCredential(EncryptUtil.encrypt("correct_pass"));

            when(userIdentityMapper.selectOne(any(QueryWrapper.class))).thenReturn(identity);

            boolean result = userIdentityService.verifyUsernamePassword(1, "testuser", "wrong_pass");

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("修改用户信息测试")
    class ModifyUserInfoTests {

        @Test
        @DisplayName("输入参数：用户ID、新密码；预期结果：返回true；测试点：成功修改密码")
        void changePassword_ShouldReturnTrue() {
            when(userIdentityMapper.update(any(), any(QueryWrapper.class))).thenReturn(1);

            boolean result = userIdentityService.changePassword(1, "newpass");

            assertThat(result).isTrue();
            verify(userIdentityMapper, times(1)).update(any(), any(QueryWrapper.class));
        }

        @Test
        @DisplayName("输入参数：用户ID、新密码；预期结果：返回false；测试点：修改密码失败")
        void changePassword_WhenUpdateFails_ShouldReturnFalse() {
            when(userIdentityMapper.update(any(), any(QueryWrapper.class))).thenReturn(0);

            boolean result = userIdentityService.changePassword(1, "newpass");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("输入参数：用户ID、新用户名；预期结果：返回true；测试点：成功修改用户名")
        void changeUsername_ShouldReturnTrue() {
            when(userIdentityMapper.update(any(), any(QueryWrapper.class))).thenReturn(1);

            boolean result = userIdentityService.changeUsername(1, "newname");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：用户ID、新用户名和密码；预期结果：返回true；测试点：同时修改用户名密码")
        void changeUsernamePassword_ShouldReturnTrue() {
            when(userIdentityMapper.update(any(), any(QueryWrapper.class))).thenReturn(1);

            boolean result = userIdentityService.changeUsernamePassword(1, "newname", "newpass");

            assertThat(result).isTrue();
        }
    }
}
