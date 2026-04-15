package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.service.PermissionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("权限服务集成测试")
class PermissionServiceImplIntegrationTest {

    @Autowired
    private PermissionService permissionService;

    @Nested
    @DisplayName("获取分组权限测试")
    class GetPermissionByGroupIdTests {

        @Test
        @DisplayName("获取分组权限 - 存在的分组")
        void getPermissionByGroupId_WhenGroupExists_ShouldReturnPermissions() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupId(1);

            assertThat(permissions).isNotNull();
        }

        @Test
        @DisplayName("获取分组权限 - 不存在的分组")
        void getPermissionByGroupId_WhenGroupNotExists_ShouldReturnEmptyList() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupId(99999);

            assertThat(permissions).isEmpty();
        }
    }

    @Nested
    @DisplayName("获取多分组权限测试")
    class GetPermissionByGroupIdsTests {

        @Test
        @DisplayName("获取多分组权限 - 存在的分组")
        void getPermissionByGroupIds_WhenGroupsExist_ShouldReturnPermissions() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupIds(Arrays.asList(1, 2));

            assertThat(permissions).isNotNull();
        }

        @Test
        @DisplayName("获取多分组权限 - 不存在的分组")
        void getPermissionByGroupIds_WhenGroupsNotExist_ShouldReturnEmptyList() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupIds(Arrays.asList(99999));

            assertThat(permissions).isEmpty();
        }
    }

    @Nested
    @DisplayName("获取权限Map测试")
    class GetPermissionMapTests {

        @Test
        @DisplayName("获取权限Map - 存在的分组")
        void getPermissionMapByGroupIds_WhenGroupsExist_ShouldReturnMap() {
            Map<Long, List<PermissionDO>> map = permissionService.getPermissionMapByGroupIds(Arrays.asList(1, 2));

            assertThat(map).isNotNull();
            assertThat(map).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("结构化权限测试")
    class StructuringPermissionsTests {

        @Test
        @DisplayName("结构化权限 - 有权限")
        void structuringPermissions_WithPermissions_ShouldReturnStructuredList() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupId(1);

            List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(permissions);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("结构化权限 - 空列表")
        void structuringPermissions_WithEmptyList_ShouldReturnEmptyList() {
            List<Map<String, List<Map<String, String>>>> result = permissionService.structuringPermissions(Collections.emptyList());

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("结构化权限简化版 - 有权限")
        void structuringPermissionsSimply_WithPermissions_ShouldReturnMap() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupId(1);

            Map<String, List<String>> result = permissionService.structuringPermissionsSimply(permissions);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("结构化权限简化版 - 空列表")
        void structuringPermissionsSimply_WithEmptyList_ShouldReturnEmptyMap() {
            Map<String, List<String>> result = permissionService.structuringPermissionsSimply(Collections.emptyList());

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("根据模块获取权限测试")
    class GetPermissionByModuleTests {

        @Test
        @DisplayName("根据模块获取权限 - 存在的模块")
        void getPermissionByGroupIdsAndModule_WhenModuleExists_ShouldReturnPermissions() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupIdsAndModule(Arrays.asList(1, 2), "图书");

            assertThat(permissions).isNotNull();
        }

        @Test
        @DisplayName("根据模块获取权限 - 不存在的模块")
        void getPermissionByGroupIdsAndModule_WhenModuleNotExists_ShouldReturnEmptyList() {
            List<PermissionDO> permissions = permissionService.getPermissionByGroupIdsAndModule(Arrays.asList(1, 2), "不存在的模块");

            assertThat(permissions).isEmpty();
        }
    }
}
