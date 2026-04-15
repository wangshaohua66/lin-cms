package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.talelin.latticy.mapper.LogMapper;
import io.github.talelin.latticy.model.LogDO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("LogServiceImpl单元测试")
class LogServiceImplTest {

    @MockBean
    private LogMapper logMapper;

    @Autowired
    private LogServiceImpl logService;

    @Nested
    @DisplayName("日志分页查询测试")
    class LogQueryTests {

        @Test
        @DisplayName("输入参数：分页、用户名、时间范围；预期结果：返回分页日志；测试点：查询日志分页")
        void getLogPage_WithValidParams_ShouldReturnPagedLogs() {
            Page<LogDO> page = new Page<>();
            LogDO log1 = new LogDO();
            log1.setId(1);
            log1.setMessage("test log");
            page.setRecords(java.util.Collections.singletonList(log1));

            when(logMapper.findLogsByUsernameAndRange(any(), anyString(), any(), any())).thenReturn(page);

            IPage<LogDO> result = logService.getLogPage(1, 10, "admin", new Date(), new Date());

            assertThat(result).isNotNull();
            assertThat(result.getRecords()).hasSize(1);
        }

        @Test
        @DisplayName("输入参数：null的name；预期结果：查询所有用户日志；测试点：不指定用户名查询")
        void getLogPage_WithNullName_ShouldQueryAllUsers() {
            Page<LogDO> page = new Page<>();

            when(logMapper.findLogsByUsernameAndRange(any(), isNull(), any(), any())).thenReturn(page);

            IPage<LogDO> result = logService.getLogPage(1, 10, null, new Date(), new Date());

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：关键词搜索；预期结果：返回搜索结果分页；测试点：搜索日志")
        void searchLogPage_WithKeyword_ShouldReturnFilteredResults() {
            Page<LogDO> page = new Page<>();
            LogDO log1 = new LogDO();
            log1.setMessage("login action");
            page.setRecords(java.util.Collections.singletonList(log1));

            when(logMapper.searchLogsByUsernameAndKeywordAndRange(any(), anyString(), anyString(), any(), any())).thenReturn(page);

            IPage<LogDO> result = logService.searchLogPage(1, 10, "admin", "login", new Date(), new Date());

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：分页参数；预期结果：返回分页用户名列表；测试点：查询所有操作用户")
        void getUserNamePage_ShouldReturnPagedUsernames() {
            Page<String> page = new Page<>();
            page.setRecords(java.util.Arrays.asList("admin", "user1", "user2"));

            when(logMapper.getUserNames(any())).thenReturn(page);

            IPage<String> result = logService.getUserNamePage(1, 10);

            assertThat(result).isNotNull();
            assertThat(result.getRecords()).hasSize(3);
        }
    }

    @Nested
    @DisplayName("创建日志测试")
    class CreateLogTests {

        @Test
        @DisplayName("输入参数：带权限的日志参数；预期结果：返回true；测试点：创建带权限的日志")
        void createLog_WithPermission_ShouldReturnTrue() {
            when(logMapper.insert(any(LogDO.class))).thenReturn(1);

            boolean result = logService.createLog(
                    "user login", "user:login", 1, "admin", "POST", "/cms/user/login", 200
            );

            assertThat(result).isTrue();
            verify(logMapper, times(1)).insert(any(LogDO.class));
        }

        @Test
        @DisplayName("输入参数：null权限的日志参数；预期结果：返回true；测试点：创建不带权限的日志")
        void createLog_WithNullPermission_ShouldReturnTrue() {
            when(logMapper.insert(any(LogDO.class))).thenReturn(1);

            boolean result = logService.createLog(
                    "public access", null, 0, "anonymous", "GET", "/public", 200
            );

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：日志参数；预期结果：返回false；测试点：创建日志失败")
        void createLog_WhenInsertFails_ShouldReturnFalse() {
            when(logMapper.insert(any(LogDO.class))).thenReturn(0);

            boolean result = logService.createLog(
                    "test", "test", 1, "test", "GET", "/test", 500
            );

            assertThat(result).isFalse();
        }
    }
}
