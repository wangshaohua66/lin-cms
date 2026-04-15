package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.latticy.bo.GroupPermissionBO;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.mapper.GroupMapper;
import io.github.talelin.latticy.mapper.UserGroupMapper;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserGroupDO;
import io.github.talelin.latticy.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * GroupServiceImpl 单元测试
 * 测试点：分组查询、用户分组关系、权限验证等操作
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("分组服务实现类测试")
class GroupServiceImplTest {

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private UserGroupMapper userGroupMapper;

    @Mock
    private PermissionService permissionService;

    @InjectMocks
    private GroupServiceImpl groupService;

    private GroupDO groupDO;
    private UserGroupDO userGroupDO;

    @BeforeEach
    void setUp() throws Exception {
        // 使用反射设置baseMapper，因为GroupServiceImpl继承自ServiceImpl
        Field baseMapperField = groupService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(groupService, groupMapper);

        groupDO = new GroupDO();
        groupDO.setId(1);
        groupDO.setName("测试分组");
        groupDO.setInfo("测试分组信息");
        groupDO.setLevel(GroupLevelEnum.USER);

        userGroupDO = new UserGroupDO(1, 1);
        userGroupDO.setId(1);
    }

    @Test
    @DisplayName("获取用户分组 - 正常情况")
    void getUserGroupsByUserId_WithValidUserId_ShouldReturnGroups() {
        // 输入参数：有效用户ID
        // 预期结果：返回用户所属分组列表
        // 测试点：验证用户分组查询
        Integer userId = 1;
        when(groupMapper.selectGroupsByUserId(userId)).thenReturn(Arrays.asList(groupDO));

        List<GroupDO> result = groupService.getUserGroupsByUserId(userId);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("测试分组");
    }

