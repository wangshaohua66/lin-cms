package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.dto.admin.*;
import io.github.talelin.latticy.mapper.GroupPermissionMapper;
import io.github.talelin.latticy.mapper.UserGroupMapper;
import io.github.talelin.latticy.model.*;
import io.github.talelin.latticy.service.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("AdminServiceImpl单元测试")
class AdminServiceImplTest {

    @MockBean
    private UserService userService;

    @MockBean
    private UserIdentityService userIdentityService;

    @MockBean
    private GroupService groupService;

    @MockBean
    private PermissionService permissionService;

    @MockBean
    private GroupPermissionMapper groupPermissionMapper;

    @MockBean
    private UserGroupMapper userGroupMapper;

    @Autowired
    private AdminServiceImpl adminService;

    @Nested
    @DisplayName("用户分页查询测试")
    class UserPageQueryTests {

        @Test
        @DisplayName("输入参数：null的groupId；预期结果：返回所有用户分页；测试点：查询所有用户（排除root）")
        void getUserPageByGroupId_WithNullGroupId_ShouldReturnAllUsers() {
            Page<UserDO> expectedPage = new Page<>();
            UserDO user1 = new UserDO();
            user1.setId(2);
            user1.setUsername("user1");
            expectedPage.setRecords(Collections.singletonList(user1));

            when(userService.getRootUserId()).thenReturn(1);
            when(userService.page(any(), any(QueryWrapper.class))).thenReturn(expectedPage);

            IPage<UserDO> result = adminService.getUserPageByGroupId(null, 10, 1);

            assertThat(result).isNotNull();
            assertThat(result.getRecords()).hasSize(1);
            verify(userService, times(1)).getRootUserId();
        }

        @Test
        @DisplayName("输入参数：有效的groupId；预期结果：返回分组用户分页；测试点：按分组查询用户")
        void getUserPageByGroupId_WithValidGroupId_ShouldReturnGroupUsers() {
            Page<UserDO> expectedPage = new Page<>();
            UserDO user = new UserDO();
            user.setId(3);
            user.setUsername("groupuser");
            expectedPage.setRecords(Collections.singletonList(user));

            when(userService.getUserPageByGroupId(any(), eq(2))).thenReturn(expectedPage);

            IPage<UserDO> result = adminService.getUserPageByGroupId(2, 10, 1);

            assertThat(result).isNotNull();
            verify(userService, times(1)).getUserPageByGroupId(any(), eq(2));
        }
    }

    @Nested
    @DisplayName("用户密码修改测试")
    class PasswordChangeTests {

        @Test
        @DisplayName("输入参数：有效用户ID、密码DTO；预期结果：返回true；测试点：成功修改用户密码")
        void changeUserPassword_WithValidUser_ShouldReturnTrue() {
            ResetPasswordDTO dto = new ResetPasswordDTO();
            dto.setNewPassword("newpass123");
            dto.setConfirmPassword("newpass123");

            when(userService.checkUserExistById(eq(1))).thenReturn(true);
            when(userIdentityService.changePassword(eq(1), eq("newpass123"))).thenReturn(true);

            boolean result = adminService.changeUserPassword(1, dto);

            assertThat(result).isTrue();
            verify(userIdentityService, times(1)).changePassword(eq(1), eq("newpass123"));
        }

        @Test
        @DisplayName("输入参数：无效用户ID；预期结果：抛出NotFoundException；测试点：用户不存在时修改密码")
        void changeUserPassword_WithNonExistentUser_ShouldThrowNotFoundException() {
            ResetPasswordDTO dto = new ResetPasswordDTO();
            dto.setNewPassword("newpass123");

            when(userService.checkUserExistById(eq(999))).thenReturn(false);

            assertThrows(NotFoundException.class, () -> adminService.changeUserPassword(999, dto));
        }
    }

    @Nested
    @DisplayName("删除用户测试")
    class DeleteUserTests {

