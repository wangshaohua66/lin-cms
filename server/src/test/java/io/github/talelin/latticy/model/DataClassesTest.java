package io.github.talelin.latticy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Model数据类单元测试")
class DataClassesTest {

    @Test
    @DisplayName("输入参数：BookDO属性；预期结果：getter/setter正常工作；测试点：BookDO数据类测试")
    void bookDO_GettersAndSetters_ShouldWorkCorrectly() {
        BookDO book = new BookDO();
        book.setId(1);
        book.setTitle("Test Book");
        book.setAuthor("Test Author");
        book.setImage("test.jpg");
        book.setSummary("Test Summary");

        assertThat(book.getId()).isEqualTo(1);
        assertThat(book.getTitle()).isEqualTo("Test Book");
        assertThat(book.getAuthor()).isEqualTo("Test Author");
    }

    @Test
    @DisplayName("输入参数：UserDO属性；预期结果：getter/setter正常工作；测试点：UserDO数据类测试")
    void userDO_GettersAndSetters_ShouldWorkCorrectly() {
        UserDO user = new UserDO();
        user.setId(1);
        user.setUsername("testuser");
        user.setNickname("Test User");
        user.setEmail("test@example.com");

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("输入参数：GroupDO属性；预期结果：getter/setter正常工作；测试点：GroupDO数据类测试")
    void groupDO_GettersAndSetters_ShouldWorkCorrectly() {
        GroupDO group = GroupDO.builder()
                .name("admin")
                .info("Administrator group")
                .build();
        group.setId(1);

        assertThat(group.getId()).isEqualTo(1);
        assertThat(group.getName()).isEqualTo("admin");
    }

    @Test
    @DisplayName("输入参数：PermissionDO属性；预期结果：getter/setter正常工作；测试点：PermissionDO数据类测试")
    void permissionDO_GettersAndSetters_ShouldWorkCorrectly() {
        PermissionDO permission = new PermissionDO();
        permission.setId(1);
        permission.setName("create user");
        permission.setModule("user");
        permission.setMount(true);

        assertThat(permission.getId()).isEqualTo(1);
        assertThat(permission.getName()).isEqualTo("create user");
        assertThat(permission.getModule()).isEqualTo("user");
    }

    @Test
    @DisplayName("输入参数：LogDO属性；预期结果：getter/setter正常工作；测试点：LogDO数据类测试")
    void logDO_GettersAndSetters_ShouldWorkCorrectly() {
        LogDO log = LogDO.builder()
                .message("User logged in")
                .username("admin")
                .statusCode(200)
                .method("POST")
                .path("/login")
                .build();
        log.setId(1);

        assertThat(log.getId()).isEqualTo(1);
        assertThat(log.getMessage()).isEqualTo("User logged in");
    }

    @Test
    @DisplayName("输入参数：FileDO属性；预期结果：getter/setter正常工作；测试点：FileDO数据类测试")
    void fileDO_GettersAndSetters_ShouldWorkCorrectly() {
        FileDO file = new FileDO();
        file.setId(1);
        file.setName("test.txt");
        file.setPath("/files/test.txt");
        file.setSize(1024);

        assertThat(file.getId()).isEqualTo(1);
        assertThat(file.getName()).isEqualTo("test.txt");
    }

    @Test
    @DisplayName("输入参数：UserGroupDO属性；预期结果：getter/setter正常工作；测试点：UserGroupDO数据类测试")
    void userGroupDO_GettersAndSetters_ShouldWorkCorrectly() {
        UserGroupDO userGroup = new UserGroupDO(1, 2);

        assertThat(userGroup.getUserId()).isEqualTo(1);
        assertThat(userGroup.getGroupId()).isEqualTo(2);
    }

    @Test
    @DisplayName("输入参数：UserIdentityDO属性；预期结果：getter/setter正常工作；测试点：UserIdentityDO数据类测试")
    void userIdentityDO_GettersAndSetters_ShouldWorkCorrectly() {
        UserIdentityDO identity = UserIdentityDO.builder()
                .userId(1)
                .identityType("USERNAME_PASSWORD")
                .identifier("testuser")
                .credential("encrypted_pass")
                .build();
        identity.setId(1);

        assertThat(identity.getId()).isEqualTo(1);
        assertThat(identity.getUserId()).isEqualTo(1);
    }

    @Test
    @DisplayName("输入参数：GroupPermissionDO属性；预期结果：getter/setter正常工作；测试点：GroupPermissionDO数据类测试")
    void groupPermissionDO_GettersAndSetters_ShouldWorkCorrectly() {
        GroupPermissionDO groupPermission = new GroupPermissionDO(1, 2);

        assertThat(groupPermission.getGroupId()).isEqualTo(1);
        assertThat(groupPermission.getPermissionId()).isEqualTo(2);
    }
}