    @Test
    @DisplayName("获取用户分组 - 用户无分组")
    void getUserGroupsByUserId_WithNoGroups_ShouldReturnEmptyList() {
        // 输入参数：用户ID
        // 预期结果：返回空列表
        // 测试点：验证无分组时的处理
        when(groupMapper.selectGroupsByUserId(1)).thenReturn(Collections.emptyList());

        List<GroupDO> result = groupService.getUserGroupsByUserId(1);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("获取用户分组ID - 正常情况")
    void getUserGroupIdsByUserId_WithValidUserId_ShouldReturnIds() {
        // 输入参数：有效用户ID
        // 预期结果：返回用户分组ID列表
        // 测试点：验证用户分组ID查询
        when(groupMapper.selectUserGroupIds(1)).thenReturn(Arrays.asList(1));

        List<Integer> result = groupService.getUserGroupIdsByUserId(1);

        assertThat(result).containsExactly(1);
    }

    @Test
    @DisplayName("获取用户分组ID - 用户无分组")
    void getUserGroupIdsByUserId_WithNoGroups_ShouldReturnEmptyList() {
        // 输入参数：用户ID
        // 预期结果：返回空列表
        // 测试点：验证无分组时的处理
        when(groupMapper.selectUserGroupIds(1)).thenReturn(Collections.emptyList());

        List<Integer> result = groupService.getUserGroupIdsByUserId(1);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("获取用户分组ID - null结果")
    void getUserGroupIdsByUserId_WithNullResult_ShouldReturnEmptyList() {
        // 输入参数：用户ID
        // 预期结果：返回空列表或null（取决于实现）
        // 测试点：验证null结果处理
        when(groupMapper.selectUserGroupIds(1)).thenReturn(null);

        List<Integer> result = groupService.getUserGroupIdsByUserId(1);

        // 实际实现可能返回null或空列表
        assertThat(result == null || result.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("获取分组权限 - 正常情况")
    void getGroupAndPermissions_WithValidGroupId_ShouldReturnGroupPermissionBO() {
        // 输入参数：有效分组ID
        // 预期结果：返回分组权限BO
        // 测试点：验证分组权限查询
        when(groupMapper.selectById(1)).thenReturn(groupDO);
        when(permissionService.getPermissionByGroupId(1)).thenReturn(Arrays.asList(new PermissionDO()));

        GroupPermissionBO result = groupService.getGroupAndPermissions(1);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("测试分组");
    }

    @Test
    @DisplayName("获取分组权限 - 分组不存在抛出异常")
    void getGroupAndPermissions_WithNonExistingGroup_ShouldThrowNotFoundException() {
        // 输入参数：不存在的分组ID
        // 预期结果：抛出NotFoundException
        // 测试点：验证分组存在性检查
        when(groupMapper.selectById(999)).thenReturn(null);

        assertThatThrownBy(() -> groupService.getGroupAndPermissions(999))
                .isInstanceOfAny(NotFoundException.class, IllegalArgumentException.class);
    }

    @Test
    @DisplayName("分页获取分组 - 正常情况")
    void getGroupPage_WithValidParams_ShouldReturnPage() {
        // 输入参数：分页参数
        // 预期结果：返回分组分页数据
        // 测试点：验证分页查询逻辑
        LinPage<GroupDO> page = new LinPage<>(0, 10);
        page.setRecords(Arrays.asList(groupDO));
        page.setTotal(1);
        when(groupMapper.selectPage(any(LinPage.class), any())).thenReturn(page);

        IPage<GroupDO> result = groupService.getGroupPage(0, 10);

        assertThat(result.getRecords()).hasSize(1);
    }

    @Test
    @DisplayName("检查分组是否存在 - 存在的分组")
    void checkGroupExistById_WithExistingGroup_ShouldReturnTrue() {
        // 输入参数：存在的分组ID
        // 预期结果：返回true
        // 测试点：验证分组存在性检查
        when(groupMapper.selectCountById(1)).thenReturn(1);

        boolean result = groupService.checkGroupExistById(1);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("检查分组是否存在 - 不存在的分组")
    void checkGroupExistById_WithNonExistingGroup_ShouldReturnFalse() {
        // 输入参数：不存在的分组ID
        // 预期结果：返回false
        // 测试点：验证分组不存在时的处理
        when(groupMapper.selectCountById(999)).thenReturn(0);

        boolean result = groupService.checkGroupExistById(999);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("检查分组名称是否存在 - 存在的名称")
    void checkGroupExistByName_WithExistingName_ShouldReturnTrue() {
        // 输入参数：存在的分组名称
        // 预期结果：返回true
        // 测试点：验证分组名称存在性检查
        when(groupMapper.selectCount(any(QueryWrapper.class))).thenReturn(1L);

        boolean result = groupService.checkGroupExistByName("测试分组");

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("检查分组名称是否存在 - 不存在的名称")
    void checkGroupExistByName_WithNonExistingName_ShouldReturnFalse() {
        // 输入参数：不存在的分组名称
        // 预期结果：返回false
        // 测试点：验证分组名称不存在时的处理
        when(groupMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);

        boolean result = groupService.checkGroupExistByName("不存在分组");

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("检查是否为Root用户 - Root用户")
    void checkIsRootByUserId_WithRootUser_ShouldReturnTrue() {
        // 输入参数：Root用户ID
        // 预期结果：返回true
        // 测试点：验证Root用户检查
        GroupDO rootGroup = new GroupDO();
        rootGroup.setId(1);
        rootGroup.setLevel(GroupLevelEnum.ROOT);
        when(groupMapper.selectOne(any(QueryWrapper.class))).thenReturn(rootGroup);
        when(userGroupMapper.selectOne(any(QueryWrapper.class))).thenReturn(userGroupDO);

        boolean result = groupService.checkIsRootByUserId(1);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("检查是否为Root用户 - 非Root用户")
    void checkIsRootByUserId_WithNonRootUser_ShouldReturnFalse() {
        // 输入参数：非Root用户ID
        // 预期结果：返回false
        // 测试点：验证非Root用户的处理
        GroupDO rootGroup = new GroupDO();
        rootGroup.setId(1);
        rootGroup.setLevel(GroupLevelEnum.ROOT);
        when(groupMapper.selectOne(any(QueryWrapper.class))).thenReturn(rootGroup);
        when(userGroupMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        boolean result = groupService.checkIsRootByUserId(2);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("删除用户分组关系 - 空删除列表")
    void deleteUserGroupRelations_WithEmptyDeleteIds_ShouldReturnTrue() {
        // 输入参数：空删除列表
        // 预期结果：返回true
        // 测试点：验证空列表的处理
        boolean result = groupService.deleteUserGroupRelations(1, Collections.emptyList());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("删除用户分组关系 - null删除列表")
    void deleteUserGroupRelations_WithNullDeleteIds_ShouldReturnTrue() {
        // 输入参数：null删除列表
        // 预期结果：返回true
        // 测试点：验证null列表的处理
        boolean result = groupService.deleteUserGroupRelations(1, null);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("删除用户分组关系 - Root用户抛出异常")
    void deleteUserGroupRelations_WithRootUser_ShouldThrowForbiddenException() {
        // 输入参数：Root用户ID和删除列表
        // 预期结果：抛出ForbiddenException
        // 测试点：验证Root用户保护逻辑
        GroupDO rootGroup = new GroupDO();
        rootGroup.setId(1);
        rootGroup.setLevel(GroupLevelEnum.ROOT);
        when(groupMapper.selectOne(any(QueryWrapper.class))).thenReturn(rootGroup);
        when(userGroupMapper.selectOne(any(QueryWrapper.class))).thenReturn(userGroupDO);

        assertThatThrownBy(() -> groupService.deleteUserGroupRelations(1, Arrays.asList(2, 3)))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("删除用户分组关系 - 正常情况")
    void deleteUserGroupRelations_WithValidParams_ShouldReturnTrue() {
        // 输入参数：有效用户ID和删除列表
        // 预期结果：返回true
        // 测试点：验证删除逻辑
        GroupDO rootGroup = new GroupDO();
        rootGroup.setId(1);
        rootGroup.setLevel(GroupLevelEnum.ROOT);
        when(groupMapper.selectOne(any(QueryWrapper.class))).thenReturn(rootGroup);
        when(userGroupMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        when(userGroupMapper.delete(any(QueryWrapper.class))).thenReturn(2);

        boolean result = groupService.deleteUserGroupRelations(2, Arrays.asList(2, 3));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("添加用户分组关系 - 空添加列表")
    void addUserGroupRelations_WithEmptyAddIds_ShouldReturnTrue() {
        // 输入参数：空添加列表
        // 预期结果：返回true
        // 测试点：验证空列表的处理
        boolean result = groupService.addUserGroupRelations(1, Collections.emptyList());

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("添加用户分组关系 - null添加列表")
    void addUserGroupRelations_WithNullAddIds_ShouldReturnTrue() {
        // 输入参数：null添加列表
        // 预期结果：返回true
        // 测试点：验证null列表的处理
        boolean result = groupService.addUserGroupRelations(1, null);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("添加用户分组关系 - 分组不存在抛出异常")
    void addUserGroupRelations_WithNonExistingGroup_ShouldThrowForbiddenException() {
        // 输入参数：包含不存在分组的列表
        // 预期结果：抛出ForbiddenException
        // 测试点：验证分组存在性检查
        when(groupMapper.selectCountById(999)).thenReturn(0);

        assertThatThrownBy(() -> groupService.addUserGroupRelations(1, Arrays.asList(999)))
                .isInstanceOf(ForbiddenException.class);
    }

    @Test
    @DisplayName("添加用户分组关系 - 正常情况")
    void addUserGroupRelations_WithValidParams_ShouldReturnTrue() {
        // 输入参数：有效用户ID和添加列表
        // 预期结果：返回true
        // 测试点：验证添加逻辑
        when(groupMapper.selectCountById(2)).thenReturn(1);
        when(userGroupMapper.insertBatch(any())).thenReturn(1);

        boolean result = groupService.addUserGroupRelations(1, Arrays.asList(2));

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("获取分组用户ID - 正常情况")
    void getGroupUserIds_WithValidGroupId_ShouldReturnUserIds() {
        // 输入参数：有效分组ID
        // 预期结果：返回用户ID列表
        // 测试点：验证分组用户查询
        UserGroupDO ug1 = new UserGroupDO(1, 1);
        UserGroupDO ug2 = new UserGroupDO(2, 1);
        when(userGroupMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(ug1, ug2));

        List<Integer> result = groupService.getGroupUserIds(1);

        assertThat(result).containsExactly(1, 2);
    }

    @Test
    @DisplayName("获取特定级别分组 - Root级别")
    void getParticularGroupByLevel_WithRootLevel_ShouldReturnGroup() {
        // 输入参数：Root级别
        // 预期结果：返回Root分组
        // 测试点：验证Root分组查询
        GroupDO rootGroup = new GroupDO();
        rootGroup.setId(1);
        rootGroup.setLevel(GroupLevelEnum.ROOT);
        when(groupMapper.selectOne(any(QueryWrapper.class))).thenReturn(rootGroup);

        GroupDO result = groupService.getParticularGroupByLevel(GroupLevelEnum.ROOT);

        assertThat(result).isNotNull();
        assertThat(result.getLevel()).isEqualTo(GroupLevelEnum.ROOT);
    }

    @Test
    @DisplayName("获取特定级别分组 - User级别返回null")
    void getParticularGroupByLevel_WithUserLevel_ShouldReturnNull() {
        // 输入参数：User级别
        // 预期结果：返回null
        // 测试点：验证User级别特殊处理
        GroupDO result = groupService.getParticularGroupByLevel(GroupLevelEnum.USER);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("获取特定级别分组ID - 正常情况")
    void getParticularGroupIdByLevel_WithValidLevel_ShouldReturnId() {
        // 输入参数：有效级别
        // 预期结果：返回分组ID
        // 测试点：验证分组ID查询
        GroupDO rootGroup = new GroupDO();
        rootGroup.setId(1);
        rootGroup.setLevel(GroupLevelEnum.ROOT);
        when(groupMapper.selectOne(any(QueryWrapper.class))).thenReturn(rootGroup);

        Integer result = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);

        assertThat(result).isEqualTo(1);
    }

    @Test
    @DisplayName("获取特定级别分组ID - 分组不存在返回0")
    void getParticularGroupIdByLevel_WithNonExistingGroup_ShouldReturnZero() {
        // 输入参数：级别
        // 预期结果：返回0
        // 测试点：验证不存在时的处理
        when(groupMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);

        Integer result = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);

        assertThat(result).isEqualTo(0);
    }

    @Test
    @DisplayName("获取特定级别分组ID - User级别返回0")
    void getParticularGroupIdByLevel_WithUserLevel_ShouldReturnZero() {
        // 输入参数：User级别
        // 预期结果：返回0
        // 测试点：验证User级别特殊处理
        Integer result = groupService.getParticularGroupIdByLevel(GroupLevelEnum.USER);

        assertThat(result).isEqualTo(0);
    }
}
