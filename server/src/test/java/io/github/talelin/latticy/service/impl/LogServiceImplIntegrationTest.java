package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.model.LogDO;
import io.github.talelin.latticy.service.LogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("日志服务集成测试")
class LogServiceImplIntegrationTest {

    @Autowired
    private LogService logService;

    @Nested
    @DisplayName("获取日志分页测试")
    class GetLogPageTests {

        @Test
        @DisplayName("获取日志分页 - 默认参数")
        void getLogPage_WithDefaultParams_ShouldReturnPage() {
            var page = logService.getLogPage(1, 10, null, null, null);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isNotNull();
        }

        @Test
        @DisplayName("获取日志分页 - 带用户名")
        void getLogPage_WithUsername_ShouldReturnPage() {
            var page = logService.getLogPage(1, 10, "admin", null, null);

            assertThat(page).isNotNull();
        }

        @Test
        @DisplayName("获取日志分页 - 带时间范围")
        void getLogPage_WithDateRange_ShouldReturnPage() {
            Date start = new Date(System.currentTimeMillis() - 86400000L * 30);
            Date end = new Date();

            var page = logService.getLogPage(1, 10, null, start, end);

            assertThat(page).isNotNull();
        }

        @Test
        @DisplayName("获取日志分页 - 大页码")
        void getLogPage_WithLargePageNumber_ShouldReturnEmptyPage() {
            var page = logService.getLogPage(1000, 10, null, null, null);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isEmpty();
        }

        @Test
        @DisplayName("获取日志分页 - 小页面大小")
        void getLogPage_WithSmallPageSize_ShouldReturnPage() {
            var page = logService.getLogPage(1, 1, null, null, null);

            assertThat(page).isNotNull();
            assertThat(page.getRecords().size()).isLessThanOrEqualTo(1);
        }
    }

    @Nested
    @DisplayName("搜索日志分页测试")
    class SearchLogPageTests {

        @Test
        @DisplayName("搜索日志 - 默认参数")
        void searchLogPage_WithDefaultParams_ShouldReturnPage() {
            var page = logService.searchLogPage(1, 10, null, null, null, null);

            assertThat(page).isNotNull();
        }

        @Test
        @DisplayName("搜索日志 - 带关键字")
        void searchLogPage_WithKeyword_ShouldReturnPage() {
            var page = logService.searchLogPage(1, 10, null, "登录", null, null);

            assertThat(page).isNotNull();
        }

        @Test
        @DisplayName("搜索日志 - 带用户名和关键字")
        void searchLogPage_WithUsernameAndKeyword_ShouldReturnPage() {
            var page = logService.searchLogPage(1, 10, "admin", "登录", null, null);

            assertThat(page).isNotNull();
        }

        @Test
        @DisplayName("搜索日志 - 带时间范围")
        void searchLogPage_WithDateRange_ShouldReturnPage() {
            Date start = new Date(System.currentTimeMillis() - 86400000L * 30);
            Date end = new Date();

            var page = logService.searchLogPage(1, 10, null, null, start, end);

            assertThat(page).isNotNull();
        }

        @Test
        @DisplayName("搜索日志 - 无匹配关键字")
        void searchLogPage_WithNoMatchKeyword_ShouldReturnEmptyPage() {
            var page = logService.searchLogPage(1, 10, null, "不存在的关键字xyz123", null, null);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isEmpty();
        }
    }

    @Nested
    @DisplayName("获取用户名分页测试")
    class GetUserNamePageTests {

        @Test
        @DisplayName("获取用户名分页 - 正常获取")
        void getUserNamePage_ShouldReturnPage() {
            var page = logService.getUserNamePage(1, 10);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isNotNull();
        }

        @Test
        @DisplayName("获取用户名分页 - 大页码")
        void getUserNamePage_WithLargePageNumber_ShouldReturnEmptyPage() {
            var page = logService.getUserNamePage(1000, 10);

            assertThat(page).isNotNull();
            assertThat(page.getRecords()).isEmpty();
        }
    }

    @Nested
    @DisplayName("创建日志测试")
    class CreateLogTests {

        @Test
        @DisplayName("创建日志 - 正常创建")
        void createLog_ShouldReturnTrue() {
            boolean result = logService.createLog("测试日志消息", "测试权限", 1, "testuser", "GET", "/test", 200);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("创建日志 - 无权限参数")
        void createLog_WithoutPermission_ShouldReturnTrue() {
            boolean result = logService.createLog("测试日志消息", null, 1, "testuser", "GET", "/test", 200);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("创建日志 - 不同HTTP方法")
        void createLog_WithDifferentMethods_ShouldReturnTrue() {
            assertThat(logService.createLog("POST日志", null, 1, "testuser", "POST", "/test", 201)).isTrue();
            assertThat(logService.createLog("PUT日志", null, 1, "testuser", "PUT", "/test", 200)).isTrue();
            assertThat(logService.createLog("DELETE日志", null, 1, "testuser", "DELETE", "/test", 200)).isTrue();
        }

        @Test
        @DisplayName("创建日志 - 不同状态码")
        void createLog_WithDifferentStatusCodes_ShouldReturnTrue() {
            assertThat(logService.createLog("成功日志", null, 1, "testuser", "GET", "/test", 200)).isTrue();
            assertThat(logService.createLog("创建日志", null, 1, "testuser", "POST", "/test", 201)).isTrue();
            assertThat(logService.createLog("错误日志", null, 1, "testuser", "GET", "/test", 500)).isTrue();
        }
    }

    @Nested
    @DisplayName("保存日志测试")
    class SaveLogTests {

        @Test
        @DisplayName("保存日志实体 - 正常保存")
        void saveLog_ShouldSaveSuccessfully() {
            LogDO log = LogDO.builder()
                    .message("测试日志消息")
                    .userId(1)
                    .username("testuser")
                    .statusCode(200)
                    .method("GET")
                    .path("/test")
                    .build();

            boolean result = logService.save(log);

            assertThat(result).isTrue();
            assertThat(log.getId()).isNotNull();
        }
    }
}
