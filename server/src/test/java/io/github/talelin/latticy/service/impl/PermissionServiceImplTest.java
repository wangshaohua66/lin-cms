package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.mapper.PermissionMapper;
import io.github.talelin.latticy.model.PermissionDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * PermissionServiceImpl 单元测试
 * 测试点：权限查询、结构化等操作
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("权限服务实现类测试")
class PermissionServiceImplTest {

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    private PermissionDO permission1;
    private PermissionDO permission2;
    private PermissionDO permission3;

    @BeforeEach
    void setUp() {
        permission1 = new PermissionDO();
        permission1.setId(1);
        permission1.setName("查看用户");
        permission1.setModule("用户管理");
        permission1.setMount(true);

        permission2 = new PermissionDO();
        permission2.setId(2);
        permission2.setName("编辑用户");
        permission2.setModule("用户管理");
        permission2.setMount(true);

        permission3 = new PermissionDO();
        permission3.setId(3);
        permission3.setName("查看图书");
        permission3.setModule("图书管理");
        permission3.setMount(true);
    }

    @Test
    @DisplayName("根据分组ID获取权限 - 正常情况")
    void getPermissionByGroupId_WithValidGroupId_ShouldReturnPermissions() {
        // 输入参数：有效的分组ID
        // 预期结果：返回该分组的权限列表
        // 测试点：验证分组权限查询
        Integer groupId = 1;
        List<PermissionDO> expectedPermissions = Arrays.asList(permission1, permission2);
        when(permissionMapper.selectPermissionsByGroupId(groupId)).thenReturn(expectedPermissions);

        List<PermissionDO> result = permissionService.getPermissionByGroupId(groupId);

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("查看用户");
    }

