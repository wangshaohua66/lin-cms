package io.github.talelin.latticy.model;

import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Model类单元测试
 * 测试所有Model的getter/setter
 */
@DisplayName("Model类测试")
class ModelTest {

    @Test
    @DisplayName("BookDO - 测试")
    void bookDO_Test() {
        BookDO book = new BookDO();
        book.setId(1);
        book.setTitle("测试图书");
        book.setAuthor("测试作者");
        book.setSummary("测试摘要");
        book.setImage("test.jpg");

        assertThat(book.getId()).isEqualTo(1);
        assertThat(book.getTitle()).isEqualTo("测试图书");
        assertThat(book.getAuthor()).isEqualTo("测试作者");
        assertThat(book.getSummary()).isEqualTo("测试摘要");
        assertThat(book.getImage()).isEqualTo("test.jpg");
    }

    @Test
    @DisplayName("UserDO - 测试")
    void userDO_Test() {
        UserDO user = new UserDO();
        user.setId(1);
        user.setUsername("testuser");
        user.setNickname("测试用户");
        user.setEmail("test@example.com");
        user.setAvatar("avatar.jpg");

        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getUsername()).isEqualTo("testuser");
        assertThat(user.getNickname()).isEqualTo("测试用户");
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getAvatar()).isEqualTo("avatar.jpg");
    }

    @Test
    @DisplayName("GroupDO - 测试")
    void groupDO_Test() {
        GroupDO group = new GroupDO();
        group.setId(1);
        group.setName("测试分组");
        group.setInfo("分组信息");
        group.setLevel(GroupLevelEnum.USER);

        assertThat(group.getId()).isEqualTo(1);
        assertThat(group.getName()).isEqualTo("测试分组");
        assertThat(group.getInfo()).isEqualTo("分组信息");
        assertThat(group.getLevel()).isEqualTo(GroupLevelEnum.USER);
    }

    @Test
    @DisplayName("PermissionDO - 测试")
    void permissionDO_Test() {
        PermissionDO permission = new PermissionDO();
        permission.setId(1);
        permission.setName("查看用户");
        permission.setModule("用户管理");
        permission.setMount(true);

        assertThat(permission.getId()).isEqualTo(1);
        assertThat(permission.getName()).isEqualTo("查看用户");
        assertThat(permission.getModule()).isEqualTo("用户管理");
        assertThat(permission.getMount()).isTrue();
    }

    @Test
    @DisplayName("UserGroupDO - 测试")
    void userGroupDO_Test() {
        UserGroupDO userGroup = new UserGroupDO(1, 2);
        userGroup.setId(1);

        assertThat(userGroup.getId()).isEqualTo(1);
        assertThat(userGroup.getUserId()).isEqualTo(1);
        assertThat(userGroup.getGroupId()).isEqualTo(2);
    }

    @Test
    @DisplayName("GroupPermissionDO - 测试")
    void groupPermissionDO_Test() {
        GroupPermissionDO gp = new GroupPermissionDO(1, 2);
        gp.setId(1);

        assertThat(gp.getId()).isEqualTo(1);
        assertThat(gp.getGroupId()).isEqualTo(1);
        assertThat(gp.getPermissionId()).isEqualTo(2);
    }

    @Test
    @DisplayName("UserIdentityDO - 测试")
    void userIdentityDO_Test() {
        UserIdentityDO identity = new UserIdentityDO();
        identity.setId(1);
        identity.setUserId(1);
        identity.setIdentityType("USERNAME_PASSWORD");
        identity.setIdentifier("testuser");
        identity.setCredential("password");

        assertThat(identity.getId()).isEqualTo(1);
        assertThat(identity.getUserId()).isEqualTo(1);
        assertThat(identity.getIdentityType()).isEqualTo("USERNAME_PASSWORD");
        assertThat(identity.getIdentifier()).isEqualTo("testuser");
        assertThat(identity.getCredential()).isEqualTo("password");
    }

    @Test
    @DisplayName("LogDO - 测试")
    void logDO_Test() {
        LogDO log = new LogDO();
        log.setId(1);
        log.setUserId(1);
        log.setUsername("testuser");
        log.setMessage("查看用户");
        log.setMethod("GET");
        log.setPath("/api/user");
        log.setStatusCode(200);
        log.setPermission("查看用户");

        assertThat(log.getId()).isEqualTo(1);
        assertThat(log.getUserId()).isEqualTo(1);
        assertThat(log.getUsername()).isEqualTo("testuser");
        assertThat(log.getMessage()).isEqualTo("查看用户");
        assertThat(log.getMethod()).isEqualTo("GET");
        assertThat(log.getPath()).isEqualTo("/api/user");
        assertThat(log.getStatusCode()).isEqualTo(200);
        assertThat(log.getPermission()).isEqualTo("查看用户");
    }

    @Test
    @DisplayName("FileDO - 测试")
    void fileDO_Test() {
        FileDO file = new FileDO();
        file.setId(1);
        file.setName("test.jpg");
        file.setPath("/upload/test.jpg");
        file.setExtension("jpg");
        file.setSize(1024);
        file.setMd5("abc123");
        file.setType("LOCAL");

        assertThat(file.getId()).isEqualTo(1);
        assertThat(file.getName()).isEqualTo("test.jpg");
        assertThat(file.getPath()).isEqualTo("/upload/test.jpg");
        assertThat(file.getExtension()).isEqualTo("jpg");
        assertThat(file.getSize()).isEqualTo(1024);
        assertThat(file.getMd5()).isEqualTo("abc123");
        assertThat(file.getType()).isEqualTo("LOCAL");
    }

    @Test
    @DisplayName("BaseModel - 测试")
    void baseModel_Test() {
        BookDO book = new BookDO();
        Date now = new Date();
        book.setCreateTime(now);
        book.setUpdateTime(now);
        book.setDeleteTime(null);

        assertThat(book.getCreateTime()).isEqualTo(now);
        assertThat(book.getUpdateTime()).isEqualTo(now);
        assertThat(book.getDeleteTime()).isNull();
    }
}
