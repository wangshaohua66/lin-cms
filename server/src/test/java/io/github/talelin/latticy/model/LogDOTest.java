package io.github.talelin.latticy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("日志DO测试")
class LogDOTest {

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            LogDO log = LogDO.builder()
                    .message("测试日志")
                    .userId(1)
                    .username("testuser")
                    .statusCode(200)
                    .method("GET")
                    .path("/api/test")
                    .permission("查看")
                    .build();

            assertThat(log.getMessage()).isEqualTo("测试日志");
            assertThat(log.getUserId()).isEqualTo(1);
            assertThat(log.getUsername()).isEqualTo("testuser");
            assertThat(log.getStatusCode()).isEqualTo(200);
            assertThat(log.getMethod()).isEqualTo("GET");
            assertThat(log.getPath()).isEqualTo("/api/test");
            assertThat(log.getPermission()).isEqualTo("查看");
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            LogDO log = new LogDO();

            log.setMessage("测试消息");
            log.setUserId(1);
            log.setUsername("testuser");
            log.setStatusCode(200);
            log.setMethod("POST");
            log.setPath("/api/create");
            log.setPermission("创建");

            assertThat(log.getMessage()).isEqualTo("测试消息");
            assertThat(log.getUserId()).isEqualTo(1);
            assertThat(log.getUsername()).isEqualTo("testuser");
            assertThat(log.getStatusCode()).isEqualTo(200);
            assertThat(log.getMethod()).isEqualTo("POST");
            assertThat(log.getPath()).isEqualTo("/api/create");
            assertThat(log.getPermission()).isEqualTo("创建");
        }
    }
}