        @Test
        @DisplayName("输入参数：有效的普通用户ID；预期结果：返回true；测试点：成功删除普通用户")
        void deleteUser_WithValidUser_ShouldReturnTrue() {
            when(userService.checkUserExistById(eq(2))).thenReturn(true);
            when(userService.getRootUserId()).thenReturn(1);
            when(userService.removeById(eq(2))).thenReturn(true);
            when(userGroupMapper.deleteByUserId(eq(2))).thenReturn(2);
            when(userIdentityService.remove(any(QueryWrapper.class))).thenReturn(true);

            boolean result = adminService.deleteUser(2);

            assertThat(result).isTrue();
            verify(userService, times(1)).removeById(eq(2));
            verify(userGroupMapper, times(1)).deleteByUserId(eq(2));
        }

        @Test
        @DisplayName("输入参数：root用户ID；预期结果：抛出ForbiddenException；测试点：禁止删除root用户")
        void deleteUser_WithRootUser_ShouldThrowForbiddenException() {
            when(userService.checkUserExistById(eq(1))).thenReturn(true);
            when(userService.getRootUserId()).thenReturn(1);

            assertThrows(ForbiddenException.class, () -> adminService.deleteUser(1));
        }

        @Test
        @DisplayName("输入参数：不存在的用户ID；预期结果：抛出NotFoundException；测试点：删除不存在的用户")
        void deleteUser_WithNonExistentUser_ShouldThrowNotFoundException() {
            when(userService.checkUserExistById(eq(999))).thenReturn(false);

            assertThrows(NotFoundException.class, () -> adminService.deleteUser(999));
        }
    }

    @Nested
    @DisplayName("更新用户信息测试")
    class UpdateUserInfoTests {

        @Test
        @DisplayName("输入参数：用户ID、包含ROOT分组的DTO；预期结果：抛出ForbiddenException；测试点：禁止分配ROOT分组")
        void updateUserInfo_WithRootGroup_ShouldThrowForbiddenException() {
            UpdateUserInfoDTO dto = new UpdateUserInfoDTO();
            dto.setGroupIds(Arrays.asList(1, 2));

            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);

            assertThrows(ForbiddenException.class, () -> adminService.updateUserInfo(1, dto));
        }

        @Test
        @DisplayName("输入参数：用户ID、正常分组DTO；预期结果：返回true；测试点：成功更新用户分组")
        void updateUserInfo_WithValidGroups_ShouldReturnTrue() {
            UpdateUserInfoDTO dto = new UpdateUserInfoDTO();
            dto.setGroupIds(Arrays.asList(2, 3));

            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(groupService.getUserGroupIdsByUserId(eq(1))).thenReturn(Arrays.asList(2, 4));
            when(groupService.deleteUserGroupRelations(eq(1), eq(Arrays.asList(4)))).thenReturn(true);
            when(groupService.addUserGroupRelations(eq(1), eq(Arrays.asList(3)))).thenReturn(true);

            boolean result = adminService.updateUserInfo(1, dto);

            assertThat(result).isTrue();
            verify(groupService, times(1)).deleteUserGroupRelations(eq(1), anyList());
            verify(groupService, times(1)).addUserGroupRelations(eq(1), anyList());
        }

