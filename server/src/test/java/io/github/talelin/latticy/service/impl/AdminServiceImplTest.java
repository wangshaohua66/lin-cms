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
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.service.PermissionService;
import io.github.talelin.latticy.service.UserIdentityService;
import io.github.talelin.latticy.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * AdminServiceImpl 单元测试
 * 测试点：管理员用户管理、分组管理、权限分配等操作
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("管理员服务实现类测试")
class AdminServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private UserIdentityService userIdentityService;

    @Mock
    private GroupService groupService;

    @Mock
    private PermissionService permissionService;

    @Mock
    private GroupPermissionMapper groupPermissionMapper;

    @Mock
    private UserGroupMapper userGroupMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private UserDO userDO;
    private GroupDO groupDO;
    private ResetPasswordDTO resetPasswordDTO;
    private NewGroupDTO newGroupDTO;
    private UpdateGroupDTO updateGroupDTO;
    private UpdateUserInfoDTO updateUserInfoDTO;

    @BeforeEach
    void setUp() {
        userDO = new UserDO();
        userDO.setId(1);
        userDO.setUsername("testuser");
        userDO.setNickname("测试用户");
        userDO.setEmail("test@example.com");

        groupDO = new GroupDO();
        groupDO.setId(1);
        groupDO.setName("测试分组");
        groupDO.setInfo("测试分组信息");
        groupDO.setLevel(GroupLevelEnum.USER);

        resetPasswordDTO = new ResetPasswordDTO();
        resetPasswordDTO.setNewPassword("newpassword123");
        resetPasswordDTO.setConfirmPassword("newpassword123");

        newGroupDTO = new NewGroupDTO();
        newGroupDTO.setName("新分组");
        newGroupDTO.setInfo("新分组信息");
        newGroupDTO.setPermissionIds(Arrays.asList(1, 2));

        updateGroupDTO = new UpdateGroupDTO();
        updateGroupDTO.setName("更新分组");
        updateGroupDTO.setInfo("更新分组信息");

        updateUserInfoDTO = new UpdateUserInfoDTO();
        updateUserInfoDTO.setGroupIds(Arrays.asList(1, 2));
    }

    @Test
    @DisplayName("分页获取用户 - 按分组ID")
    void getUserPageByGroupId_WithGroupId_ShouldReturnPage() {
        // 输入参数：分组ID、每页数量、页码
        // 预期结果：返回用户分页数据
        // 测试点：验证按分组查询用户
        Page<UserDO> page = new Page<>(0, 10);
        page.setRecords(Arrays.asList(userDO));
        page.setTotal(1);

        when(userService.getUserPageByGroupId(any(), eq(1))).thenReturn(page);

        IPage<UserDO> result = adminService.getUserPageByGroupId(1, 10, 0);

        assertThat(result.getRecords()).hasSize(1);
    }

    @Test
    @DisplayName("分页获取用户 - 无分组ID（获取所有）")
    void getUserPageByGroupId_WithNullGroupId_ShouldReturnAllUsers() {
        // 输入参数：null分组ID、每页数量、页码
        // 预期结果：返回所有用户分页数据
        // 测试点：验证获取所有用户
        Page<UserDO> page = new Page<>(0, 10);
        page.setRecords(Arrays.asList(userDO));
        page.setTotal(1);

        when(userService.getRootUserId()).thenReturn(999);
        when(userService.page(any(), any(QueryWrapper.class))).thenReturn(page);

        IPage<UserDO> result = adminService.getUserPageByGroupId(null, 10, 0);

        assertThat(result.getRecords()).hasSize(1);
    }

    @Test
    @DisplayName("修改用户密码 - 正常情况")
    void changeUserPassword_WithValidParams_ShouldReturnTrue() {
        // 输入参数：用户ID、重置密码DTO
        // 预期结果：返回true
        // 测试点：验证密码修改逻辑
        when(userService.checkUserExistById(1)).thenReturn(true);
        when(userIdentityService.changePassword(1, "newpassword123")).thenReturn(true);

        boolean result = adminService.changeUserPassword(1, resetPasswordDTO);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("修改用户密码 - 用户不存在抛出异常")
    void changeUserPassword_WithNonExistentUser_ShouldThrowNotFoundException() {
        // 输入参数：不存在的用户ID
        // 预期结果：抛出NotFoundException
        // 测试点：验证用户存在性检查
        when(userService.checkUserExistById(999)).thenReturn(false);

        assertThatThrownBy(() -> adminService.changeUserPassword(999, resetPasswordDTO))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("删除用户 - 正常情况")
    void deleteUser_WithValidId_ShouldReturnTrue() {
        // 输入参数：用户ID
        // 预期结果：返回true
        // 测试点：验证用户删除逻辑
        when(userService.checkUserExistById(1)).thenReturn(true);
        when(userService.getRootUserId()).thenReturn(999);
        when(userService.removeById(1)).thenReturn(true);
        when(userGroupMapper.deleteByUserId(1)).thenReturn(1);
        when(userIdentityService.remove(any(QueryWrapper.class))).thenReturn(true);

        boolean result = adminService.deleteUser(1);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("删除用户 - 删除Root用户抛出异常")
    void deleteUser_WithRootUser_ShouldThrowForbiddenException() {
        // 输入参数：Root用户ID
        // 预期结果：抛出ForbiddenException
        // 测试点：验证Root用户保护
        when(userService.checkUserExistById(1)).thenReturn(true);
        when(userService.getRootUserId()).thenReturn(1);

        assertThatThrownBy(() -> adminService.deleteUser(1))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("删除用户 - 用户不存在抛出异常")
    void deleteUser_WithNonExistentUser_ShouldThrowNotFoundException() {
        // 输入参数：不存在的用户ID
        // 预期结果：抛出NotFoundException
        // 测试点：验证用户存在性检查
        when(userService.checkUserExistById(999)).thenReturn(false);

        assertThatThrownBy(() -> adminService.deleteUser(999))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("更新用户信息 - 包含Root分组抛出异常")
    void updateUserInfo_WithRootGroup_ShouldThrowForbiddenException() {
        // 输入参数：包含Root分组的更新DTO
        // 预期结果：抛出ForbiddenException
        // 测试点：验证Root分组保护
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT)).thenReturn(1);

        updateUserInfoDTO.setGroupIds(Arrays.asList(1));

        assertThatThrownBy(() -> adminService.updateUserInfo(1, updateUserInfoDTO))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("更新用户信息 - 正常情况")
    void updateUserInfo_WithValidParams_ShouldReturnTrue() {
        // 输入参数：用户ID、更新DTO
        // 预期结果：返回true
        // 测试点：验证用户信息更新逻辑
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT)).thenReturn(999);
        when(groupService.getUserGroupIdsByUserId(1)).thenReturn(Arrays.asList(3, 4));
        when(groupService.deleteUserGroupRelations(eq(1), any())).thenReturn(true);
        when(groupService.addUserGroupRelations(eq(1), any())).thenReturn(true);

        boolean result = adminService.updateUserInfo(1, updateUserInfoDTO);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("分页获取分组 - 正常情况")
    void getGroupPage_WithValidParams_ShouldReturnPage() {
        // 输入参数：页码、每页数量
        // 预期结果：返回分组分页数据
        // 测试点：验证分组分页查询
        Page<GroupDO> page = new Page<>(0, 10);
        page.setRecords(Arrays.asList(groupDO));
        page.setTotal(1);

        when(groupService.getGroupPage(0, 10)).thenReturn(page);

        IPage<GroupDO> result = adminService.getGroupPage(0, 10);

        assertThat(result.getRecords()).hasSize(1);
    }

    @Test
    @DisplayName("获取分组 - 正常情况")
    void getGroup_WithValidId_ShouldReturnBO() {
        // 输入参数：分组ID
        // 预期结果：返回分组权限BO
        // 测试点：验证分组查询
        when(groupService.checkGroupExistById(1)).thenReturn(true);
        when(groupService.getGroupAndPermissions(1)).thenReturn(new GroupPermissionBO(groupDO, Collections.emptyList()));

        GroupPermissionBO result = adminService.getGroup(1);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("获取分组 - 分组不存在抛出异常")
    void getGroup_WithNonExistentGroup_ShouldThrowNotFoundException() {
        // 输入参数：不存在的分组ID
        // 预期结果：抛出NotFoundException
        // 测试点：验证分组存在性检查
        when(groupService.checkGroupExistById(999)).thenReturn(false);

        assertThatThrownBy(() -> adminService.getGroup(999))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("创建分组 - 正常情况")
    void createGroup_WithValidDTO_ShouldReturnTrue() {
        // 输入参数：新建分组DTO
        // 预期结果：返回true
        // 测试点：验证分组创建逻辑
        when(groupService.checkGroupExistByName("新分组")).thenReturn(false);
        when(groupService.save(any(GroupDO.class))).thenAnswer(invocation -> {
            GroupDO g = invocation.getArgument(0);
            g.setId(1);
            return true;
        });
        when(groupPermissionMapper.insertBatch(any())).thenReturn(2);

        boolean result = adminService.createGroup(newGroupDTO);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("创建分组 - 分组名已存在抛出异常")
    void createGroup_WithExistingName_ShouldThrowForbiddenException() {
        // 输入参数：已存在的分组名
        // 预期结果：抛出ForbiddenException
        // 测试点：验证分组名唯一性检查
        when(groupService.checkGroupExistByName("新分组")).thenReturn(true);

        assertThatThrownBy(() -> adminService.createGroup(newGroupDTO))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("创建分组 - 无权限ID")
    void createGroup_WithNullPermissionIds_ShouldReturnTrue() {
        // 输入参数：无权限ID的分组DTO
        // 预期结果：返回true
        // 测试点：验证无权限时的处理
        newGroupDTO.setPermissionIds(null);
        when(groupService.checkGroupExistByName("新分组")).thenReturn(false);
        when(groupService.save(any(GroupDO.class))).thenReturn(true);

        boolean result = adminService.createGroup(newGroupDTO);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("更新分组 - 正常情况")
    void updateGroup_WithValidParams_ShouldReturnTrue() {
        // 输入参数：分组ID、更新DTO
        // 预期结果：返回true
        // 测试点：验证分组更新逻辑
        GroupDO existGroup = new GroupDO();
        existGroup.setId(1);
        existGroup.setName("原分组名");
        when(groupService.getById(1)).thenReturn(existGroup);
        when(groupService.updateById(any(GroupDO.class))).thenReturn(true);

        boolean result = adminService.updateGroup(1, updateGroupDTO);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("更新分组 - 分组不存在抛出异常")
    void updateGroup_WithNonExistentGroup_ShouldThrowNotFoundException() {
        // 输入参数：不存在的分组ID
        // 预期结果：抛出NotFoundException
        // 测试点：验证分组存在性检查
        when(groupService.getById(999)).thenReturn(null);

        assertThatThrownBy(() -> adminService.updateGroup(999, updateGroupDTO))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    @DisplayName("更新分组 - 修改名称且名称已存在抛出异常")
    void updateGroup_WithExistingName_ShouldThrowForbiddenException() {
        // 输入参数：已存在的分组名
        // 预期结果：抛出ForbiddenException
        // 测试点：验证分组名唯一性检查
        GroupDO existGroup = new GroupDO();
        existGroup.setId(1);
        existGroup.setName("原分组名");
        when(groupService.getById(1)).thenReturn(existGroup);
        when(groupService.checkGroupExistByName("更新分组")).thenReturn(true);

        assertThatThrownBy(() -> adminService.updateGroup(1, updateGroupDTO))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("删除分组 - 正常情况")
    void deleteGroup_WithValidId_ShouldReturnTrue() {
        // 输入参数：分组ID
        // 预期结果：返回true
        // 测试点：验证分组删除逻辑
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT)).thenReturn(999);
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST)).thenReturn(998);
        when(groupService.checkGroupExistById(1)).thenReturn(true);
        when(groupService.getGroupUserIds(1)).thenReturn(Collections.emptyList());
        when(groupService.removeById(1)).thenReturn(true);

        boolean result = adminService.deleteGroup(1);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("删除分组 - 删除Root分组抛出异常")
    void deleteGroup_WithRootGroup_ShouldThrowForbiddenException() {
        // 输入参数：Root分组ID
        // 预期结果：抛出ForbiddenException
        // 测试点：验证Root分组保护
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT)).thenReturn(1);

        assertThatThrownBy(() -> adminService.deleteGroup(1))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("删除分组 - 删除Guest分组抛出异常")
    void deleteGroup_WithGuestGroup_ShouldThrowForbiddenException() {
        // 输入参数：Guest分组ID
        // 预期结果：抛出ForbiddenException
        // 测试点：验证Guest分组保护
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT)).thenReturn(999);
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST)).thenReturn(1);

        assertThatThrownBy(() -> adminService.deleteGroup(1))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("删除分组 - 分组下有用户抛出异常")
    void deleteGroup_WithUsers_ShouldThrowForbiddenException() {
        // 输入参数：有用户的分组ID
        // 预期结果：抛出ForbiddenException
        // 测试点：验证分组用户检查
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT)).thenReturn(999);
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST)).thenReturn(998);
        when(groupService.checkGroupExistById(1)).thenReturn(true);
        when(groupService.getGroupUserIds(1)).thenReturn(Arrays.asList(1, 2));

        assertThatThrownBy(() -> adminService.deleteGroup(1))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("分配权限 - 正常情况")
    void dispatchPermission_WithValidDTO_ShouldReturnTrue() {
        // 输入参数：分配权限DTO
        // 预期结果：返回true
        // 测试点：验证权限分配逻辑
        DispatchPermissionDTO dto = new DispatchPermissionDTO();
        dto.setGroupId(1);
        dto.setPermissionId(1);

        when(groupPermissionMapper.insert(any())).thenReturn(1);

        boolean result = adminService.dispatchPermission(dto);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("批量分配权限 - 正常情况")
    void dispatchPermissions_WithValidDTO_ShouldReturnTrue() {
        // 输入参数：批量分配权限DTO
        // 预期结果：返回true
        // 测试点：验证批量权限分配逻辑
        DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
        dto.setGroupId(1);
        dto.setPermissionIds(Arrays.asList(1, 2, 3));

        when(groupPermissionMapper.insertBatch(any())).thenReturn(3);

        boolean result = adminService.dispatchPermissions(dto);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("批量分配权限 - 空权限列表")
    void dispatchPermissions_WithEmptyPermissionIds_ShouldReturnFalse() {
        // 输入参数：空权限列表
        // 预期结果：返回false（因为insertBatch返回0）
        // 测试点：验证空列表的处理
        DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
        dto.setGroupId(1);
        dto.setPermissionIds(Collections.emptyList());

        boolean result = adminService.dispatchPermissions(dto);

        // 空列表时insertBatch不会被调用，或者返回0
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("移除权限 - 正常情况")
    void removePermissions_WithValidDTO_ShouldReturnTrue() {
        // 输入参数：移除权限DTO
        // 预期结果：返回true
        // 测试点：验证权限移除逻辑
        RemovePermissionsDTO dto = new RemovePermissionsDTO();
        dto.setGroupId(1);
        dto.setPermissionIds(Arrays.asList(1, 2));

        when(groupPermissionMapper.deleteBatchByGroupIdAndPermissionId(1, Arrays.asList(1, 2))).thenReturn(2);

        boolean result = adminService.removePermissions(dto);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("获取所有分组 - 正常情况")
    void getAllGroups_ShouldReturnGroups() {
        // 输入参数：无
        // 预期结果：返回所有分组（排除Root）
        // 测试点：验证获取所有分组逻辑
        when(groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT)).thenReturn(999);
        when(groupService.list(any(QueryWrapper.class))).thenReturn(Arrays.asList(groupDO));

        List<GroupDO> result = adminService.getAllGroups();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("获取所有权限 - 正常情况")
    void getAllPermissions_ShouldReturnPermissions() {
        // 输入参数：无
        // 预期结果：返回所有已挂载权限
        // 测试点：验证获取所有权限逻辑
        PermissionDO permission = new PermissionDO();
        permission.setId(1);
        permission.setName("查看");
        permission.setMount(true);
        when(permissionService.list(any(QueryWrapper.class))).thenReturn(Arrays.asList(permission));

        List<PermissionDO> result = adminService.getAllPermissions();

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("获取所有结构化权限 - 正常情况")
    void getAllStructuralPermissions_ShouldReturnMap() {
        // 输入参数：无
        // 预期结果：返回结构化的权限Map
        // 测试点：验证权限结构化逻辑
        PermissionDO permission = new PermissionDO();
        permission.setId(1);
        permission.setName("查看");
        permission.setModule("用户管理");
        permission.setMount(true);
        when(permissionService.list(any(QueryWrapper.class))).thenReturn(Arrays.asList(permission));

        Map<String, List<PermissionDO>> result = adminService.getAllStructuralPermissions();

        assertThat(result).hasSize(1);
        assertThat(result.get("用户管理")).hasSize(1);
    }

    @Test
    @DisplayName("获取所有结构化权限 - 空权限列表")
    void getAllStructuralPermissions_WithEmptyPermissions_ShouldReturnEmptyMap() {
        // 输入参数：无
        // 预期结果：返回空Map
        // 测试点：验证空权限的处理
        when(permissionService.list(any(QueryWrapper.class))).thenReturn(Collections.emptyList());

        Map<String, List<PermissionDO>> result = adminService.getAllStructuralPermissions();

        assertThat(result).isEmpty();
    }
}
