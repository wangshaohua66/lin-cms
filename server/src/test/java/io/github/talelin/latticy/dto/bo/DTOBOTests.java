package io.github.talelin.latticy.dto.bo;

import io.github.talelin.latticy.bo.FileBO;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.dto.admin.DispatchPermissionDTO;
import io.github.talelin.latticy.dto.admin.DispatchPermissionsDTO;
import io.github.talelin.latticy.dto.admin.NewGroupDTO;
import io.github.talelin.latticy.dto.admin.QueryUsersDTO;
import io.github.talelin.latticy.dto.admin.RemovePermissionsDTO;
import io.github.talelin.latticy.dto.admin.ResetPasswordDTO;
import io.github.talelin.latticy.dto.admin.UpdateGroupDTO;
import io.github.talelin.latticy.dto.admin.UpdateUserInfoDTO;
import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.dto.log.QueryLogDTO;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.LoginDTO;
import io.github.talelin.latticy.dto.user.RegisterDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BO和DTO数据类单元测试")
class DTOBOTests {

    @Nested
    @DisplayName("BO业务对象测试")
    class BusinessObjectTests {

        @Test
        @DisplayName("输入参数：FileBO属性；预期结果：getter/setter正常工作；测试点：FileBO数据类测试")
        void fileBO_GettersAndSetters_ShouldWorkCorrectly() {
            FileBO bo = new FileBO();
            bo.setId(1);
            bo.setKey("test_key");
            bo.setPath("/files/test.jpg");
            bo.setUrl("http://example.com/test.jpg");

            assertThat(bo.getId()).isEqualTo(1);
            assertThat(bo.getKey()).isEqualTo("test_key");
            assertThat(bo.getPath()).isEqualTo("/files/test.jpg");
            assertThat(bo.getUrl()).isEqualTo("http://example.com/test.jpg");
        }

