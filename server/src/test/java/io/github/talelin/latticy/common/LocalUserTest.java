package io.github.talelin.latticy.common;

import io.github.talelin.latticy.model.UserDO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LocalUser 单元测试
 * 测试点：线程本地用户存储功能
 */
@DisplayName("本地用户工具类测试")
class LocalUserTest {

    private UserDO userDO;

    @BeforeEach
    void setUp() {
        userDO = new UserDO();
        userDO.setId(1);
        userDO.setUsername("testuser");
        userDO.setNickname("测试用户");
    }

    @AfterEach
    void tearDown() {
        LocalUser.clearLocalUser();
    }

    @Test
    @DisplayName("设置和获取本地用户 - 正常情况")
    void setAndGetLocalUser_WithValidUser_ShouldWork() {
        // 输入参数：有效用户
        // 预期结果：能够正确设置和获取
        // 测试点：验证本地用户存储
        LocalUser.setLocalUser(userDO);

        UserDO result = LocalUser.getLocalUser();

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("获取本地用户 - 未设置时返回null")
    void getLocalUser_WhenNotSet_ShouldReturnNull() {
        // 输入参数：无
        // 预期结果：返回null
        // 测试点：验证未设置时的处理
        LocalUser.clearLocalUser();

        UserDO result = LocalUser.getLocalUser();

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("清除本地用户 - 正常情况")
    void clearLocalUser_AfterSet_ShouldReturnNull() {
        // 输入参数：已设置的用户
        // 预期结果：清除后返回null
        // 测试点：验证清除功能
        LocalUser.setLocalUser(userDO);
        assertThat(LocalUser.getLocalUser()).isNotNull();

        LocalUser.clearLocalUser();

        assertThat(LocalUser.getLocalUser()).isNull();
    }

    @Test
    @DisplayName("获取本地用户 - 指定类型")
    void getLocalUser_WithClassType_ShouldReturnTypedUser() {
        // 输入参数：UserDO.class
        // 预期结果：返回指定类型的用户
        // 测试点：验证类型转换获取
        LocalUser.setLocalUser(userDO);

        UserDO result = LocalUser.getLocalUser(UserDO.class);

        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("设置本地用户 - null值")
    void setLocalUser_WithNull_ShouldWork() {
        // 输入参数：null
        // 预期结果：正常设置，获取返回null
        // 测试点：验证null值处理
        LocalUser.setLocalUser(null);

        UserDO result = LocalUser.getLocalUser();

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("多次设置本地用户 - 覆盖")
    void setLocalUser_MultipleTimes_ShouldOverride() {
        // 输入参数：多个用户
        // 预期结果：最后一次设置生效
        // 测试点：验证覆盖逻辑
        UserDO user1 = new UserDO();
        user1.setId(1);
        user1.setUsername("user1");

        UserDO user2 = new UserDO();
        user2.setId(2);
        user2.setUsername("user2");

        LocalUser.setLocalUser(user1);
        LocalUser.setLocalUser(user2);

        UserDO result = LocalUser.getLocalUser();

        assertThat(result.getId()).isEqualTo(2);
        assertThat(result.getUsername()).isEqualTo("user2");
    }

    @Test
    @DisplayName("线程隔离 - 不同线程独立")
    void threadIsolation_DifferentThreads_ShouldBeIndependent() throws InterruptedException {
        // 输入参数：不同线程设置不同用户
        // 预期结果：线程间互不影响
        // 测试点：验证线程隔离性
        LocalUser.setLocalUser(userDO);

        final UserDO[] otherThreadUser = new UserDO[1];
        Thread otherThread = new Thread(() -> {
            UserDO otherUser = new UserDO();
            otherUser.setId(99);
            otherUser.setUsername("other");
            LocalUser.setLocalUser(otherUser);
            otherThreadUser[0] = LocalUser.getLocalUser();
        });

        otherThread.start();
        otherThread.join();

        // 主线程用户不变
        assertThat(LocalUser.getLocalUser().getId()).isEqualTo(1);
        // 子线程用户正确
        assertThat(otherThreadUser[0].getId()).isEqualTo(99);
    }

    @Test
    @DisplayName("私有构造函数 - 工具类不可实例化")
    void privateConstructor_UtilityClass_ShouldNotBeInstantiated() {
        // 测试点：验证工具类私有构造函数
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<LocalUser> constructor = LocalUser.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