    @Test
    @DisplayName("根据分组ID获取权限 - 无权限")
    void getPermissionByGroupId_WithNoPermissions_ShouldReturnEmptyList() {
        // 输入参数：分组ID
        // 预期结果：返回空列表
        // 测试点：验证无权限时的处理
        when(permissionMapper.selectPermissionsByGroupId(1)).thenReturn(Collections.emptyList());

        List<PermissionDO> result = permissionService.getPermissionByGroupId(1);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据分组ID获取权限 - null参数")
    void getPermissionByGroupId_WithNullGroupId_ShouldHandleNull() {
        // 输入参数：null分组ID
        // 预期结果：正常处理
        // 测试点：验证null参数的处理
        when(permissionMapper.selectPermissionsByGroupId(null)).thenReturn(Collections.emptyList());

        List<PermissionDO> result = permissionService.getPermissionByGroupId(null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据分组ID列表获取权限 - 正常情况")
    void getPermissionByGroupIds_WithValidGroupIds_ShouldReturnPermissions() {
        // 输入参数：有效的分组ID列表
        // 预期结果：返回这些分组的权限列表
        // 测试点：验证多分组权限查询
        List<Integer> groupIds = Arrays.asList(1, 2);
        List<PermissionDO> expectedPermissions = Arrays.asList(permission1, permission2, permission3);
        when(permissionMapper.selectPermissionsByGroupIds(groupIds)).thenReturn(expectedPermissions);

        List<PermissionDO> result = permissionService.getPermissionByGroupIds(groupIds);

        assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("根据分组ID列表获取权限 - 空列表")
    void getPermissionByGroupIds_WithEmptyList_ShouldReturnEmptyList() {
        // 输入参数：空列表
        // 预期结果：返回空列表
        // 测试点：验证空列表处理
        when(permissionMapper.selectPermissionsByGroupIds(Collections.emptyList())).thenReturn(Collections.emptyList());

        List<PermissionDO> result = permissionService.getPermissionByGroupIds(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据分组ID列表获取权限 - null列表")
    void getPermissionByGroupIds_WithNullList_ShouldReturnEmptyList() {
        // 输入参数：null列表
        // 预期结果：返回空列表
        // 测试点：验证null列表处理
        when(permissionMapper.selectPermissionsByGroupIds(null)).thenReturn(Collections.emptyList());

        List<PermissionDO> result = permissionService.getPermissionByGroupIds(null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据分组ID和模块获取权限 - 正常情况")
    void getPermissionByGroupIdsAndModule_WithValidParams_ShouldReturnPermissions() {
        // 输入参数：分组ID列表和模块名
        // 预期结果：返回该模块的权限列表
        // 测试点：验证按模块查询权限
        List<Integer> groupIds = Arrays.asList(1, 2);
        List<PermissionDO> expectedPermissions = Arrays.asList(permission1, permission2);
        when(permissionMapper.selectPermissionsByGroupIdsAndModule(groupIds, "用户管理")).thenReturn(expectedPermissions);

        List<PermissionDO> result = permissionService.getPermissionByGroupIdsAndModule(groupIds, "用户管理");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getModule()).isEqualTo("用户管理");
    }

    @Test
    @DisplayName("根据分组ID和模块获取权限 - 空分组列表")
    void getPermissionByGroupIdsAndModule_WithEmptyGroupIds_ShouldReturnEmptyList() {
        // 输入参数：空分组ID列表
        // 预期结果：返回空列表
        // 测试点：验证空列表处理
        when(permissionMapper.selectPermissionsByGroupIdsAndModule(Collections.emptyList(), "用户管理")).thenReturn(Collections.emptyList());

        List<PermissionDO> result = permissionService.getPermissionByGroupIdsAndModule(Collections.emptyList(), "用户管理");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据分组ID和模块获取权限 - null分组列表")
    void getPermissionByGroupIdsAndModule_WithNullGroupIds_ShouldReturnEmptyList() {
        // 输入参数：null分组ID列表
        // 预期结果：返回空列表
        // 测试点：验证null列表处理
        when(permissionMapper.selectPermissionsByGroupIdsAndModule(null, "用户管理")).thenReturn(Collections.emptyList());

        List<PermissionDO> result = permissionService.getPermissionByGroupIdsAndModule(null, "用户管理");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("结构化权限 - 正常情况")
    void structuringPermissions_WithValidPermissions_ShouldReturnStructuredMap() {
        // 输入参数：权限列表
        // 预期结果：返回按模块分组的结构化权限
        // 测试点：验证权限结构化逻辑
        List<PermissionDO> permissions = Arrays.asList(permission1, permission2, permission3);

        List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(permissions);

        assertThat(result).isNotEmpty();
        // 验证有两个模块（用户管理、图书管理）
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("结构化权限 - 空列表")
    void structuringPermissions_WithEmptyList_ShouldReturnEmptyList() {
        // 输入参数：空列表
        // 预期结果：返回空列表
        // 测试点：验证空列表处理
        List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("结构化权限 - null列表抛出异常")
    void structuringPermissions_WithNullList_ShouldThrowException() {
        // 输入参数：null列表
        // 预期结果：抛出NullPointerException
        // 测试点：验证null列表处理
        assertThatThrownBy(() -> permissionService.structuringPermissions(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("结构化权限 - 单个权限")
    void structuringPermissions_WithSinglePermission_ShouldReturnSingleModule() {
        // 输入参数：单个权限
        // 预期结果：返回单个模块的结构
        // 测试点：验证单个权限处理
        List<PermissionDO> permissions = Collections.singletonList(permission1);

        List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(permissions);

        assertThat(result).hasSize(1);
    }

    @Test
    @DisplayName("结构化权限 - 相同模块多个权限")
    void structuringPermissions_WithSameModulePermissions_ShouldGroupTogether() {
        // 输入参数：相同模块的多个权限
        // 预期结果：权限被分组到一起
        // 测试点：验证权限分组逻辑
        List<PermissionDO> permissions = Arrays.asList(permission1, permission2);

        List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(permissions);

        assertThat(result).hasSize(1);
        // 验证该模块下有两个权限
        Map<String, List<Map<String, String>>> module = result.get(0);
        assertThat(module.get("用户管理")).hasSize(2);
    }

    @Test
    @DisplayName("结构化权限 - 未挂载权限也被包含")
    void structuringPermissions_WithUnmountedPermission_ShouldIncludeIt() {
        // 输入参数：包含未挂载权限的列表
        // 预期结果：未挂载权限也被包含（当前实现不过滤）
        // 测试点：验证所有权限都被处理
        PermissionDO unmountedPermission = new PermissionDO();
        unmountedPermission.setId(4);
        unmountedPermission.setName("未挂载权限");
        unmountedPermission.setModule("测试模块");
        unmountedPermission.setMount(false);

        List<PermissionDO> permissions = Arrays.asList(permission1, unmountedPermission);

        List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(permissions);

        // 包含两个模块的权限
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("简化结构化权限 - 正常情况")
    void structuringPermissionsSimply_WithValidPermissions_ShouldReturnSimpleMap() {
        // 输入参数：权限列表
        // 预期结果：返回简化的权限映射（模块 -> 权限名列表）
        // 测试点：验证简化权限结构化逻辑
        List<PermissionDO> permissions = Arrays.asList(permission1, permission2, permission3);

        Map<String, List<String>> result = permissionService.structuringPermissionsSimply(permissions);

        assertThat(result).isNotEmpty();
        assertThat(result).containsKeys("用户管理", "图书管理");
        assertThat(result.get("用户管理")).containsExactly("查看用户", "编辑用户");
        assertThat(result.get("图书管理")).containsExactly("查看图书");
    }

    @Test
    @DisplayName("简化结构化权限 - 空列表")
    void structuringPermissionsSimply_WithEmptyList_ShouldReturnEmptyMap() {
        // 输入参数：空列表
        // 预期结果：返回空Map
        // 测试点：验证空列表处理
        Map<String, List<String>> result = permissionService.structuringPermissionsSimply(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("简化结构化权限 - null列表抛出异常")
    void structuringPermissionsSimply_WithNullList_ShouldThrowException() {
        // 输入参数：null列表
        // 预期结果：抛出NullPointerException
        // 测试点：验证null列表处理
        assertThatThrownBy(() -> permissionService.structuringPermissionsSimply(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("简化结构化权限 - 单个权限")
    void structuringPermissionsSimply_WithSinglePermission_ShouldReturnSingleEntry() {
        // 输入参数：单个权限
        // 预期结果：返回单个模块的映射
        // 测试点：验证单个权限处理
        List<PermissionDO> permissions = Collections.singletonList(permission1);

        Map<String, List<String>> result = permissionService.structuringPermissionsSimply(permissions);

        assertThat(result).hasSize(1);
        assertThat(result.get("用户管理")).containsExactly("查看用户");
    }

    @Test
    @DisplayName("简化结构化权限 - 相同模块多个权限")
    void structuringPermissionsSimply_WithSameModulePermissions_ShouldGroupTogether() {
        // 输入参数：相同模块的多个权限
        // 预期结果：权限名被分组到一起
        // 测试点：验证权限分组逻辑
        List<PermissionDO> permissions = Arrays.asList(permission1, permission2);

        Map<String, List<String>> result = permissionService.structuringPermissionsSimply(permissions);

        assertThat(result).hasSize(1);
        assertThat(result.get("用户管理")).containsExactly("查看用户", "编辑用户");
    }

    @Test
    @DisplayName("根据分组ID列表获取权限Map - 正常情况")
    void getPermissionMapByGroupIds_WithValidGroupIds_ShouldReturnPermissionMap() {
        // 输入参数：分组ID列表
        // 预期结果：返回每个分组对应的权限列表Map
        // 测试点：验证权限Map构建
        List<Integer> groupIds = Arrays.asList(1, 2);
        List<PermissionDO> group1Permissions = Arrays.asList(permission1, permission2);
        List<PermissionDO> group2Permissions = Collections.singletonList(permission3);

        when(permissionMapper.selectPermissionsByGroupId(1)).thenReturn(group1Permissions);
        when(permissionMapper.selectPermissionsByGroupId(2)).thenReturn(group2Permissions);

        Map<Long, List<PermissionDO>> result = permissionService.getPermissionMapByGroupIds(groupIds);

        assertThat(result).hasSize(2);
        assertThat(result.get(1)).hasSize(2);
        assertThat(result.get(2)).hasSize(1);
    }

    @Test
    @DisplayName("根据分组ID列表获取权限Map - 空列表")
    void getPermissionMapByGroupIds_WithEmptyList_ShouldReturnEmptyMap() {
        // 输入参数：空列表
        // 预期结果：返回空Map
        // 测试点：验证空列表处理
        Map<Long, List<PermissionDO>> result = permissionService.getPermissionMapByGroupIds(Collections.emptyList());

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据分组ID列表获取权限Map - 单个分组")
    void getPermissionMapByGroupIds_WithSingleGroupId_ShouldReturnSingleEntry() {
        // 输入参数：单个分组ID
        // 预期结果：返回单个条目的Map
        // 测试点：验证单个分组处理
        List<Integer> groupIds = Collections.singletonList(1);
        List<PermissionDO> permissions = Arrays.asList(permission1, permission2);

        when(permissionMapper.selectPermissionsByGroupId(1)).thenReturn(permissions);

        Map<Long, List<PermissionDO>> result = permissionService.getPermissionMapByGroupIds(groupIds);

        assertThat(result).hasSize(1);
        assertThat(result.get(1)).hasSize(2);
    }

    @Test
    @DisplayName("根据分组ID列表获取权限Map - 分组无权限")
    void getPermissionMapByGroupIds_WithGroupNoPermissions_ShouldReturnEmptyList() {
        // 输入参数：分组ID列表（其中某些分组无权限）
        // 预期结果：返回的Map中包含空列表
        // 测试点：验证无权限分组处理
        List<Integer> groupIds = Arrays.asList(1, 2);

        when(permissionMapper.selectPermissionsByGroupId(1)).thenReturn(Arrays.asList(permission1));
        when(permissionMapper.selectPermissionsByGroupId(2)).thenReturn(Collections.emptyList());

        Map<Long, List<PermissionDO>> result = permissionService.getPermissionMapByGroupIds(groupIds);

        assertThat(result).hasSize(2);
        assertThat(result.get(1)).hasSize(1);
        assertThat(result.get(2)).isEmpty();
    }
}
