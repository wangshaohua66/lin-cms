package io.github.talelin.latticy.vo;

import io.github.talelin.latticy.model.UserDO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("用户权限VO测试")
class UserPermissionVOTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("构造函数 - UserDO和权限列表")
        void constructor_WithUserAndPermissions_ShouldCreateVO() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setNickname("测试用户");
            user.setEmail("test@example.com");
            user.setAvatar("http://example.com/avatar.jpg");

            List<Map<String, List<Map<String, String>>>> permissions = Collections.emptyList();

            UserPermissionVO vo = new UserPermissionVO(user, permissions);

            assertThat(vo.getId()).isEqualTo(1);
            assertThat(vo.getNickname()).isEqualTo("测试用户");
            assertThat(vo.getEmail()).isEqualTo("test@example.com");
            assertThat(vo.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
            assertThat(vo.getPermissions()).isEmpty();
        }

        @Test
        @DisplayName("构造函数 - 仅UserDO")
        void constructor_WithUserOnly_ShouldCreateVO() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setNickname("测试用户");

            UserPermissionVO vo = new UserPermissionVO(user);

            assertThat(vo.getId()).isEqualTo(1);
            assertThat(vo.getNickname()).isEqualTo("测试用户");
        }

        @Test
        @DisplayName("无参构造函数")
        void noArgsConstructor_ShouldCreateVO() {
            UserPermissionVO vo = new UserPermissionVO();

            assertThat(vo).isNotNull();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            UserPermissionVO vo = new UserPermissionVO();

            vo.setId(1);
            vo.setNickname("测试用户");
            vo.setAvatar("http://example.com/avatar.jpg");
            vo.setAdmin(true);
            vo.setEmail("test@example.com");
            vo.setPermissions(Collections.emptyList());

            assertThat(vo.getId()).isEqualTo(1);
            assertThat(vo.getNickname()).isEqualTo("测试用户");
            assertThat(vo.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
            assertThat(vo.getAdmin()).isTrue();
            assertThat(vo.getEmail()).isEqualTo("test@example.com");
            assertThat(vo.getPermissions()).isEmpty();
        }
    }
}
