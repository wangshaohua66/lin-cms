package io.github.talelin.latticy.vo;

import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.UserDO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("用户信息VO测试")
class UserInfoVOTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("构造函数 - UserDO和分组列表")
        void constructor_WithUserAndGroups_ShouldCreateVO() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");
            user.setNickname("测试用户");
            user.setEmail("test@example.com");
            user.setAvatar("http://example.com/avatar.jpg");

            GroupDO group = GroupDO.builder().name("测试分组").build();

            UserInfoVO vo = new UserInfoVO(user, Arrays.asList(group));

            assertThat(vo.getId()).isEqualTo(1);
            assertThat(vo.getUsername()).isEqualTo("testuser");
            assertThat(vo.getNickname()).isEqualTo("测试用户");
            assertThat(vo.getEmail()).isEqualTo("test@example.com");
            assertThat(vo.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
            assertThat(vo.getGroups()).hasSize(1);
        }

        @Test
        @DisplayName("构造函数 - 空分组列表")
        void constructor_WithEmptyGroups_ShouldCreateVO() {
            UserDO user = new UserDO();
            user.setId(1);
            user.setUsername("testuser");

            UserInfoVO vo = new UserInfoVO(user, Collections.emptyList());

            assertThat(vo.getGroups()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            UserInfoVO vo = UserInfoVO.builder()
                    .id(1)
                    .username("testuser")
                    .nickname("测试用户")
                    .email("test@example.com")
                    .avatar("http://example.com/avatar.jpg")
                    .groups(Collections.emptyList())
                    .build();

            assertThat(vo.getId()).isEqualTo(1);
            assertThat(vo.getUsername()).isEqualTo("testuser");
            assertThat(vo.getNickname()).isEqualTo("测试用户");
            assertThat(vo.getEmail()).isEqualTo("test@example.com");
            assertThat(vo.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
            assertThat(vo.getGroups()).isEmpty();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            UserInfoVO vo = new UserInfoVO();

            vo.setId(1);
            vo.setUsername("testuser");
            vo.setNickname("测试用户");
            vo.setEmail("test@example.com");
            vo.setAvatar("http://example.com/avatar.jpg");
            vo.setGroups(Collections.emptyList());

            assertThat(vo.getId()).isEqualTo(1);
            assertThat(vo.getUsername()).isEqualTo("testuser");
            assertThat(vo.getNickname()).isEqualTo("测试用户");
            assertThat(vo.getEmail()).isEqualTo("test@example.com");
            assertThat(vo.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
            assertThat(vo.getGroups()).isEmpty();
        }
    }
}
