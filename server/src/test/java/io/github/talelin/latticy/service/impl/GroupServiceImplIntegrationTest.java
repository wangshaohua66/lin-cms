package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import io.github.talelin.latticy.service.GroupService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("分组服务集成测试")
class GroupServiceImplIntegrationTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private PermissionService permissionService;

    @Nested
    @DisplayName("获取用户分组测试")
    class GetUserGroupsTests {

        @Test
        @DisplayName("获取用户分组 - 存在的用户")
        void getUserGroupsByUserId_WhenUserExists_ShouldReturnGroups() {
            List<GroupDO> groups = groupService.getUserGroupsByUserId(1);

            assertThat(groups).isNotNull();
            assertThat(groups).isNotEmpty();
        }

        @Test
        @DisplayName("获取用户分组 - 不存在的用户")
        void getUserGroupsByUserId_WhenUserNotExists_ShouldReturnEmptyList() {
            List<GroupDO> groups = groupService.getUserGroupsByUserId(99999);

            assertThat(groups).isEmpty();
        }
    }

    @Nested
    @DisplayName("获取用户分组ID测试")
    class GetUserGroupIdsTests {

        @Test
        @DisplayName("获取用户分组ID - 存在的用户")
        void getUserGroupIdsByUserId_WhenUserExists_ShouldReturnIds() {
            List<Integer> ids = groupService.getUserGroupIdsByUserId(1);

            assertThat(ids).isNotNull();
            assertThat(ids).isNotEmpty();
        }

        @Test
        @DisplayName("获取用户分组ID - 不存在的用户")
        void getUserGroupIdsByUserId_WhenUserNotExists_ShouldReturnEmptyList() {
            List<Integer> ids = groupService.getUserGroupIdsByUserId(99999);

            assertThat(ids).isEmpty();
        }
    }

    @Nested
    @DisplayName("获取分组分页测试")
    class GetGroupPageTests {

        @Test
        @DisplayName("获取分组分页 - 第一页")
        void getGroupPage_FirstPage_ShouldReturnPage() {
            var page = groupService.getGroupPage(1, 10);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isNotNull();
        }

        @Test
        @DisplayName("获取分组分页 - 大页码")
        void getGroupPage_LargePageNumber_ShouldReturnEmptyPage() {
            var page = groupService.getGroupPage(1000, 10);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isEmpty();
        }

        @Test
        @DisplayName("获取分组分页 - 小页面大小")
        void getGroupPage_SmallPageSize_ShouldReturnPage() {
            var page = groupService.getGroupPage(1, 1);

            assertThat(page).isNotNull();
            assertThat(page.getRecords().size()).isLessThanOrEqualTo(1);
        }
    }

    @Nested
    @DisplayName("检查分组是否存在测试")
    class CheckGroupExistTests {

        @Test
        @DisplayName("检查分组存在 - 存在")
        void checkGroupExistById_WhenExists_ShouldReturnTrue() {
            boolean exists = groupService.checkGroupExistById(1);

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("检查分组存在 - 不存在")
        void checkGroupExistById_WhenNotExists_ShouldReturnFalse() {
            boolean exists = groupService.checkGroupExistById(99999);

            assertThat(exists).isFalse();
        }

        @Test
        @DisplayName("检查分组名称存在 - 存在")
        void checkGroupExistByName_WhenExists_ShouldReturnTrue() {
            boolean exists = groupService.checkGroupExistByName("root");

            assertThat(exists).isTrue();
        }

        @Test
        @DisplayName("检查分组名称存在 - 不存在")
        void checkGroupExistByName_WhenNotExists_ShouldReturnFalse() {
            boolean exists = groupService.checkGroupExistByName("不存在的分组名");

            assertThat(exists).isFalse();
        }
    }

    @Nested
    @DisplayName("获取分组及权限测试")
    class GetGroupAndPermissionsTests {

        @Test
        @DisplayName("获取分组及权限 - 存在的分组")
        void getGroupAndPermissions_WhenGroupExists_ShouldReturnBO() {
            var bo = groupService.getGroupAndPermissions(1);

            assertThat(bo).isNotNull();
            assertThat(bo.getName()).isNotNull();
        }
    }

    @Nested
    @DisplayName("检查是否为root用户测试")
    class CheckIsRootTests {

        @Test
        @DisplayName("检查是否为root用户 - 是root")
        void checkIsRootByUserId_WhenRoot_ShouldReturnTrue() {
            boolean isRoot = groupService.checkIsRootByUserId(1);

            assertThat(isRoot).isTrue();
        }

        @Test
        @DisplayName("检查是否为root用户 - 不是root")
        void checkIsRootByUserId_WhenNotRoot_ShouldReturnFalse() {
            boolean isRoot = groupService.checkIsRootByUserId(99999);

            assertThat(isRoot).isFalse();
        }
    }

    @Nested
    @DisplayName("用户分组关系测试")
    class UserGroupRelationTests {

        @Test
        @DisplayName("删除用户分组关系 - 空列表")
        void deleteUserGroupRelations_WithEmptyList_ShouldReturnTrue() {
            boolean result = groupService.deleteUserGroupRelations(1, Collections.emptyList());

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("删除用户分组关系 - null列表")
        void deleteUserGroupRelations_WithNullList_ShouldReturnTrue() {
            boolean result = groupService.deleteUserGroupRelations(1, null);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("添加用户分组关系 - 空列表")
        void addUserGroupRelations_WithEmptyList_ShouldReturnTrue() {
            boolean result = groupService.addUserGroupRelations(1, Collections.emptyList());

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("添加用户分组关系 - null列表")
        void addUserGroupRelations_WithNullList_ShouldReturnTrue() {
            boolean result = groupService.addUserGroupRelations(1, null);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("添加用户分组关系 - 不存在的分组ID")
        void addUserGroupRelations_WithNonExistentGroupId_ShouldThrowException() {
            assertThatThrownBy(() -> groupService.addUserGroupRelations(1, Arrays.asList(99999)))
                    .isInstanceOf(Exception.class);
        }
    }

    @Nested
    @DisplayName("获取分组用户ID测试")
    class GetGroupUserIdsTests {

        @Test
        @DisplayName("获取分组用户ID - 存在的分组")
        void getGroupUserIds_WhenGroupExists_ShouldReturnIds() {
            List<Integer> ids = groupService.getGroupUserIds(1);

            assertThat(ids).isNotNull();
        }

        @Test
        @DisplayName("获取分组用户ID - 不存在的分组")
        void getGroupUserIds_WhenGroupNotExists_ShouldReturnEmptyList() {
            List<Integer> ids = groupService.getGroupUserIds(99999);

            assertThat(ids).isEmpty();
        }
    }

    @Nested
    @DisplayName("根据级别获取分组测试")
    class GetParticularGroupTests {

        @Test
        @DisplayName("获取ROOT级别分组")
        void getParticularGroupByLevel_WhenRoot_ShouldReturnGroup() {
            GroupDO group = groupService.getParticularGroupByLevel(GroupLevelEnum.ROOT);

            assertThat(group).isNotNull();
            assertThat(group.getLevel()).isEqualTo(GroupLevelEnum.ROOT);
        }

        @Test
        @DisplayName("获取USER级别分组 - 返回null")
        void getParticularGroupByLevel_WhenUser_ShouldReturnNull() {
            GroupDO group = groupService.getParticularGroupByLevel(GroupLevelEnum.USER);

            assertThat(group).isNull();
        }

        @Test
        @DisplayName("获取GUEST级别分组")
        void getParticularGroupByLevel_WhenGuest_ShouldReturnGroup() {
            GroupDO group = groupService.getParticularGroupByLevel(GroupLevelEnum.GUEST);

            assertThat(group).isNotNull();
            assertThat(group.getLevel()).isEqualTo(GroupLevelEnum.GUEST);
        }

        @Test
        @DisplayName("获取ROOT级别分组ID")
        void getParticularGroupIdByLevel_WhenRoot_ShouldReturnId() {
            Integer id = groupService.getParticularGroupIdByLevel(GroupLevelEnum.ROOT);

            assertThat(id).isGreaterThan(0);
        }

        @Test
        @DisplayName("获取USER级别分组ID - 返回0")
        void getParticularGroupIdByLevel_WhenUser_ShouldReturnZero() {
            Integer id = groupService.getParticularGroupIdByLevel(GroupLevelEnum.USER);

            assertThat(id).isEqualTo(0);
        }

        @Test
        @DisplayName("获取GUEST级别分组ID")
        void getParticularGroupIdByLevel_WhenGuest_ShouldReturnId() {
            Integer id = groupService.getParticularGroupIdByLevel(GroupLevelEnum.GUEST);

            assertThat(id).isGreaterThan(0);
        }
    }

    @Nested
    @DisplayName("保存分组测试")
    class SaveGroupTests {

        @Test
        @DisplayName("保存分组 - 正常保存")
        void saveGroup_ShouldSaveSuccessfully() {
            GroupDO group = GroupDO.builder()
                    .name("测试分组_new")
                    .info("测试分组信息")
                    .level(GroupLevelEnum.USER)
                    .build();

            boolean result = groupService.save(group);

            assertThat(result).isTrue();
            assertThat(group.getId()).isNotNull();
        }
    }
}
