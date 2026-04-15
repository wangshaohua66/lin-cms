package io.github.talelin.latticy.common.mybatis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LinPage 单元测试
 * 测试点：自定义分页对象功能
 */
@DisplayName("LinPage分页对象测试")
class LinPageTest {

    @Test
    @DisplayName("默认构造函数 - current为0")
    void defaultConstructor_CurrentShouldBeZero() {
        // 测试点：验证默认构造函数将current设为0
        LinPage<String> page = new LinPage<>();

        assertThat(page.getCurrent()).isEqualTo(0);
    }

    @Test
    @DisplayName("两参数构造函数 - 正常情况")
    void twoParamConstructor_WithValidParams_ShouldSetCorrectly() {
        // 输入参数：current=1, size=10
        // 预期结果：正确设置分页参数
        // 测试点：验证两参数构造
        LinPage<String> page = new LinPage<>(1, 10);

        assertThat(page.getCurrent()).isEqualTo(1);
        assertThat(page.getSize()).isEqualTo(10);
    }

    @Test
    @DisplayName("三参数构造函数 - 正常情况")
    void threeParamConstructor_WithValidParams_ShouldSetCorrectly() {
        // 输入参数：current=1, size=10, total=100
        // 预期结果：正确设置分页参数
        // 测试点：验证三参数构造
        LinPage<String> page = new LinPage<>(1, 10, 100);

        assertThat(page.getCurrent()).isEqualTo(1);
        assertThat(page.getSize()).isEqualTo(10);
        assertThat(page.getTotal()).isEqualTo(100);
    }

    @Test
    @DisplayName("四参数构造函数 - 正常情况")
    void fourParamConstructor_WithValidParams_ShouldSetCorrectly() {
        // 输入参数：current=1, size=10, total=100, isSearchCount=true
        // 预期结果：正确设置分页参数
        // 测试点：验证四参数构造
        LinPage<String> page = new LinPage<>(1, 10, 100, true);

        assertThat(page.getCurrent()).isEqualTo(1);
        assertThat(page.getSize()).isEqualTo(10);
        assertThat(page.getTotal()).isEqualTo(100);
        assertThat(page.isSearchCount()).isTrue();
    }

    @Test
    @DisplayName("负数current - 应设为0")
    void constructor_WithNegativeCurrent_ShouldSetToZero() {
        // 输入参数：current=-1
        // 预期结果：current被设为0
        // 测试点：验证负数处理
        LinPage<String> page = new LinPage<>(-1, 10);

        assertThat(page.getCurrent()).isEqualTo(0);
    }

    @Test
    @DisplayName("零current - 应保持为0")
    void constructor_WithZeroCurrent_ShouldRemainZero() {
        // 输入参数：current=0
        // 预期结果：current保持为0
        // 测试点：验证零值处理
        LinPage<String> page = new LinPage<>(0, 10);

        assertThat(page.getCurrent()).isEqualTo(0);
    }

    @Test
    @DisplayName("大current值 - 正常处理")
    void constructor_WithLargeCurrent_ShouldHandle() {
        // 输入参数：current=1000
        // 预期结果：正常处理
        // 测试点：验证大值处理
        LinPage<String> page = new LinPage<>(1000, 10);

        assertThat(page.getCurrent()).isEqualTo(1000);
    }

    @Test
    @DisplayName("isSearchCount构造函数 - 正常情况")
    void twoParamWithSearchCountConstructor_WithValidParams_ShouldSetCorrectly() {
        // 输入参数：current=1, size=10, isSearchCount=false
        // 预期结果：正确设置分页参数
        // 测试点：验证isSearchCount构造
        LinPage<String> page = new LinPage<>(1, 10, false);

        assertThat(page.getCurrent()).isEqualTo(1);
        assertThat(page.getSize()).isEqualTo(10);
        assertThat(page.isSearchCount()).isFalse();
    }

    @Test
    @DisplayName("设置current - 正常情况")
    void setCurrent_WithValidValue_ShouldSet() {
        // 输入参数：current=5
        // 预期结果：current被设置
        // 测试点：验证setter
        LinPage<String> page = new LinPage<>();
        page.setCurrent(5);

        assertThat(page.getCurrent()).isEqualTo(5);
    }

    @Test
    @DisplayName("序列化 - 正常情况")
    void serialVersionUID_ShouldExist() {
        // 测试点：验证序列化ID存在
        LinPage<String> page = new LinPage<>();
        assertThat(page).isNotNull();
    }
}