        @Test
        @DisplayName("输入参数：用户ID、空分组DTO；预期结果：删除所有现有分组；测试点：清空用户分组")
        void updateUserInfo_WithEmptyGroups_ShouldDeleteAllGroups() {
            UpdateUserInfoDTO dto = new UpdateUserInfoDTO();
            dto.setGroupIds(Collections.emptyList());

            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(groupService.getUserGroupIdsByUserId(eq(1))).thenReturn(Arrays.asList(2, 3));
            when(groupService.deleteUserGroupRelations(eq(1), anyList())).thenReturn(true);
            when(groupService.addUserGroupRelations(eq(1), anyList())).thenReturn(true);

            boolean result = adminService.updateUserInfo(1, dto);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("分组管理测试")
    class GroupManagementTests {

        @Test
        @DisplayName("输入参数：分页参数；预期结果：返回分组分页；测试点：查询分组分页列表")
        void getGroupPage_ShouldReturnPagedGroups() {
            Page<GroupDO> page = new Page<>();
            GroupDO group = new GroupDO();
            group.setId(2);
            group.setName("test");
            page.setRecords(Collections.singletonList(group));

            when(groupService.getGroupPage(eq(1), eq(10))).thenReturn(page);

            IPage<GroupDO> result = adminService.getGroupPage(1, 10);

            assertThat(result).isNotNull();
            verify(groupService, times(1)).getGroupPage(eq(1), eq(10));
        }

        @Test
        @DisplayName("输入参数：有效分组ID；预期结果：返回GroupPermissionBO；测试点：查询单个分组及权限")
        void getGroup_WithValidId_ShouldReturnGroupWithPermissions() {
            GroupPermissionBO bo = new GroupPermissionBO();
            bo.setId(2);
            bo.setName("admin");

            when(groupService.checkGroupExistById(eq(2))).thenReturn(true);
            when(groupService.getGroupAndPermissions(eq(2))).thenReturn(bo);

            GroupPermissionBO result = adminService.getGroup(2);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(2);
        }

        @Test
        @DisplayName("输入参数：不存在的分组ID；预期结果：抛出NotFoundException；测试点：查询不存在的分组")
        void getGroup_WithNonExistentId_ShouldThrowNotFoundException() {
            when(groupService.checkGroupExistById(eq(999))).thenReturn(false);

            assertThrows(NotFoundException.class, () -> adminService.getGroup(999));
        }

        @Test
        @DisplayName("输入参数：有效NewGroupDTO带权限；预期结果：返回true；测试点：创建带权限的分组")
        void createGroup_WithPermissions_ShouldReturnTrue() {
            NewGroupDTO dto = new NewGroupDTO();
            dto.setName("newgroup");
            dto.setInfo("test info");
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            when(groupService.checkGroupExistByName(eq("newgroup"))).thenReturn(false);
            when(groupService.save(any(GroupDO.class))).thenReturn(true);
            when(groupPermissionMapper.insertBatch(anyList())).thenReturn(3);

            boolean result = adminService.createGroup(dto);

            assertThat(result).isTrue();
            verify(groupService, times(1)).save(any(GroupDO.class));
            verify(groupPermissionMapper, times(1)).insertBatch(anyList());
        }

        @Test
        @DisplayName("输入参数：重复的分组名称；预期结果：抛出ForbiddenException；测试点：分组名重复校验")
        void createGroup_WithDuplicateName_ShouldThrowForbiddenException() {
            NewGroupDTO dto = new NewGroupDTO();
            dto.setName("existing");

            when(groupService.checkGroupExistByName(eq("existing"))).thenReturn(true);

            assertThrows(ForbiddenException.class, () -> adminService.createGroup(dto));
        }

        @Test
        @DisplayName("输入参数：分组ID、更新DTO；预期结果：返回true；测试点：更新分组信息")
        void updateGroup_WithDifferentName_ShouldReturnTrue() {
            UpdateGroupDTO dto = new UpdateGroupDTO();
            dto.setName("newname");
            dto.setInfo("updated");

            GroupDO exist = new GroupDO();
            exist.setId(1);
            exist.setName("oldname");

            when(groupService.getById(eq(1))).thenReturn(exist);
            when(groupService.checkGroupExistByName(eq("newname"))).thenReturn(false);
            when(groupService.updateById(any(GroupDO.class))).thenReturn(true);

            boolean result = adminService.updateGroup(1, dto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：不存在的分组ID；预期结果：抛出NotFoundException；测试点：更新不存在的分组")
        void updateGroup_WithNonExistentGroup_ShouldThrowNotFoundException() {
            UpdateGroupDTO dto = new UpdateGroupDTO();
            dto.setName("test");

            when(groupService.getById(eq(999))).thenReturn(null);

            assertThrows(NotFoundException.class, () -> adminService.updateGroup(999, dto));
        }

        @Test
        @DisplayName("输入参数：ROOT分组ID；预期结果：抛出ForbiddenException；测试点：禁止删除ROOT分组")
        void deleteGroup_WithRootGroup_ShouldThrowForbiddenException() {
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.GUEST))).thenReturn(2);

            assertThrows(ForbiddenException.class, () -> adminService.deleteGroup(1));
        }

        @Test
        @DisplayName("输入参数：包含用户的分组ID；预期结果：抛出ForbiddenException；测试点：禁止删除有用户的分组")
        void deleteGroup_WithUsersInGroup_ShouldThrowForbiddenException() {
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.GUEST))).thenReturn(2);
            when(groupService.checkGroupExistById(eq(3))).thenReturn(true);
            when(groupService.getGroupUserIds(eq(3))).thenReturn(Arrays.asList(1, 2, 3));

            assertThrows(ForbiddenException.class, () -> adminService.deleteGroup(3));
        }

        @Test
        @DisplayName("输入参数：空的有效分组；预期结果：返回true；测试点：成功删除空分组")
        void deleteGroup_WithEmptyValidGroup_ShouldReturnTrue() {
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.GUEST))).thenReturn(2);
            when(groupService.checkGroupExistById(eq(3))).thenReturn(true);
            when(groupService.getGroupUserIds(eq(3))).thenReturn(Collections.emptyList());
            when(groupService.removeById(eq(3))).thenReturn(true);

            boolean result = adminService.deleteGroup(3);

            assertThat(result).isTrue();
            verify(groupService, times(1)).removeById(eq(3));
        }
    }

    @Nested
    @DisplayName("权限分配测试")
    class PermissionDispatchTests {

        @Test
        @DisplayName("输入参数：DispatchPermissionDTO；预期结果：返回true；测试点：分配单个权限")
        void dispatchPermission_ShouldReturnTrue() {
            DispatchPermissionDTO dto = new DispatchPermissionDTO();
            dto.setGroupId(2);
            dto.setPermissionId(100);

            when(groupPermissionMapper.insert(any(GroupPermissionDO.class))).thenReturn(1);

            boolean result = adminService.dispatchPermission(dto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：DispatchPermissionsDTO；预期结果：返回true；测试点：批量分配权限")
        void dispatchPermissions_ShouldReturnTrue() {
            DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
            dto.setGroupId(2);
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            when(groupPermissionMapper.insertBatch(anyList())).thenReturn(3);

            boolean result = adminService.dispatchPermissions(dto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：RemovePermissionsDTO；预期结果：返回true；测试点：移除多个权限")
        void removePermissions_ShouldReturnTrue() {
            RemovePermissionsDTO dto = new RemovePermissionsDTO();
            dto.setGroupId(2);
            dto.setPermissionIds(Arrays.asList(1, 2));

            when(groupPermissionMapper.deleteBatchByGroupIdAndPermissionId(eq(2), anyList())).thenReturn(2);

            boolean result = adminService.removePermissions(dto);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("分组和权限列表测试")
    class GroupAndPermissionListTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回所有非ROOT分组；测试点：查询所有分组")
        void getAllGroups_ShouldReturnAllNonRootGroups() {
            GroupDO group1 = new GroupDO();
            group1.setId(2);
            group1.setName("guest");
            GroupDO group2 = new GroupDO();
            group2.setId(3);
            group2.setName("admin");

            when(groupService.getParticularGroupIdByLevel(eq(GroupLevelEnum.ROOT))).thenReturn(1);
            when(groupService.list(any(QueryWrapper.class))).thenReturn(Arrays.asList(group1, group2));

            List<GroupDO> result = adminService.getAllGroups();

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回所有已挂载的权限；测试点：查询所有权限")
        void getAllPermissions_ShouldReturnAllMountedPermissions() {
            PermissionDO p1 = new PermissionDO();
            p1.setId(1);
            p1.setName("test");
            p1.setMount(true);

            when(permissionService.list(any(QueryWrapper.class))).thenReturn(Collections.singletonList(p1));

            List<PermissionDO> result = adminService.getAllPermissions();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回结构化权限Map；测试点：按模块结构化权限")
        void getAllStructuralPermissions_ShouldReturnMapByModule() {
            PermissionDO p1 = new PermissionDO();
            p1.setId(1);
            p1.setName("create");
            p1.setModule("user");
            p1.setMount(true);
            PermissionDO p2 = new PermissionDO();
            p2.setId(2);
            p2.setName("delete");
            p2.setModule("user");
            p2.setMount(true);
            PermissionDO p3 = new PermissionDO();
            p3.setId(3);
            p3.setName("query");
            p3.setModule("log");
            p3.setMount(true);

            when(permissionService.list(any(QueryWrapper.class))).thenReturn(Arrays.asList(p1, p2, p3));

            Map<String, List<PermissionDO>> result = adminService.getAllStructuralPermissions();

            assertThat(result).containsKeys("user", "log");
            assertThat(result.get("user")).hasSize(2);
            assertThat(result.get("log")).hasSize(1);
        }
    }
}
