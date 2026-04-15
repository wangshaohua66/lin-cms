package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.talelin.latticy.common.constant.IdentityConstant;
import io.github.talelin.latticy.mapper.UserIdentityMapper;
import io.github.talelin.latticy.model.UserIdentityDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

/**
 * UserIdentityServiceImpl 单元测试
 * 测试点：用户身份认证相关操作
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("用户身份服务实现类测试")
class UserIdentityServiceImplTest {

    @Mock
    private UserIdentityMapper userIdentityMapper;

    @InjectMocks
    private UserIdentityServiceImpl userIdentityService;

    private UserIdentityDO userIdentity;
    private static final Integer USER_ID = 1;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password123";

    @BeforeEach
    void setUp() {
        userIdentity = new UserIdentityDO();
        userIdentity.setId(1);
        userIdentity.setUserId(USER_ID);
        userIdentity.setIdentityType(IdentityConstant.USERNAME_PASSWORD_IDENTITY);
        userIdentity.setIdentifier(USERNAME);
        userIdentity.setCredential("$2a$10$encryptedpassword");
    }

    @Test
    @DisplayName("创建身份 - 使用参数创建")
    void createIdentity_WithParams_ShouldReturnIdentity() {
        // 输入参数：用户ID、身份类型、标识、凭证
        // 预期结果：返回创建的身份对象
        // 测试点：验证身份创建逻辑
        when(userIdentityMapper.insert(any(UserIdentityDO.class))).thenReturn(1);

        UserIdentityDO result = userIdentityService.createIdentity(
                USER_ID, IdentityConstant.USERNAME_PASSWORD_IDENTITY, USERNAME, PASSWORD);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(USER_ID);
        assertThat(result.getIdentityType()).isEqualTo(IdentityConstant.USERNAME_PASSWORD_IDENTITY);
        assertThat(result.getIdentifier()).isEqualTo(USERNAME);
        assertThat(result.getCredential()).isEqualTo(PASSWORD);
        verify(userIdentityMapper).insert(any(UserIdentityDO.class));
    }

    @Test
    @DisplayName("创建身份 - null参数处理")
    void createIdentity_WithNullParams_ShouldHandleNull() {
        // 输入参数：null凭证
        // 预期结果：正常处理，不抛出异常
        // 测试点：验证null值的处理
        when(userIdentityMapper.insert(any(UserIdentityDO.class))).thenReturn(1);

        UserIdentityDO result = userIdentityService.createIdentity(
                USER_ID, IdentityConstant.USERNAME_PASSWORD_IDENTITY, USERNAME, null);

        assertThat(result).isNotNull();
        assertThat(result.getCredential()).isNull();
    }

    @Test
    @DisplayName("创建身份 - 使用对象创建")
    void createIdentity_WithObject_ShouldReturnIdentity() {
        // 输入参数：UserIdentityDO对象
        // 预期结果：返回创建的身份对象
        // 测试点：验证对象创建逻辑
        when(userIdentityMapper.insert(userIdentity)).thenReturn(1);

        UserIdentityDO result = userIdentityService.createIdentity(userIdentity);

        assertThat(result).isEqualTo(userIdentity);
        verify(userIdentityMapper).insert(userIdentity);
    }

    @Test
    @DisplayName("创建用户名密码身份 - 正常情况")
    void createUsernamePasswordIdentity_WithValidParams_ShouldReturnIdentity() {
        // 输入参数：用户ID、用户名、密码
        // 预期结果：返回创建的身份对象，密码已加密
        // 测试点：验证用户名密码身份创建，验证密码加密
        when(userIdentityMapper.insert(any(UserIdentityDO.class))).thenReturn(1);

        UserIdentityDO result = userIdentityService.createUsernamePasswordIdentity(USER_ID, USERNAME, PASSWORD);

        assertThat(result).isNotNull();
        assertThat(result.getUserId()).isEqualTo(USER_ID);
        assertThat(result.getIdentityType()).isEqualTo(IdentityConstant.USERNAME_PASSWORD_IDENTITY);
        assertThat(result.getIdentifier()).isEqualTo(USERNAME);
        // 验证密码已加密（BCrypt加密后的密码以$2a$开头）
        assertThat(result.getCredential()).startsWith("$2a$");
        verify(userIdentityMapper).insert(any(UserIdentityDO.class));
    }

    @Test
    @DisplayName("创建用户名密码身份 - 空密码")
    void createUsernamePasswordIdentity_WithEmptyPassword_ShouldHandleEmpty() {
        // 输入参数：空密码
        // 预期结果：正常处理，密码为空字符串
        // 测试点：验证空密码处理
        when(userIdentityMapper.insert(any(UserIdentityDO.class))).thenReturn(1);

        UserIdentityDO result = userIdentityService.createUsernamePasswordIdentity(USER_ID, USERNAME, "");

        assertThat(result).isNotNull();
        assertThat(result.getCredential()).isNotEmpty(); // 空字符串也会被加密
    }

    @Test
    @DisplayName("验证用户名密码 - 正确密码")
    void verifyUsernamePassword_WithCorrectPassword_ShouldReturnTrue() {
        // 输入参数：用户ID、用户名、正确密码
        // 预期结果：返回true
        // 测试点：验证密码验证逻辑
        String correctPassword = "password123";
        // 创建一个真实的加密密码
        String encryptedPassword = io.github.talelin.latticy.common.util.EncryptUtil.encrypt(correctPassword);
        
        UserIdentityDO identity = new UserIdentityDO();
        identity.setId(1);
        identity.setUserId(USER_ID);
        identity.setIdentityType(IdentityConstant.USERNAME_PASSWORD_IDENTITY);
        identity.setIdentifier(USERNAME);
        identity.setCredential(encryptedPassword);
        
        when(userIdentityMapper.selectOne(any(QueryWrapper.class))).thenReturn(identity);

        boolean result = userIdentityService.verifyUsernamePassword(USER_ID, USERNAME, correctPassword);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("验证用户名密码 - 错误密码")
    void verifyUsernamePassword_WithWrongPassword_ShouldReturnFalse() {
        // 输入参数：用户ID、用户名、错误密码
        // 预期结果：返回false
        // 测试点：验证错误密码处理
        String correctPassword = "password123";
        String wrongPassword = "wrongpassword";
        String encryptedPassword = io.github.talelin.latticy.common.util.EncryptUtil.encrypt(correctPassword);
        
        UserIdentityDO identity = new UserIdentityDO();
        identity.setId(1);
        identity.setUserId(USER_ID);
        identity.setIdentityType(IdentityConstant.USERNAME_PASSWORD_IDENTITY);
        identity.setIdentifier(USERNAME);
        identity.setCredential(encryptedPassword);
        
        when(userIdentityMapper.selectOne(any(QueryWrapper.class))).thenReturn(identity);

        boolean result = userIdentityService.verifyUsernamePassword(USER_ID, USERNAME, wrongPassword);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("验证用户名密码 - 身份不存在抛出异常")
    void verifyUsernamePassword_WithNonExistingIdentity_ShouldThrowException() {
        // 输入参数：不存在的用户身份
        // 预期结果：抛出NullPointerException
        // 测试点：验证身份不存在时的处理
        when(userIdentityMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        assertThatThrownBy(() -> userIdentityService.verifyUsernamePassword(999, "nonexistent", PASSWORD))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("修改密码 - 正常情况")
    void changePassword_WithValidParams_ShouldReturnTrue() {
        // 输入参数：用户ID、新密码
        // 预期结果：返回true
        // 测试点：验证密码修改逻辑
        when(userIdentityMapper.update(any(UserIdentityDO.class), any(QueryWrapper.class))).thenReturn(1);

        boolean result = userIdentityService.changePassword(USER_ID, "newpassword123");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("修改密码 - 身份不存在")
    void changePassword_WithNonExistingIdentity_ShouldReturnFalse() {
        // 输入参数：不存在的用户ID
        // 预期结果：返回false
        // 测试点：验证身份不存在时的处理
        when(userIdentityMapper.update(any(UserIdentityDO.class), any(QueryWrapper.class))).thenReturn(0);

        boolean result = userIdentityService.changePassword(999, "newpassword123");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("修改用户名 - 正常情况")
    void changeUsername_WithValidParams_ShouldReturnTrue() {
        // 输入参数：用户ID、新用户名
        // 预期结果：返回true
        // 测试点：验证用户名修改逻辑
        when(userIdentityMapper.update(any(UserIdentityDO.class), any(QueryWrapper.class))).thenReturn(1);

        boolean result = userIdentityService.changeUsername(USER_ID, "newusername");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("修改用户名 - 身份不存在")
    void changeUsername_WithNonExistingIdentity_ShouldReturnFalse() {
        // 输入参数：不存在的用户ID
        // 预期结果：返回false
        // 测试点：验证身份不存在时的处理
        when(userIdentityMapper.update(any(UserIdentityDO.class), any(QueryWrapper.class))).thenReturn(0);

        boolean result = userIdentityService.changeUsername(999, "newusername");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("修改密码 - 更新失败")
    void changePassword_WhenUpdateFails_ShouldReturnFalse() {
        // 输入参数：用户ID、新密码
        // 预期结果：返回false
        // 测试点：验证更新失败处理
        when(userIdentityMapper.update(any(UserIdentityDO.class), any(QueryWrapper.class))).thenReturn(0);

        boolean result = userIdentityService.changePassword(USER_ID, "newpassword123");

        assertThat(result).isFalse();
        verify(userIdentityMapper).update(any(UserIdentityDO.class), any(QueryWrapper.class));
    }

    @Test
    @DisplayName("创建身份 - 插入失败")
    void createIdentity_WhenInsertFails_ShouldStillReturnIdentity() {
        // 输入参数：有效参数
        // 预期结果：即使插入失败也返回对象（当前实现如此）
        // 测试点：验证插入失败处理
        when(userIdentityMapper.insert(any(UserIdentityDO.class))).thenReturn(0);

        UserIdentityDO result = userIdentityService.createIdentity(
                USER_ID, IdentityConstant.USERNAME_PASSWORD_IDENTITY, USERNAME, PASSWORD);

        assertThat(result).isNotNull();
    }
}
