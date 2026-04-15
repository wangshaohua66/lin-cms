package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.latticy.bo.GroupPermissionBO;

import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.mapper.GroupMapper;
import io.github.talelin.latticy.mapper.UserGroupMapper;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserGroupDO;
import io.github.talelin.latticy.service.PermissionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("GroupService单元测试")
class GroupServiceImplTest {

    @Mock
    private PermissionService permissionService;

    @Mock
    private UserGroupMapper userGroupMapper;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private GroupServiceImpl groupService;

    @BeforeEach
    void setUp() {
        injectBaseMapper(groupService, groupMapper);
    }

    private <M extends com.baomidou.mybatisplus.core.mapper.BaseMapper<T>, T> void injectBaseMapper(
            com.baomidou.mybatisplus.extension.service.impl.ServiceImpl<M, T> service, M mapper) {
        try {
            java.lang.reflect.Field field = com.baomidou.mybatisplus.extension.service.impl.ServiceImpl.class.getDeclaredField("baseMapper");
            field.setAccessible(true);
            field.set(service, mapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to inject baseMapper", e);
        }
    }

    @Nested
    @DisplayName("用户分组查询测试")
    class UserGroupQueryTests {

        @Test
        @DisplayName("输入参数：有效用户ID；预期结果：返回用户分组列表；测试点：正常查询用户分组")
        void getUserGroupsByUserId_WithValidId_ShouldReturnGroups() {
            List<GroupDO> expected = Arrays.asList(new GroupDO(), new GroupDO());
            when(groupMapper.selectGroupsByUserId(eq(1))).thenReturn(expected);

            List<GroupDO> result = groupService.getUserGroupsByUserId(1);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：不存在的用户ID；预期结果：返回空列表；测试点：用户无分组场景")
        void getUserGroupsByUserId_WithNonExistentId_ShouldReturnEmpty() {
            when(groupMapper.selectGroupsByUserId(eq(999))).thenReturn(Collections.emptyList());

            List<GroupDO> result = groupService.getUserGroupsByUserId(999);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("输入参数：有效用户ID；预期结果：返回分组ID列表；测试点：查询用户分组ID")
        void getUserGroupIdsByUserId_WithValidId_ShouldReturnGroupIds() {
            List<Integer> expected = Arrays.asList(1, 2, 3);
            when(groupMapper.selectUserGroupIds(eq(1))).thenReturn(expected);

            List<Integer> result = groupService.getUserGroupIdsByUserId(1);

            assertThat(result).containsExactly(1, 2, 3);
        }
    }

    @Nested
    @DisplayName("分组存在性测试")
    class GroupExistenceTests {

        @ParameterizedTest
        @ValueSource(ints = {0, 1})
        @DisplayName("输入参数：分组ID；预期结果：正确返回存在性；测试点：分组存在性检查分支覆盖")
        void checkGroupExistById_ShouldReturnCorrectResult(int count) {
            when(groupMapper.selectCountById(eq(1))).thenReturn(count);

            boolean result = groupService.checkGroupExistById(1);

            assertThat(result).isEqualTo(count > 0);
        }

        @Test
        @DisplayName("输入参数：分组名称；预期结果：返回true；测试点：分组名称存在")
        void checkGroupExistByName_WhenNameExists_ShouldReturnTrue() {
            when(groupMapper.selectCount(any())).thenReturn(1L);

            boolean result = groupService.checkGroupExistByName("admin");

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("分组权限测试")
    class GroupPermissionTests {

        @Test
        @DisplayName("输入参数：有效分组ID；预期结果：返回分组权限BO；测试点：正常查询分组及权限")
        void getGroupAndPermissions_WithValidId_ShouldReturnBO() {
            GroupDO group = new GroupDO();
            group.setId(1);
            group.setName("test");
            List<PermissionDO> permissions = Arrays.asList(new PermissionDO(), new PermissionDO());
            when(groupMapper.selectById(eq(1))).thenReturn(group);
            when(permissionService.getPermissionByGroupId(eq(1))).thenReturn(permissions);

            GroupPermissionBO result = groupService.getGroupAndPermissions(1);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("test");
            assertThat(result.getPermissions()).hasSize(2);
        }
    }

    @Nested
    @DisplayName("用户分组关系删除测试")
    class DeleteUserGroupRelationsTests {

        @Test
        @DisplayName("输入参数：null删除ID列表；预期结果：返回true；测试点：空列表直接返回")
        void deleteUserGroupRelations_WithNullIds_ShouldReturnTrue() {
            boolean result = groupService.deleteUserGroupRelations(1, null);

            assertThat(result).isTrue();
            verify(userGroupMapper, never()).delete(any());
        }

        @Test
        @DisplayName("输入参数：删除Root用户分组；预期结果：抛出ForbiddenException；测试点：禁止删除Root用户分组")
        void deleteUserGroupRelations_ForRootUser_ShouldThrowForbiddenException() {
            when(groupMapper.selectOne(any())).thenReturn(mock(GroupDO.class));
            when(userGroupMapper.selectOne(any())).thenReturn(mock(UserGroupDO.class));

            ForbiddenException exception = assertThrows(ForbiddenException.class,
                    () -> groupService.deleteUserGroupRelations(1, Collections.singletonList(2)));

            assertThat(exception.getCode()).isEqualTo(10078);
        }

        @Test
        @DisplayName("输入参数：有效删除ID列表；预期结果：返回true；测试点：正常删除用户分组关系")
        void deleteUserGroupRelations_WithValidIds_ShouldReturnTrue() {
            when(groupMapper.selectOne(any())).thenReturn(mock(GroupDO.class));
            when(userGroupMapper.selectOne(any())).thenReturn(null);
            when(userGroupMapper.delete(any())).thenReturn(2);

            boolean result = groupService.deleteUserGroupRelations(2, Arrays.asList(2, 3));

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("用户分组关系添加测试")
    class AddUserGroupRelationsTests {

        @Test
        @DisplayName("输入参数：null添加ID列表；预期结果：返回true；测试点：空列表直接返回")
        void addUserGroupRelations_WithNullIds_ShouldReturnTrue() {
            boolean result = groupService.addUserGroupRelations(1, null);

            assertThat(result).isTrue();
            verify(userGroupMapper, never()).insertBatch(anyList());
        }

        @Test
        @DisplayName("输入参数：包含不存在的分组ID；预期结果：抛出ForbiddenException；测试点：分组不存在异常")
        void addUserGroupRelations_WithNonExistentGroup_ShouldThrowForbiddenException() {
            when(groupMapper.selectCountById(eq(999))).thenReturn(0);

            ForbiddenException exception = assertThrows(ForbiddenException.class,
                    () -> groupService.addUserGroupRelations(1, Collections.singletonList(999)));

            assertThat(exception.getCode()).isEqualTo(10077);
        }

        @Test
        @DisplayName("输入参数：有效分组ID列表；预期结果：返回true；测试点：正常添加用户分组关系")
        void addUserGroupRelations_WithValidIds_ShouldReturnTrue() {
            when(groupMapper.selectCountById(anyInt())).thenReturn(1);
            when(userGroupMapper.insertBatch(anyList())).thenReturn(2);

            boolean result = groupService.addUserGroupRelations(1, Arrays.asList(2, 3));

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("特定分组查询测试")
    class ParticularGroupTests {

        @Test
        @DisplayName("输入参数：USER level；预期结果：返回null；测试点：USER level特殊处理")
        void getParticularGroupByLevel_WithUserLevel_ShouldReturnNull() {
            GroupDO result = groupService.getParticularGroupByLevel(GroupLevelEnum.USER);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("输入参数：ROOT level；预期结果：返回Root分组；测试点：ROOT level查询")
        void getParticularGroupByLevel_WithRootLevel_ShouldReturnGroup() {
            GroupDO expected = new GroupDO();
            expected.setName("Root");
            when(groupMapper.selectOne(any())).thenReturn(expected);

            GroupDO result = groupService.getParticularGroupByLevel(GroupLevelEnum.ROOT);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Root");
        }

        @Test
        @DisplayName("输入参数：分组存在；预期结果：返回分组ID；测试点：获取分组ID成功")
        void getParticularGroupIdByLevel_WhenGroupExists_ShouldReturnId() {
            GroupDO group = new GroupDO();
            group.setId(1);
            when(groupMapper.selectOne(any())).thenReturn(group);

            Integer result = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);

            assertThat(result).isEqualTo(1);
        }
    }
}
