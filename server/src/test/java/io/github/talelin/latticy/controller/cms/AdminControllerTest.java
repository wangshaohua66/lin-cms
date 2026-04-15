package io.github.talelin.latticy.controller.cms;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.dto.admin.*;
import io.github.talelin.latticy.dto.query.BasePageDTO;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.AdminService;
import io.github.talelin.latticy.service.GroupService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("AdminController单元测试")
class AdminControllerTest {

    @MockBean
    private AdminService adminService;

    @MockBean
    private GroupService groupService;

    @Autowired
    private AdminController adminController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Nested
    @DisplayName("权限查询测试")
    class PermissionTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回结构化权限Map；测试点：查询所有可分配的权限")
        void getAllPermissions_ShouldReturnStructuralPermissions() {
            Map<String, List<PermissionDO>> mockMap = new HashMap<>();
            PermissionDO perm1 = new PermissionDO();
            perm1.setName("test");
            perm1.setModule("test");
            mockMap.put("test", Collections.singletonList(perm1));

            when(adminService.getAllStructuralPermissions()).thenReturn(mockMap);

            Map<String, List<PermissionDO>> result = adminController.getAllPermissions();

            assertThat(result).isNotNull();
            assertThat(result).containsKey("test");
            verify(adminService, times(1)).getAllStructuralPermissions();
        }
    }

    @Nested
    @DisplayName("用户管理测试")
    class UserManagementTests {

        @Test
        @DisplayName("输入参数：有效QueryUsersDTO；预期结果：返回分页用户列表；测试点：查询所有用户")
        void getUsers_WithValidDTO_ShouldReturnPagedUsers() {
            QueryUsersDTO dto = new QueryUsersDTO();
            dto.setGroupId(1);
            dto.setPage(1);
            dto.setCount(10);

            Page<UserDO> page = new Page<>(1, 10);
            UserDO user1 = new UserDO();
            user1.setId(1);
            user1.setUsername("user1");
            page.setRecords(Collections.singletonList(user1));

            GroupDO group = new GroupDO();
            group.setId(1);
            group.setName("group1");

            when(adminService.getUserPageByGroupId(eq(1), eq(10), eq(1))).thenReturn(page);
            when(groupService.getUserGroupsByUserId(eq(1))).thenReturn(Collections.singletonList(group));

            var result = adminController.getUsers(dto);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(1);
            verify(adminService, times(1)).getUserPageByGroupId(eq(1), eq(10), eq(1));
        }

        @Test
        @DisplayName("输入参数：用户ID、密码DTO；预期结果：返回UpdatedVO；测试点：修改用户密码")
        void changeUserPassword_WithValidParams_ShouldReturnUpdatedVO() {
            ResetPasswordDTO dto = new ResetPasswordDTO();
            dto.setNewPassword("newpass123");
            dto.setConfirmPassword("newpass123");

            when(adminService.changeUserPassword(eq(1), any(ResetPasswordDTO.class))).thenReturn(true);

            var result = adminController.changeUserPassword(1, dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(4);
            verify(adminService, times(1)).changeUserPassword(eq(1), any(ResetPasswordDTO.class));
        }

        @Test
        @DisplayName("输入参数：有效用户ID；预期结果：返回DeletedVO；测试点：删除用户")
        void deleteUser_WithValidId_ShouldReturnDeletedVO() {
            when(adminService.deleteUser(eq(1))).thenReturn(true);

            var result = adminController.deleteUser(1);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(5);
            verify(adminService, times(1)).deleteUser(eq(1));
        }

        @Test
        @DisplayName("输入参数：用户ID、更新信息DTO；预期结果：返回UpdatedVO；测试点：更新用户信息")
        void updateUser_WithValidParams_ShouldReturnUpdatedVO() {
            UpdateUserInfoDTO dto = new UpdateUserInfoDTO();
            dto.setGroupIds(List.of(1, 2));

            when(adminService.updateUserInfo(eq(1), any(UpdateUserInfoDTO.class))).thenReturn(true);

            var result = adminController.updateUser(1, dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(6);
            verify(adminService, times(1)).updateUserInfo(eq(1), any(UpdateUserInfoDTO.class));
        }
    }

    @Nested
    @DisplayName("分组管理测试")
    class GroupManagementTests {

        @Test
        @DisplayName("输入参数：分页DTO；预期结果：返回分组分页结果；测试点：查询所有权限组")
        void getGroups_WithValidDTO_ShouldReturnPagedGroups() {
            BasePageDTO dto = new BasePageDTO();
            dto.setPage(1);
            dto.setCount(10);

            Page<GroupDO> page = new Page<>(1, 10);
            GroupDO group1 = new GroupDO();
            group1.setId(1);
            group1.setName("group1");
            page.setRecords(Collections.singletonList(group1));

            when(adminService.getGroupPage(eq(1), eq(10))).thenReturn(page);

            var result = adminController.getGroups(dto);

            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(page.getTotal());
            verify(adminService, times(1)).getGroupPage(eq(1), eq(10));
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回所有分组列表；测试点：查询所有权限组(不分页)")
        void getAllGroup_ShouldReturnAllGroups() {
            GroupDO group1 = new GroupDO();
            group1.setId(1);
            group1.setName("group1");
            GroupDO group2 = new GroupDO();
            group2.setId(2);
            group2.setName("group2");

            when(adminService.getAllGroups()).thenReturn(Arrays.asList(group1, group2));

            List<GroupDO> result = adminController.getAllGroup();

            assertThat(result).hasSize(2);
            verify(adminService, times(1)).getAllGroups();
        }

        @Test
        @DisplayName("输入参数：有效分组ID；预期结果：返回GroupPermissionBO；测试点：查询单个权限组及其权限")
        void getGroup_WithValidId_ShouldReturnGroupWithPermissions() {
            GroupPermissionBO bo = new GroupPermissionBO();
            bo.setId(1);
            bo.setName("admin");
            bo.setPermissions(new ArrayList<>());

            when(adminService.getGroup(eq(1))).thenReturn(bo);

            GroupPermissionBO result = adminController.getGroup(1);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1);
            assertThat(result.getName()).isEqualTo("admin");
            verify(adminService, times(1)).getGroup(eq(1));
        }

        @Test
        @DisplayName("输入参数：新建分组DTO；预期结果：返回CreatedVO；测试点：新建权限组")
        void createGroup_WithValidDTO_ShouldReturnCreatedVO() {
            NewGroupDTO dto = new NewGroupDTO();
            dto.setName("newgroup");
            dto.setInfo("test group");
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            when(adminService.createGroup(any(NewGroupDTO.class))).thenReturn(true);

            var result = adminController.createGroup(dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(15);
            verify(adminService, times(1)).createGroup(any(NewGroupDTO.class));
        }

        @Test
        @DisplayName("输入参数：分组ID、更新DTO；预期结果：返回UpdatedVO；测试点：更新权限组")
        void updateGroup_WithValidParams_ShouldReturnUpdatedVO() {
            UpdateGroupDTO dto = new UpdateGroupDTO();
            dto.setName("updated");
            dto.setInfo("updated info");

            when(adminService.updateGroup(eq(1), any(UpdateGroupDTO.class))).thenReturn(true);

            var result = adminController.updateGroup(1, dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(7);
            verify(adminService, times(1)).updateGroup(eq(1), any(UpdateGroupDTO.class));
        }

        @Test
        @DisplayName("输入参数：有效分组ID；预期结果：返回DeletedVO；测试点：删除权限组")
        void deleteGroup_WithValidId_ShouldReturnDeletedVO() {
            when(adminService.deleteGroup(eq(1))).thenReturn(true);

            var result = adminController.deleteGroup(1);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(8);
            verify(adminService, times(1)).deleteGroup(eq(1));
        }
    }

    @Nested
    @DisplayName("权限分配测试")
    class PermissionDispatchTests {

        @Test
        @DisplayName("输入参数：分配权限DTO；预期结果：返回CreatedVO；测试点：分配单个权限")
        void dispatchPermission_WithValidDTO_ShouldReturnCreatedVO() {
            DispatchPermissionDTO dto = new DispatchPermissionDTO();
            dto.setGroupId(1);
            dto.setPermissionId(100);

            when(adminService.dispatchPermission(any(DispatchPermissionDTO.class))).thenReturn(true);

            var result = adminController.dispatchPermission(dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(9);
            verify(adminService, times(1)).dispatchPermission(any(DispatchPermissionDTO.class));
        }

        @Test
        @DisplayName("输入参数：批量分配DTO；预期结果：返回CreatedVO；测试点：批量分配权限")
        void dispatchPermissions_WithValidDTO_ShouldReturnCreatedVO() {
            DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
            dto.setGroupId(1);
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            when(adminService.dispatchPermissions(any(DispatchPermissionsDTO.class))).thenReturn(true);

            var result = adminController.dispatchPermissions(dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(9);
            verify(adminService, times(1)).dispatchPermissions(any(DispatchPermissionsDTO.class));
        }

        @Test
        @DisplayName("输入参数：移除权限DTO；预期结果：返回DeletedVO；测试点：移除权限")
        void removePermissions_WithValidDTO_ShouldReturnDeletedVO() {
            RemovePermissionsDTO dto = new RemovePermissionsDTO();
            dto.setGroupId(1);
            dto.setPermissionIds(Arrays.asList(1, 2));

            when(adminService.removePermissions(any(RemovePermissionsDTO.class))).thenReturn(true);

            var result = adminController.removePermissions(dto);

            assertThat(result).isNotNull();
            assertThat(result.getCode()).isEqualTo(10);
            verify(adminService, times(1)).removePermissions(any(RemovePermissionsDTO.class));
        }
    }
}
