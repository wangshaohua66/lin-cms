package io.github.talelin.latticy.dto.query;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BasePageDTO 单元测试
 * 测试点：基础分页数据传输对象
 */
@DisplayName("基础分页DTO测试")
class BasePageDTOTest {

    private BasePageDTO basePageDTO;

    @BeforeEach
    void setUp() {
        basePageDTO = new BasePageDTO();
    }

    @Test
    @DisplayName("默认count值 - 应为10")
    void getCount_WithDefaultValue_ShouldReturnTen() {
        // 测试点：验证默认count值
        assertThat(basePageDTO.getCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("默认page值 - 应为0")
    void getPage_WithDefaultValue_ShouldReturnZero() {
        // 测试点：验证默认page值
        assertThat(basePageDTO.getPage()).isEqualTo(0);
    }

    @Test
    @DisplayName("设置count - 正常情况")
    void setCount_WithValidValue_ShouldSet() {
        // 输入参数：count=20
        // 预期结果：count被设置
        // 测试点：验证count setter
        basePageDTO.setCount(20);

        assertThat(basePageDTO.getCount()).isEqualTo(20);
    }

    @Test
    @DisplayName("设置page - 正常情况")
    void setPage_WithValidValue_ShouldSet() {
        // 输入参数：page=5
        // 预期结果：page被设置
        // 测试点：验证page setter
        basePageDTO.setPage(5);

        assertThat(basePageDTO.getPage()).isEqualTo(5);
    }

    @Test
    @DisplayName("设置count为null - 应返回默认值10")
    void getCount_WithNullValue_ShouldReturnDefault() {
        // 输入参数：null
        // 预期结果：返回默认值10
        // 测试点：验证null处理
        basePageDTO.setCount(null);

        assertThat(basePageDTO.getCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("设置page为null - 应返回默认值0")
    void getPage_WithNullValue_ShouldReturnDefault() {
        // 输入参数：null
        // 预期结果：返回默认值0
        // 测试点：验证null处理
        basePageDTO.setPage(null);

        assertThat(basePageDTO.getPage()).isEqualTo(0);
    }

    @Test
    @DisplayName("设置count为0 - 应返回0")
    void getCount_WithZeroValue_ShouldReturnZero() {
        // 输入参数：0
        // 预期结果：返回0
        // 测试点：验证零值处理
        basePageDTO.setCount(0);

        assertThat(basePageDTO.getCount()).isEqualTo(0);
    }

    @Test
    @DisplayName("设置page为负数 - 应返回负数")
    void getPage_WithNegativeValue_ShouldReturnNegative() {
        // 输入参数：-1
        // 预期结果：返回-1
        // 测试点：验证负值处理
        basePageDTO.setPage(-1);

        assertThat(basePageDTO.getPage()).isEqualTo(-1);
    }

    @Test
    @DisplayName("设置大count值 - 正常处理")
    void setCount_WithLargeValue_ShouldHandle() {
        // 输入参数：大值
        // 预期结果：正常处理
        // 测试点：验证大值处理
        basePageDTO.setCount(1000);

        assertThat(basePageDTO.getCount()).isEqualTo(1000);
    }

    @Test
    @DisplayName("设置大page值 - 正常处理")
    void setPage_WithLargeValue_ShouldHandle() {
        // 输入参数：大值
        // 预期结果：正常处理
        // 测试点：验证大值处理
        basePageDTO.setPage(10000);

        assertThat(basePageDTO.getPage()).isEqualTo(10000);
    }
}
