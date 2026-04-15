package io.github.talelin.latticy.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.talelin.latticy.model.BookDO;
import io.github.talelin.latticy.vo.PageResponseVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("分页工具类测试")
class PageUtilTest {

    @Nested
    @DisplayName("构建分页响应测试")
    class BuildTests {

        @Test
        @DisplayName("构建分页响应 - 正常数据")
        void build_WithValidData_ShouldReturnPageResponseVO() {
            Page<BookDO> page = new Page<>(1, 10);
            page.setTotal(100);
            page.setRecords(Arrays.asList(new BookDO(), new BookDO()));

            PageResponseVO<BookDO> result = PageUtil.build(page);

            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(100);
            assertThat(result.getItems()).hasSize(2);
            assertThat(result.getPage()).isEqualTo(1);
            assertThat(result.getCount()).isEqualTo(10);
        }

        @Test
        @DisplayName("构建分页响应 - 空数据")
        void build_WithEmptyData_ShouldReturnEmptyPageResponseVO() {
            Page<BookDO> page = new Page<>(1, 10);
            page.setTotal(0);
            page.setRecords(Collections.emptyList());

            PageResponseVO<BookDO> result = PageUtil.build(page);

            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(0);
            assertThat(result.getItems()).isEmpty();
        }

        @Test
        @DisplayName("构建分页响应 - 大页码")
        void build_WithLargePageNumber_ShouldReturnPageResponseVO() {
            Page<BookDO> page = new Page<>(100, 10);
            page.setTotal(1000);
            page.setRecords(Collections.emptyList());

            PageResponseVO<BookDO> result = PageUtil.build(page);

            assertThat(result).isNotNull();
            assertThat(result.getPage()).isEqualTo(100);
        }

        @Test
        @DisplayName("构建分页响应 - 自定义记录列表")
        void build_WithCustomRecords_ShouldReturnPageResponseVO() {
            Page<BookDO> page = new Page<>(1, 10);
            page.setTotal(100);
            page.setRecords(Arrays.asList(new BookDO()));

            List<String> customRecords = Arrays.asList("item1", "item2");

            PageResponseVO<String> result = PageUtil.build(page, customRecords);

            assertThat(result).isNotNull();
            assertThat(result.getTotal()).isEqualTo(100);
            assertThat(result.getItems()).hasSize(2);
            assertThat(result.getItems().get(0)).isEqualTo("item1");
        }

        @Test
        @DisplayName("构建分页响应 - 自定义空记录列表")
        void build_WithEmptyCustomRecords_ShouldReturnPageResponseVO() {
            Page<BookDO> page = new Page<>(1, 10);
            page.setTotal(100);

            PageResponseVO<String> result = PageUtil.build(page, Collections.emptyList());

            assertThat(result).isNotNull();
            assertThat(result.getItems()).isEmpty();
        }
    }

    @Nested
    @DisplayName("私有构造函数测试")
    class PrivateConstructorTests {

        @Test
        @DisplayName("私有构造函数 - 抛出异常")
        void privateConstructor_ShouldThrowException() {
            assertThatThrownBy(() -> {
                var constructor = PageUtil.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            }).hasCauseInstanceOf(IllegalStateException.class);
        }
    }
}
