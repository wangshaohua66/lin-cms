package io.github.talelin.latticy.service.impl;

import io.github.talelin.autoconfigure.exception.ForbiddenException;
import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.dto.admin.*;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.model.UserDO;
import io.github.talelin.latticy.service.AdminService;
import io.github.talelin.latticy.service.GroupService;
import io.github.talelin.latticy.service.PermissionService;
import io.github.talelin.latticy.service.UserService;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("管理员服务集成测试")
class AdminServiceImplIntegrationTest {

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private PermissionService permissionService;

    @Nested
    @DisplayName("获取用户分页测试")
    class GetUserPageTests {

        @Test
        @DisplayName("获取用户分页 - 无分组ID")
        void getUserPageByGroupId_WithoutGroupId_ShouldReturnAllUsers() {
            var page = adminService.getUserPageByGroupId(null, 10, 1);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isNotNull();
        }

        @Test
        @DisplayName("获取用户分页 - 有分组ID")
        void getUserPageByGroupId_WithGroupId_ShouldReturnUsers() {
            var page = adminService.getUserPageByGroupId(1, 10, 1);

            assertThat(page).isNotNull();
        }

        @Test
        @DisplayName("获取用户分页 - 不存在的分组ID")
        void getUserPageByGroupId_WithNonExistentGroupId_ShouldReturnEmpty() {
            var page = adminService.getUserPageByGroupId(99999, 10, 1);

            assertThat(page.getRecords()).isEmpty();
        }

        @Test
        @DisplayName("获取用户分页 - 大页码")
        void getUserPageByGroupId_WithLargePageNumber_ShouldReturnEmpty() {
            var page = adminService.getUserPageByGroupId(null, 10, 1000);

            assertThat(page.getRecords()).isEmpty();
        }
    }

    @Nested
    @DisplayName("修改用户密码测试")
    class ChangeUserPasswordTests {

        @Test
        @DisplayName("修改用户密码 - 不存在的用户")
        void changeUserPassword_WhenUserNotExists_ShouldThrowException() {
            ResetPasswordDTO dto = new ResetPasswordDTO();
            dto.setNewPassword("newPassword123");

            assertThatThrownBy(() -> adminService.changeUserPassword(99999, dto))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("删除用户测试")
    class DeleteUserTests {

        @Test
        @DisplayName("删除用户 - 不存在的用户")
        void deleteUser_WhenUserNotExists_ShouldThrowException() {
            assertThatThrownBy(() -> adminService.deleteUser(99999))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("删除用户 - root用户")
        void deleteUser_WhenRootUser_ShouldThrowException() {
            Integer rootUserId = userService.getRootUserId();
            assertThatThrownBy(() -> adminService.deleteUser(rootUserId))
                    .isInstanceOf(ForbiddenException.class);
        }
    }

    @Nested
    @DisplayName("获取分组分页测试")
    class GetGroupPageTests {

        @Test
        @DisplayName("获取分组分页 - 正常获取")
        void getGroupPage_ShouldReturnPage() {
            var page = adminService.getGroupPage(1, 10);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isNotNull();
        }

        @Test
        @DisplayName("获取分组分页 - 大页码")
        void getGroupPage_WithLargePageNumber_ShouldReturnEmpty() {
            var page = adminService.getGroupPage(1000, 10);

            assertThat(page.getRecords()).isEmpty();
        }
    }

    @Nested
    @DisplayName("获取分组测试")
    class GetGroupTests {

        @Test
        @DisplayName("获取分组 - 不存在的分组")
        void getGroup_WhenGroupNotExists_ShouldThrowException() {
            assertThatThrownBy(() -> adminService.getGroup(99999))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("创建分组测试")
    class CreateGroupTests {

        @Test
        @DisplayName("创建分组 - 名称已存在")
        void createGroup_WhenNameExists_ShouldThrowException() {
            NewGroupDTO dto = new NewGroupDTO();
            dto.setName("root");
            dto.setInfo("测试分组");

            assertThatThrownBy(() -> adminService.createGroup(dto))
                    .isInstanceOf(ForbiddenException.class);
        }
    }

    @Nested
    @DisplayName("更新分组测试")
    class UpdateGroupTests {

        @Test
        @DisplayName("更新分组 - 不存在的分组")
        void updateGroup_WhenGroupNotExists_ShouldThrowException() {
            UpdateGroupDTO dto = new UpdateGroupDTO();
            dto.setName("新名称");
            dto.setInfo("新信息");

            assertThatThrownBy(() -> adminService.updateGroup(99999, dto))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        @DisplayName("更新分组 - 名称已存在")
        void updateGroup_WhenNameExists_ShouldThrowException() {
            UpdateGroupDTO dto = new UpdateGroupDTO();
            dto.setName("guest");
            dto.setInfo("新信息");

            assertThatThrownBy(() -> adminService.updateGroup(1, dto))
                    .isInstanceOf(ForbiddenException.class);
        }
    }

    @Nested
    @DisplayName("删除分组测试")
    class DeleteGroupTests {

        @Test
        @DisplayName("删除分组 - root分组")
        void deleteGroup_WhenRootGroup_ShouldThrowException() {
            Integer rootGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);
            assertThatThrownBy(() -> adminService.deleteGroup(rootGroupId))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("删除分组 - guest分组")
        void deleteGroup_WhenGuestGroup_ShouldThrowException() {
            Integer guestGroupId = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);
            assertThatThrownBy(() -> adminService.deleteGroup(guestGroupId))
                    .isInstanceOf(ForbiddenException.class);
        }

        @Test
        @DisplayName("删除分组 - 不存在的分组")
        void deleteGroup_WhenGroupNotExists_ShouldThrowException() {
            assertThatThrownBy(() -> adminService.deleteGroup(99999))
                    .isInstanceOf(NotFoundException.class);
        }
    }

    @Nested
    @DisplayName("分配权限测试")
    class DispatchPermissionTests {

        @Test
        @DisplayName("分配单个权限 - 正常分配")
        void dispatchPermission_ShouldSucceed() {
            DispatchPermissionDTO dto = new DispatchPermissionDTO();
            dto.setGroupId(3);
            dto.setPermissionId(5);

            boolean result = adminService.dispatchPermission(dto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("分配多个权限 - 正常分配")
        void dispatchPermissions_ShouldSucceed() {
            DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
            dto.setGroupId(3);
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            boolean result = adminService.dispatchPermissions(dto);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("获取所有分组测试")
    class GetAllGroupsTests {

        @Test
        @DisplayName("获取所有分组 - 正常获取")
        void getAllGroups_ShouldReturnList() {
            List<GroupDO> groups = adminService.getAllGroups();

            assertThat(groups).isNotNull();
            assertThat(groups).isNotEmpty();
        }
    }

    @Nested
    @DisplayName("获取所有权限测试")
    class GetAllPermissionsTests {

        @Test
        @DisplayName("获取所有权限 - 正常获取")
        void getAllPermissions_ShouldReturnList() {
            List<PermissionDO> permissions = adminService.getAllPermissions();

            assertThat(permissions).isNotNull();
        }

        @Test
        @DisplayName("获取所有结构化权限 - 正常获取")
        void getAllStructuralPermissions_ShouldReturnMap() {
            Map<String, List<PermissionDO>> permissions = adminService.getAllStructuralPermissions();

            assertThat(permissions).isNotNull();
        }
    }
}
