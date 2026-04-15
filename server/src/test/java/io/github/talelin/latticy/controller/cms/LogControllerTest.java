package io.github.talelin.latticy.controller.cms;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.talelin.latticy.dto.log.QueryLogDTO;
import io.github.talelin.latticy.dto.query.BasePageDTO;
import io.github.talelin.latticy.model.LogDO;
import io.github.talelin.latticy.service.LogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("LogController单元测试")
class LogControllerTest {

    @MockBean
    private LogService logService;

    @Autowired
    private LogController logController;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Nested
    @DisplayName("日志查询测试")
    class LogQueryTests {

        @Test
        @DisplayName("输入参数：QueryLogDTO；预期结果：返回分页日志列表；测试点：查询所有日志")
        void getLogs_WithValidDTO_ShouldReturnPagedLogs() {
            QueryLogDTO dto = new QueryLogDTO();
            dto.setPage(1);
            dto.setCount(10);
            dto.setName("admin");

            Page<LogDO> page = new Page<>(1, 10);
            LogDO log1 = new LogDO();
            log1.setId(1);
            log1.setMessage("test log 1");
            LogDO log2 = new LogDO();
            log2.setId(2);
            log2.setMessage("test log 2");
            page.setRecords(Arrays.asList(log1, log2));

            when(logService.getLogPage(eq(1), eq(10), eq("admin"), any(), any())).thenReturn(page);

            var result = logController.getLogs(dto);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(2);
            verify(logService, times(1)).getLogPage(eq(1), eq(10), eq("admin"), any(), any());
        }

        @Test
        @DisplayName("输入参数：带关键词的QueryLogDTO；预期结果：返回分页搜索结果；测试点：搜索日志")
        void searchLogs_WithKeyword_ShouldReturnFilteredResults() {
            QueryLogDTO dto = new QueryLogDTO();
            dto.setPage(1);
            dto.setCount(10);
            dto.setKeyword("login");
            dto.setName("admin");

            Page<LogDO> page = new Page<>(1, 10);
            LogDO log1 = new LogDO();
            log1.setId(1);
            log1.setMessage("user login");
            page.setRecords(Arrays.asList(log1));

            when(logService.searchLogPage(eq(1), eq(10), eq("admin"), eq("login"), any(), any())).thenReturn(page);

            var result = logController.searchLogs(dto);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(1);
            verify(logService, times(1)).searchLogPage(eq(1), eq(10), eq("admin"), eq("login"), any(), any());
        }

        @Test
        @DisplayName("输入参数：BasePageDTO；预期结果：返回分页用户名列表；测试点：查询日志记录的用户")
        void getUsers_WithValidDTO_ShouldReturnPagedUsernames() {
            BasePageDTO dto = new BasePageDTO();
            dto.setPage(1);
            dto.setCount(10);

            Page<String> page = new Page<>(1, 10);
            page.setRecords(Arrays.asList("admin", "user1", "user2"));

            when(logService.getUserNamePage(eq(1), eq(10))).thenReturn(page);

            var result = logController.getUsers(dto);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).hasSize(3);
            assertThat(result.getItems()).contains("admin", "user1", "user2");
            verify(logService, times(1)).getUserNamePage(eq(1), eq(10));
        }
    }
}