        @Test
        @DisplayName("输入参数：GroupDO和权限列表；预期结果：属性正确拷贝；测试点：GroupPermissionBO构造测试")
        void groupPermissionBO_WithGroupAndPermissions_ShouldCopyProperties() {
            GroupDO group = new GroupDO();
            group.setId(1);
            group.setName("admin");
            group.setInfo("Administrator");
            PermissionDO perm = new PermissionDO();
            perm.setId(1);
            perm.setName("create user");

            GroupPermissionBO bo = new GroupPermissionBO(group, Collections.singletonList(perm));

            assertThat(bo.getId()).isEqualTo(1);
            assertThat(bo.getName()).isEqualTo("admin");
            assertThat(bo.getInfo()).isEqualTo("Administrator");
            assertThat(bo.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("输入参数：空参数构造；预期结果：对象创建成功；测试点：GroupPermissionBO无参构造")
        void groupPermissionBO_NoArgsConstructor_ShouldCreateObject() {
            GroupPermissionBO bo = new GroupPermissionBO();
            bo.setId(1);
            bo.setName("test");

            assertThat(bo.getId()).isEqualTo(1);
            assertThat(bo.getName()).isEqualTo("test");
        }

    }

    @Nested
    @DisplayName("DTO数据传输对象测试")
    class DataTransferObjectTests {

        @Test
        @DisplayName("输入参数：CreateOrUpdateBookDTO属性；预期结果：getter/setter正常工作；测试点：图书DTO测试")
        void createOrUpdateBookDTO_GettersAndSetters_ShouldWorkCorrectly() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("Spring Boot");
            dto.setAuthor("Author");
            dto.setSummary("Test summary");
            dto.setImage("cover.jpg");

            assertThat(dto.getTitle()).isEqualTo("Spring Boot");
            assertThat(dto.getAuthor()).isEqualTo("Author");
        }

        @Test
        @DisplayName("输入参数：LoginDTO属性；预期结果：getter/setter正常工作；测试点：登录DTO测试")
        void loginDTO_GettersAndSetters_ShouldWorkCorrectly() {
            LoginDTO dto = new LoginDTO();
            dto.setUsername("admin");
            dto.setPassword("password123");
            dto.setCaptcha("123456");

            assertThat(dto.getUsername()).isEqualTo("admin");
            assertThat(dto.getPassword()).isEqualTo("password123");
            assertThat(dto.getCaptcha()).isEqualTo("123456");
        }

        @Test
        @DisplayName("输入参数：RegisterDTO属性；预期结果：getter/setter正常工作；测试点：注册DTO测试")
        void registerDTO_GettersAndSetters_ShouldWorkCorrectly() {
            RegisterDTO dto = new RegisterDTO();
            dto.setUsername("newuser");
            dto.setPassword("password");
            dto.setEmail("test@example.com");
            dto.setGroupIds(Arrays.asList(1, 2, 3));

            assertThat(dto.getUsername()).isEqualTo("newuser");
            assertThat(dto.getEmail()).isEqualTo("test@example.com");
            assertThat(dto.getGroupIds()).hasSize(3);
        }

        @Test
        @DisplayName("输入参数：ChangePasswordDTO属性；预期结果：getter/setter正常工作；测试点：修改密码DTO测试")
        void changePasswordDTO_GettersAndSetters_ShouldWorkCorrectly() {
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword("oldpass");
            dto.setNewPassword("newpass");
            dto.setConfirmPassword("newpass");

            assertThat(dto.getOldPassword()).isEqualTo("oldpass");
            assertThat(dto.getNewPassword()).isEqualTo("newpass");
            assertThat(dto.getConfirmPassword()).isEqualTo("newpass");
        }

        @Test
        @DisplayName("输入参数：UpdateInfoDTO属性；预期结果：getter/setter正常工作；测试点：更新信息DTO测试")
        void updateInfoDTO_GettersAndSetters_ShouldWorkCorrectly() {
            UpdateInfoDTO dto = new UpdateInfoDTO();
            dto.setNickname("New Nickname");
            dto.setAvatar("avatar.jpg");
            dto.setEmail("new@example.com");

            assertThat(dto.getNickname()).isEqualTo("New Nickname");
            assertThat(dto.getEmail()).isEqualTo("new@example.com");
        }

        @Test
        @DisplayName("输入参数：NewGroupDTO属性；预期结果：getter/setter正常工作；测试点：创建分组DTO测试")
        void newGroupDTO_GettersAndSetters_ShouldWorkCorrectly() {
            NewGroupDTO dto = new NewGroupDTO();
            dto.setName("newgroup");
            dto.setInfo("Test group");
            dto.setPermissionIds(Arrays.asList(1, 2));

            assertThat(dto.getName()).isEqualTo("newgroup");
            assertThat(dto.getInfo()).isEqualTo("Test group");
            assertThat(dto.getPermissionIds()).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：UpdateGroupDTO属性；预期结果：getter/setter正常工作；测试点：更新分组DTO测试")
        void updateGroupDTO_GettersAndSetters_ShouldWorkCorrectly() {
            UpdateGroupDTO dto = new UpdateGroupDTO();
            dto.setName("updated");
            dto.setInfo("Updated info");

            assertThat(dto.getName()).isEqualTo("updated");
            assertThat(dto.getInfo()).isEqualTo("Updated info");
        }

        @Test
        @DisplayName("输入参数：DispatchPermissionsDTO属性；预期结果：getter/setter正常工作；测试点：分配权限DTO测试")
        void dispatchPermissionsDTO_GettersAndSetters_ShouldWorkCorrectly() {
            DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
            dto.setGroupId(1);
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            assertThat(dto.getGroupId()).isEqualTo(1);
            assertThat(dto.getPermissionIds()).hasSize(3);
        }

        @Test
        @DisplayName("输入参数：DispatchPermissionDTO属性；预期结果：getter/setter正常工作；测试点：分配单个权限DTO测试")
        void dispatchPermissionDTO_GettersAndSetters_ShouldWorkCorrectly() {
            DispatchPermissionDTO dto = new DispatchPermissionDTO();
            dto.setGroupId(1);
            dto.setPermissionId(5);

            assertThat(dto.getGroupId()).isEqualTo(1);
            assertThat(dto.getPermissionId()).isEqualTo(5);
        }

        @Test
        @DisplayName("输入参数：RemovePermissionsDTO属性；预期结果：getter/setter正常工作；测试点：移除权限DTO测试")
        void removePermissionsDTO_GettersAndSetters_ShouldWorkCorrectly() {
            RemovePermissionsDTO dto = new RemovePermissionsDTO();
            dto.setPermissionIds(Arrays.asList(1, 2));

            assertThat(dto.getPermissionIds()).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：ResetPasswordDTO属性；预期结果：getter/setter正常工作；测试点：重置密码DTO测试")
        void resetPasswordDTO_GettersAndSetters_ShouldWorkCorrectly() {
            ResetPasswordDTO dto = new ResetPasswordDTO();
            dto.setNewPassword("newpass");
            dto.setConfirmPassword("newpass");

            assertThat(dto.getNewPassword()).isEqualTo("newpass");
        }

        @Test
        @DisplayName("输入参数：UpdateUserInfoDTO属性；预期结果：getter/setter正常工作；测试点：更新用户信息DTO测试")
        void updateUserInfoDTO_GettersAndSetters_ShouldWorkCorrectly() {
            UpdateUserInfoDTO dto = new UpdateUserInfoDTO();
            dto.setGroupIds(Arrays.asList(1, 2));

            assertThat(dto.getGroupIds()).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：QueryUsersDTO属性；预期结果：getter/setter正常工作；测试点：查询用户DTO测试")
        void queryUsersDTO_GettersAndSetters_ShouldWorkCorrectly() {
            QueryUsersDTO dto = new QueryUsersDTO();
            dto.setGroupId(1);

            assertThat(dto.getGroupId()).isEqualTo(1);
        }

        @Test
        @DisplayName("输入参数：QueryLogDTO属性；预期结果：getter/setter正常工作；测试点：查询日志DTO测试")
        void queryLogDTO_GettersAndSetters_ShouldWorkCorrectly() {
            QueryLogDTO dto = new QueryLogDTO();
            dto.setName("admin");
            dto.setStart(new Date());
            dto.setEnd(new Date());
            dto.setKeyword("test");

            assertThat(dto.getName()).isEqualTo("admin");
            assertThat(dto.getStart()).isNotNull();
            assertThat(dto.getKeyword()).isEqualTo("test");
        }
    }
}
