package io.github.talelin.latticy.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.github.talelin.latticy.common.mybatis.LinPage;
import io.github.talelin.latticy.mapper.LogMapper;
import io.github.talelin.latticy.model.LogDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * LogServiceImpl 单元测试
 * 测试点：日志分页查询、搜索、创建等操作
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("日志服务实现类测试")
class LogServiceImplTest {

    @Mock
    private LogMapper logMapper;

    @InjectMocks
    private LogServiceImpl logService;

    private LogDO logDO;
    private Date startDate;
    private Date endDate;

    @BeforeEach
    void setUp() throws Exception {
        // 使用反射设置baseMapper
        Field baseMapperField = logService.getClass().getSuperclass().getDeclaredField("baseMapper");
        baseMapperField.setAccessible(true);
        baseMapperField.set(logService, logMapper);

        startDate = new Date(1609459200000L); // 2021-01-01
        endDate = new Date(1640995200000L);   // 2022-01-01

        logDO = new LogDO();
        logDO.setId(1);
        logDO.setMessage("测试日志");
        logDO.setUserId(1);
        logDO.setUsername("testuser");
        logDO.setStatusCode(200);
        logDO.setMethod("GET");
        logDO.setPath("/api/test");
        logDO.setPermission("查看测试");
    }

    @Test
    @DisplayName("分页获取日志 - 正常情况")
    void getLogPage_WithValidParams_ShouldReturnPage() {
        // 输入参数：页码、每页数量、用户名、开始时间、结束时间
        // 预期结果：返回日志分页数据
        // 测试点：验证日志分页查询
        Integer page = 0;
        Integer count = 10;
        String name = "testuser";

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        expectedPage.setRecords(Arrays.asList(logDO));
        expectedPage.setTotal(1);

        when(logMapper.findLogsByUsernameAndRange(any(LinPage.class), eq(name), eq(startDate), eq(endDate)))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.getLogPage(page, count, name, startDate, endDate);

        assertThat(result.getRecords()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
    }

    @Test
    @DisplayName("分页获取日志 - null用户名")
    void getLogPage_WithNullName_ShouldHandleNull() {
        // 输入参数：null用户名
        // 预期结果：正常处理
        // 测试点：验证null用户名的处理
        Integer page = 0;
        Integer count = 10;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        when(logMapper.findLogsByUsernameAndRange(any(LinPage.class), isNull(), eq(startDate), eq(endDate)))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.getLogPage(page, count, null, startDate, endDate);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("分页获取日志 - 空用户名")
    void getLogPage_WithEmptyName_ShouldHandleEmpty() {
        // 输入参数：空用户名
        // 预期结果：正常处理
        // 测试点：验证空用户名的处理
        Integer page = 0;
        Integer count = 10;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        when(logMapper.findLogsByUsernameAndRange(any(LinPage.class), eq(""), eq(startDate), eq(endDate)))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.getLogPage(page, count, "", startDate, endDate);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("分页获取日志 - null日期")
    void getLogPage_WithNullDates_ShouldHandleNull() {
        // 输入参数：null日期
        // 预期结果：正常处理
        // 测试点：验证null日期的处理
        Integer page = 0;
        Integer count = 10;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        when(logMapper.findLogsByUsernameAndRange(any(LinPage.class), eq("testuser"), isNull(), isNull()))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.getLogPage(page, count, "testuser", null, null);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("分页搜索日志 - 正常情况")
    void searchLogPage_WithValidParams_ShouldReturnPage() {
        // 输入参数：页码、每页数量、用户名、关键字、开始时间、结束时间
        // 预期结果：返回搜索结果分页
        // 测试点：验证日志搜索功能
        Integer page = 0;
        Integer count = 10;
        String keyword = "测试";
        String name = "testuser";

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        expectedPage.setRecords(Arrays.asList(logDO));
        expectedPage.setTotal(1);

        when(logMapper.searchLogsByUsernameAndKeywordAndRange(any(LinPage.class), eq(name), eq("%" + keyword + "%"), eq(startDate), eq(endDate)))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.searchLogPage(page, count, name, keyword, startDate, endDate);

        assertThat(result.getRecords()).hasSize(1);
    }

    @Test
    @DisplayName("分页搜索日志 - null关键字")
    void searchLogPage_WithNullKeyword_ShouldHandleNull() {
        // 输入参数：null关键字
        // 预期结果：正常处理
        // 测试点：验证null关键字的处理
        Integer page = 0;
        Integer count = 10;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        when(logMapper.searchLogsByUsernameAndKeywordAndRange(any(LinPage.class), isNull(), eq("%null%"), eq(startDate), eq(endDate)))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.searchLogPage(page, count, null, null, startDate, endDate);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("搜索日志 - 空关键字")
    void searchLogPage_WithEmptyKeyword_ShouldHandleEmpty() {
        // 输入参数：空关键字
        // 预期结果：正常处理
        // 测试点：验证空关键字处理
        Integer page = 0;
        Integer count = 10;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        when(logMapper.searchLogsByUsernameAndKeywordAndRange(any(LinPage.class), eq(""), eq("%%"), isNull(), isNull()))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.searchLogPage(page, count, "", "", null, null);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("创建日志 - 正常情况")
    void createLog_WithValidParams_ShouldReturnTrue() {
        // 输入参数：日志各字段
        // 预期结果：返回true
        // 测试点：验证日志创建逻辑
        when(logMapper.insert(any(LogDO.class))).thenReturn(1);

        boolean result = logService.createLog("测试消息", "查看权限", 1, "testuser", "GET", "/api/test", 200);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("创建日志 - 创建失败返回false")
    void createLog_WhenInsertFails_ShouldReturnFalse() {
        // 输入参数：日志各字段
        // 预期结果：返回false
        // 测试点：验证创建失败处理
        when(logMapper.insert(any(LogDO.class))).thenReturn(0);

        boolean result = logService.createLog("测试消息", "查看权限", 1, "testuser", "GET", "/api/test", 200);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("创建日志 - null消息")
    void createLog_WithNullMessage_ShouldHandleNull() {
        // 输入参数：null消息
        // 预期结果：正常处理
        // 测试点：验证null消息的处理
        when(logMapper.insert(any(LogDO.class))).thenReturn(1);

        boolean result = logService.createLog(null, "查看权限", 1, "testuser", "GET", "/api/test", 200);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("获取日志分页 - 边界值页码")
    void getLogPage_WithBoundaryPage_ShouldHandle() {
        // 输入参数：边界值页码
        // 预期结果：正常处理
        // 测试点：验证边界值处理
        Integer page = -1;
        Integer count = 10;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        when(logMapper.findLogsByUsernameAndRange(any(LinPage.class), isNull(), isNull(), isNull()))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.getLogPage(page, count, null, null, null);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("获取日志分页 - 边界值每页数量")
    void getLogPage_WithBoundaryCount_ShouldHandle() {
        // 输入参数：边界值每页数量
        // 预期结果：正常处理
        // 测试点：验证边界值处理
        Integer page = 0;
        Integer count = 0;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        when(logMapper.findLogsByUsernameAndRange(any(LinPage.class), isNull(), isNull(), isNull()))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.getLogPage(page, count, null, null, null);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("分页获取日志 - 空结果")
    void getLogPage_WithEmptyResult_ShouldReturnEmptyPage() {
        // 输入参数：查询条件
        // 预期结果：返回空分页
        // 测试点：验证空结果处理
        Integer page = 0;
        Integer count = 10;

        LinPage<LogDO> expectedPage = new LinPage<>(page, count);
        expectedPage.setRecords(Collections.emptyList());
        expectedPage.setTotal(0);

        when(logMapper.findLogsByUsernameAndRange(any(LinPage.class), eq("nonexistent"), isNull(), isNull()))
                .thenReturn(expectedPage);

        IPage<LogDO> result = logService.getLogPage(page, count, "nonexistent", null, null);

        assertThat(result.getRecords()).isEmpty();
        assertThat(result.getTotal()).isEqualTo(0);
    }
}
