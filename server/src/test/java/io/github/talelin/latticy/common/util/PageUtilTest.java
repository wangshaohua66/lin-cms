package io.github.talelin.latticy.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.talelin.latticy.vo.PageResponseVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * PageUtil 单元测试
 * 测试点：分页响应对象构建
 */
@DisplayName("分页工具类测试")
class PageUtilTest {

    @Test
    @DisplayName("构建分页响应 - 正常情况")
    void build_WithValidPage_ShouldReturnPageResponseVO() {
        // 输入参数：有效的分页对象
        // 预期结果：返回分页响应VO
        // 测试点：验证分页响应构建
        Page<String> page = new Page<>(1, 10, 100);
        page.setRecords(Arrays.asList("item1", "item2", "item3"));

        PageResponseVO<String> result = PageUtil.build(page);

        assertThat(result).isNotNull();
        assertThat(result.getTotal()).isEqualTo(100);
        assertThat(result.getItems()).hasSize(3);
        assertThat(result.getPage()).isEqualTo(1);
        assertThat(result.getCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("构建分页响应 - 空记录")
    void build_WithEmptyRecords_ShouldReturnEmptyItems() {
        // 输入参数：空记录的分页对象
        // 预期结果：返回空items的分页响应VO
        // 测试点：验证空记录处理
        Page<String> page = new Page<>(1, 10, 0);
        page.setRecords(Collections.emptyList());

        PageResponseVO<String> result = PageUtil.build(page);

        assertThat(result.getTotal()).isEqualTo(0);
        assertThat(result.getItems()).isEmpty();
    }

    @Test
    @DisplayName("构建分页响应 - 自定义记录列表")
    void build_WithCustomRecords_ShouldReturnPageResponseVO() {
        // 输入参数：分页对象和自定义记录列表
        // 预期结果：返回包含自定义记录的分页响应VO
        // 测试点：验证自定义记录构建
        Page<String> page = new Page<>(1, 10, 100);
        List<String> customRecords = Arrays.asList("custom1", "custom2");

        PageResponseVO<String> result = PageUtil.build(page, customRecords);

        assertThat(result.getItems()).isEqualTo(customRecords);
        assertThat(result.getTotal()).isEqualTo(100);
    }

    @Test
    @DisplayName("构建分页响应 - 大页码")
    void build_WithLargePageNumber_ShouldHandleLargePage() {
        // 输入参数：大页码
        // 预期结果：正常处理
        // 测试点：验证大页码处理
        Page<String> page = new Page<>(1000, 10, 10000);
        page.setRecords(Arrays.asList("item"));

        PageResponseVO<String> result = PageUtil.build(page);

        assertThat(result.getPage()).isEqualTo(1000);
    }

    @Test
    @DisplayName("构建分页响应 - 边界值页码0")
    void build_WithZeroPage_ShouldHandleZero() {
        // 输入参数：页码为0（边界值）
        // 预期结果：正常处理（页码可能从1开始）
        // 测试点：验证边界值处理
        Page<String> page = new Page<>(0, 10, 100);
        page.setRecords(Arrays.asList("item1"));

        PageResponseVO<String> result = PageUtil.build(page);

        // 实际返回的页码可能为1（内部处理）
        assertThat(result.getPage()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("构建分页响应 - 单条记录")
    void build_WithSingleRecord_ShouldReturnSingleItem() {
        // 输入参数：单条记录的分页
        // 预期结果：返回单条记录的分页响应
        // 测试点：验证单条记录处理
        Page<String> page = new Page<>(1, 10, 1);
        page.setRecords(Collections.singletonList("single"));

        PageResponseVO<String> result = PageUtil.build(page);

        assertThat(result.getItems()).hasSize(1);
        assertThat(result.getTotal()).isEqualTo(1);
    }

    @Test
    @DisplayName("构建分页响应 - 泛型类型")
    void build_WithDifferentGenericTypes_ShouldWork() {
        // 输入参数：不同类型的分页对象
        // 预期结果：正确处理各种类型
        // 测试点：验证泛型处理
        Page<Integer> intPage = new Page<>(1, 10, 5);
        intPage.setRecords(Arrays.asList(1, 2, 3, 4, 5));

        PageResponseVO<Integer> result = PageUtil.build(intPage);

        assertThat(result.getItems()).containsExactly(1, 2, 3, 4, 5);
    }

    @Test
    @DisplayName("构建分页响应 - null记录列表处理")
    void build_WithNullRecordsList_ShouldHandleNull() {
        // 输入参数：null记录列表
        // 预期结果：正常处理
        // 测试点：验证null记录处理
        Page<String> page = new Page<>(1, 10, 0);
        page.setRecords(null);

        PageResponseVO<String> result = PageUtil.build(page);

        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("私有构造函数 - 工具类不可实例化")
    void privateConstructor_UtilityClass_ShouldNotBeInstantiated() {
        // 测试点：验证工具类私有构造函数
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            // 通过反射调用私有构造函数
            java.lang.reflect.Constructor<PageUtil> constructor = PageUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
