package io.github.talelin.latticy.service.impl;


import io.github.talelin.latticy.mapper.PermissionMapper;
import io.github.talelin.latticy.model.PermissionDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("PermissionService单元测试")
class PermissionServiceImplTest {

    @Mock
    private PermissionMapper permissionMapper;

    @InjectMocks
    private PermissionServiceImpl permissionService;

    @BeforeEach
    void setUp() {
        injectBaseMapper(permissionService, permissionMapper);
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
    @DisplayName("分组权限查询测试")
    class GroupPermissionQueryTests {

        @Test
        @DisplayName("输入参数：有效分组ID；预期结果：返回权限列表；测试点：按分组ID查询权限")
        void getPermissionByGroupId_WithValidId_ShouldReturnPermissions() {
            List<PermissionDO> expected = Arrays.asList(new PermissionDO(), new PermissionDO());
            when(permissionMapper.selectPermissionsByGroupId(eq(1))).thenReturn(expected);

            List<PermissionDO> result = permissionService.getPermissionByGroupId(1);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：不存在的分组ID；预期结果：返回空列表；测试点：分组无权限场景")
        void getPermissionByGroupId_WithNonExistentId_ShouldReturnEmpty() {
            when(permissionMapper.selectPermissionsByGroupId(eq(999))).thenReturn(Collections.emptyList());

            List<PermissionDO> result = permissionService.getPermissionByGroupId(999);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("输入参数：分组ID列表；预期结果：返回所有权限；测试点：按多分组查询权限")
        void getPermissionByGroupIds_WithValidIds_ShouldReturnPermissions() {
            List<Integer> groupIds = Arrays.asList(1, 2, 3);
            List<PermissionDO> expected = Arrays.asList(new PermissionDO(), new PermissionDO(), new PermissionDO());
            when(permissionMapper.selectPermissionsByGroupIds(eq(groupIds))).thenReturn(expected);

            List<PermissionDO> result = permissionService.getPermissionByGroupIds(groupIds);

            assertThat(result).hasSize(3);
        }

        @Test
        @DisplayName("输入参数：分组ID列表和模块；预期结果：返回指定模块权限；测试点：按分组和模块过滤权限")
        void getPermissionByGroupIdsAndModule_WithValidParams_ShouldReturnFilteredPermissions() {
            List<Integer> groupIds = Collections.singletonList(1);
            List<PermissionDO> expected = Collections.singletonList(new PermissionDO());
            when(permissionMapper.selectPermissionsByGroupIdsAndModule(eq(groupIds), eq("user"))).thenReturn(expected);

            List<PermissionDO> result = permissionService.getPermissionByGroupIdsAndModule(groupIds, "user");

            assertThat(result).hasSize(1);
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("输入参数：null/空模块名；预期结果：返回对应权限；测试点：边界值模块名处理")
        void getPermissionByGroupIdsAndModule_WithNullOrEmptyModule_ShouldHandle(String module) {
            List<Integer> groupIds = Collections.singletonList(1);
            when(permissionMapper.selectPermissionsByGroupIdsAndModule(eq(groupIds), eq(module))).thenReturn(Collections.emptyList());

            List<PermissionDO> result = permissionService.getPermissionByGroupIdsAndModule(groupIds, module);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("权限Map测试")
    class PermissionMapTests {

        @Test
        @DisplayName("输入参数：分组ID列表；预期结果：返回权限Map；测试点：生成分组权限Map")
        void getPermissionMapByGroupIds_WithValidIds_ShouldReturnMap() {
            List<Integer> groupIds = Arrays.asList(1, 2);
            when(permissionMapper.selectPermissionsByGroupId(eq(1))).thenReturn(Collections.singletonList(new PermissionDO()));
            when(permissionMapper.selectPermissionsByGroupId(eq(2))).thenReturn(Arrays.asList(new PermissionDO(), new PermissionDO()));

            Map<Long, List<PermissionDO>> result = permissionService.getPermissionMapByGroupIds(groupIds);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：空分组ID列表；预期结果：返回空Map；测试点：空分组列表处理")
        void getPermissionMapByGroupIds_WithEmptyIds_ShouldReturnEmptyMap() {
            Map<Long, List<PermissionDO>> result = permissionService.getPermissionMapByGroupIds(Collections.emptyList());

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("权限结构化测试")
    class StructuringPermissionsTests {

        @Test
        @DisplayName("输入参数：多模块权限列表；预期结果：返回结构化权限；测试点：按模块组织权限结构")
        void structuringPermissions_WithMultipleModules_ShouldReturnStructured() {
            List<PermissionDO> permissions = new ArrayList<>();
            PermissionDO p1 = new PermissionDO();
            p1.setModule("user");
            p1.setName("create");
            PermissionDO p2 = new PermissionDO();
            p2.setModule("user");
            p2.setName("delete");
            PermissionDO p3 = new PermissionDO();
            p3.setModule("book");
            p3.setName("query");
            permissions.add(p1);
            permissions.add(p2);
            permissions.add(p3);

            List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(permissions);

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：空权限列表；预期结果：返回空列表；测试点：空权限列表处理")
        void structuringPermissions_WithEmptyList_ShouldReturnEmptyList() {
            List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(Collections.emptyList());

            assertThat(result).isEmpty();
        }



        @Test
        @DisplayName("输入参数：多模块权限列表；预期结果：返回简化结构化权限；测试点：简化权限结构处理")
        void structuringPermissionsSimply_WithMultipleModules_ShouldReturnSimplified() {
            List<PermissionDO> permissions = new ArrayList<>();
            PermissionDO p1 = new PermissionDO();
            p1.setModule("user");
            p1.setName("create");
            PermissionDO p2 = new PermissionDO();
            p2.setModule("user");
            p2.setName("delete");
            PermissionDO p3 = new PermissionDO();
            p3.setModule("book");
            p3.setName("query");
            permissions.add(p1);
            permissions.add(p2);
            permissions.add(p3);

            Map<String, List<String>> result = permissionService.structuringPermissionsSimply(permissions);

            assertThat(result).containsKeys("user", "book");
            assertThat(result.get("user")).containsExactly("create", "delete");
            assertThat(result.get("book")).containsExactly("query");
        }

        @Test
        @DisplayName("输入参数：空权限列表；预期结果：返回空Map；测试点：空列表简化处理")
        void structuringPermissionsSimply_WithEmptyList_ShouldReturnEmptyMap() {
            Map<String, List<String>> result = permissionService.structuringPermissionsSimply(Collections.emptyList());

            assertThat(result).isEmpty();
        }


    }
}
