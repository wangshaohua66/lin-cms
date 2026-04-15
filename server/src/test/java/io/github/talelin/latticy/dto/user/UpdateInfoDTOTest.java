package io.github.talelin.latticy.dto.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("更新用户信息DTO测试")
class UpdateInfoDTOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            UpdateInfoDTO dto = new UpdateInfoDTO();

            dto.setEmail("test@example.com");
            dto.setNickname("测试昵称");
            dto.setUsername("testuser");
            dto.setAvatar("http://example.com/avatar.jpg");

            assertThat(dto.getEmail()).isEqualTo("test@example.com");
            assertThat(dto.getNickname()).isEqualTo("测试昵称");
            assertThat(dto.getUsername()).isEqualTo("testuser");
            assertThat(dto.getAvatar()).isEqualTo("http://example.com/avatar.jpg");
        }

        @Test
        @DisplayName("Setter/Getter - 空值")
        void setterGetter_WithNullValues_ShouldWorkCorrectly() {
            UpdateInfoDTO dto = new UpdateInfoDTO();

            dto.setEmail(null);
            dto.setNickname(null);
            dto.setUsername(null);
            dto.setAvatar(null);

            assertThat(dto.getEmail()).isNull();
            assertThat(dto.getNickname()).isNull();
            assertThat(dto.getUsername()).isNull();
            assertThat(dto.getAvatar()).isNull();
        }
    }
}
