package io.github.talelin.latticy.model;

import io.github.talelin.latticy.common.enumeration.GroupLevelEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("用户DO测试")
class UserDOTest {

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            UserDO user = UserDO.builder()
                    .username("testuser")
                    .nickname("测试用户")
                    .avatar("http://example.com/avatar.jpg")
                    .email("test@example.com")
                    .build();

            assertThat(user.getUsername()).isEqualTo("testuser");
            assertThat(user.getNickname()).isEqualTo("测试用户");
            assertThat(user.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
            assertThat(user.getEmail()).isEqualTo("test@example.com");
        }

        @Test
        @DisplayName("Builder - 最小字段")
        void builder_WithMinimalFields_ShouldBuildSuccessfully() {
            UserDO user = UserDO.builder()
                    .username("testuser")
                    .build();

            assertThat(user.getUsername()).isEqualTo("testuser");
            assertThat(user.getNickname()).isNull();
            assertThat(user.getAvatar()).isNull();
            assertThat(user.getEmail()).isNull();
        }

        @Test
        @DisplayName("Builder - 空值")
        void builder_WithNullValues_ShouldBuildSuccessfully() {
            UserDO user = UserDO.builder().build();

            assertThat(user.getUsername()).isNull();
            assertThat(user.getNickname()).isNull();
            assertThat(user.getAvatar()).isNull();
            assertThat(user.getEmail()).isNull();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            UserDO user = new UserDO();

            user.setUsername("newuser");
            user.setNickname("新用户");
            user.setAvatar("http://example.com/new.jpg");
            user.setEmail("new@example.com");

            assertThat(user.getUsername()).isEqualTo("newuser");
            assertThat(user.getNickname()).isEqualTo("新用户");
            assertThat(user.getAvatar()).isEqualTo("http://example.com/new.jpg");
            assertThat(user.getEmail()).isEqualTo("new@example.com");
        }

        @Test
        @DisplayName("Setter/Getter - 空字符串")
        void setterGetter_WithEmptyStrings_ShouldWorkCorrectly() {
            UserDO user = new UserDO();

            user.setUsername("");
            user.setNickname("");
            user.setAvatar("");
            user.setEmail("");

            assertThat(user.getUsername()).isEmpty();
            assertThat(user.getNickname()).isEmpty();
            assertThat(user.getAvatar()).isEmpty();
            assertThat(user.getEmail()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Equals/HashCode测试")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("Equals - 相同对象")
        void equals_WithSameObject_ShouldReturnTrue() {
            UserDO user = UserDO.builder().username("test").build();

            assertThat(user.equals(user)).isTrue();
            assertThat(user.hashCode()).isEqualTo(user.hashCode());
        }

        @Test
        @DisplayName("Equals - null对象")
        void equals_WithNull_ShouldReturnFalse() {
            UserDO user = UserDO.builder().username("test").build();

            assertThat(user.equals(null)).isFalse();
        }

        @Test
        @DisplayName("Equals - 不同类型")
        void equals_WithDifferentType_ShouldReturnFalse() {
            UserDO user = UserDO.builder().username("test").build();

            assertThat(user.equals("string")).isFalse();
        }
    }

    @Nested
    @DisplayName("无参构造函数测试")
    class NoArgsConstructorTests {

        @Test
        @DisplayName("无参构造函数 - 创建对象")
        void noArgsConstructor_ShouldCreateObject() {
            UserDO user = new UserDO();

            assertThat(user).isNotNull();
            assertThat(user.getUsername()).isNull();
            assertThat(user.getNickname()).isNull();
            assertThat(user.getAvatar()).isNull();
            assertThat(user.getEmail()).isNull();
        }
    }

    @Nested
    @DisplayName("全参构造函数测试")
    class AllArgsConstructorTests {

        @Test
        @DisplayName("全参构造函数 - 创建对象")
        void allArgsConstructor_ShouldCreateObject() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");
            user.setNickname("测试用户");
            user.setAvatar("http://example.com/avatar.jpg");
            user.setEmail("test@example.com");

            assertThat(user.getUsername()).isEqualTo("testuser");
            assertThat(user.getNickname()).isEqualTo("测试用户");
            assertThat(user.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
            assertThat(user.getEmail()).isEqualTo("test@example.com");
        }
    }
}
