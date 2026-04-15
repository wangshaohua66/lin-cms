package io.github.talelin.latticy.dto;

import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.dto.log.QueryLogDTO;
import io.github.talelin.latticy.dto.query.BasePageDTO;
import io.github.talelin.latticy.dto.user.*;
import io.github.talelin.latticy.dto.admin.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.*;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * DTO类单元测试
 * 测试所有DTO的getter/setter和验证注解
 */
@DisplayName("DTO类测试")
class DTOTest {

    private final Validator validator;

    public DTOTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("CreateOrUpdateBookDTO - 测试")
    void createOrUpdateBookDTO_Test() {
        CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
        dto.setTitle("测试标题");
        dto.setAuthor("测试作者");
        dto.setSummary("测试摘要");
        dto.setImage("test.jpg");

        assertThat(dto.getTitle()).isEqualTo("测试标题");
        assertThat(dto.getAuthor()).isEqualTo("测试作者");
        assertThat(dto.getSummary()).isEqualTo("测试摘要");
        assertThat(dto.getImage()).isEqualTo("test.jpg");

        Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("CreateOrUpdateBookDTO - 空标题验证失败")
    void createOrUpdateBookDTO_WithEmptyTitle_ShouldFail() {
        CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
        dto.setTitle("");
        dto.setAuthor("作者");

        Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("LoginDTO - 测试")
    void loginDTO_Test() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("testuser");
        dto.setPassword("password123");
        dto.setCaptcha("1234");

        assertThat(dto.getUsername()).isEqualTo("testuser");
        assertThat(dto.getPassword()).isEqualTo("password123");
        assertThat(dto.getCaptcha()).isEqualTo("1234");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(dto);
        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("LoginDTO - 空用户名验证失败")
    void loginDTO_WithEmptyUsername_ShouldFail() {
        LoginDTO dto = new LoginDTO();
        dto.setUsername("");
        dto.setPassword("password");

        Set<ConstraintViolation<LoginDTO>> violations = validator.validate(dto);
        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("RegisterDTO - 测试")
    void registerDTO_Test() {
        RegisterDTO dto = new RegisterDTO();
        dto.setUsername("newuser");
        dto.setPassword("password123");
        dto.setConfirmPassword("password123");
        dto.setEmail("test@example.com");

        assertThat(dto.getUsername()).isEqualTo("newuser");
        assertThat(dto.getPassword()).isEqualTo("password123");
        assertThat(dto.getConfirmPassword()).isEqualTo("password123");
        assertThat(dto.getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("UpdateInfoDTO - 测试")
    void updateInfoDTO_Test() {
        UpdateInfoDTO dto = new UpdateInfoDTO();
        dto.setNickname("新昵称");
        dto.setEmail("new@example.com");

        assertThat(dto.getNickname()).isEqualTo("新昵称");
        assertThat(dto.getEmail()).isEqualTo("new@example.com");
    }

    @Test
    @DisplayName("ChangePasswordDTO - 测试")
    void changePasswordDTO_Test() {
        ChangePasswordDTO dto = new ChangePasswordDTO();
        dto.setOldPassword("oldpass");
        dto.setNewPassword("newpass");
        dto.setConfirmPassword("newpass");

        assertThat(dto.getOldPassword()).isEqualTo("oldpass");
        assertThat(dto.getNewPassword()).isEqualTo("newpass");
        assertThat(dto.getConfirmPassword()).isEqualTo("newpass");
    }

    @Test
    @DisplayName("BasePageDTO - 测试")
    void basePageDTO_Test() {
        BasePageDTO dto = new BasePageDTO();
        dto.setPage(1);
        dto.setCount(10);

        assertThat(dto.getPage()).isEqualTo(1);
        assertThat(dto.getCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("QueryLogDTO - 测试")
    void queryLogDTO_Test() {
        QueryLogDTO dto = new QueryLogDTO();
        dto.setName("testuser");
        dto.setKeyword("test");

        assertThat(dto.getName()).isEqualTo("testuser");
        assertThat(dto.getKeyword()).isEqualTo("test");
    }

    @Test
    @DisplayName("NewGroupDTO - 测试")
    void newGroupDTO_Test() {
        NewGroupDTO dto = new NewGroupDTO();
        dto.setName("测试分组");
        dto.setInfo("分组信息");

        assertThat(dto.getName()).isEqualTo("测试分组");
        assertThat(dto.getInfo()).isEqualTo("分组信息");
    }

    @Test
    @DisplayName("UpdateGroupDTO - 测试")
    void updateGroupDTO_Test() {
        UpdateGroupDTO dto = new UpdateGroupDTO();
        dto.setName("更新分组");
        dto.setInfo("更新信息");

        assertThat(dto.getName()).isEqualTo("更新分组");
        assertThat(dto.getInfo()).isEqualTo("更新信息");
    }

    @Test
    @DisplayName("DispatchPermissionsDTO - 测试")
    void dispatchPermissionsDTO_Test() {
        DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
        dto.setGroupId(1);
        dto.setPermissionIds(java.util.Arrays.asList(1, 2, 3));

        assertThat(dto.getGroupId()).isEqualTo(1);
        assertThat(dto.getPermissionIds()).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("RemovePermissionsDTO - 测试")
    void removePermissionsDTO_Test() {
        RemovePermissionsDTO dto = new RemovePermissionsDTO();
        dto.setGroupId(1);
        dto.setPermissionIds(java.util.Arrays.asList(1, 2));

        assertThat(dto.getGroupId()).isEqualTo(1);
        assertThat(dto.getPermissionIds()).containsExactly(1, 2);
    }

    @Test
    @DisplayName("ResetPasswordDTO - 测试")
    void resetPasswordDTO_Test() {
        ResetPasswordDTO dto = new ResetPasswordDTO();
        dto.setNewPassword("newpass");

        assertThat(dto.getNewPassword()).isEqualTo("newpass");
    }

    @Test
    @DisplayName("QueryUsersDTO - 测试")
    void queryUsersDTO_Test() {
        QueryUsersDTO dto = new QueryUsersDTO();
        dto.setGroupId(1);

        assertThat(dto.getGroupId()).isEqualTo(1);
    }

    @Test
    @DisplayName("UpdateUserInfoDTO - 测试")
    void updateUserInfoDTO_Test() {
        UpdateUserInfoDTO dto = new UpdateUserInfoDTO();
        dto.setGroupIds(java.util.Arrays.asList(1, 2));

        assertThat(dto.getGroupIds()).containsExactly(1, 2);
    }

    @Test
    @DisplayName("DispatchPermissionDTO - 测试")
    void dispatchPermissionDTO_Test() {
        DispatchPermissionDTO dto = new DispatchPermissionDTO();
        dto.setGroupId(1);
        dto.setPermissionId(2);

        assertThat(dto.getGroupId()).isEqualTo(1);
        assertThat(dto.getPermissionId()).isEqualTo(2);
    }
}
