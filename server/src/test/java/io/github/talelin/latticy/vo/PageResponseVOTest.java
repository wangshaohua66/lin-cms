package io.github.talelin.latticy.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("分页响应VO测试")
class PageResponseVOTest {

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            PageResponseVO<String> vo = PageResponseVO.<String>builder()
                    .total(100)
                    .items(Arrays.asList("item1", "item2"))
                    .page(1)
                    .count(10)
                    .build();

            assertThat(vo.getTotal()).isEqualTo(100);
            assertThat(vo.getItems()).hasSize(2);
            assertThat(vo.getPage()).isEqualTo(1);
            assertThat(vo.getCount()).isEqualTo(10);
        }

        @Test
        @DisplayName("Builder - 空列表")
        void builder_WithEmptyList_ShouldBuildSuccessfully() {
            PageResponseVO<String> vo = PageResponseVO.<String>builder()
                    .total(0)
                    .items(Collections.emptyList())
                    .page(1)
                    .count(10)
                    .build();

            assertThat(vo.getTotal()).isEqualTo(0);
            assertThat(vo.getItems()).isEmpty();
        }
    }

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("全参构造函数 - 正常创建")
        void allArgsConstructor_ShouldCreateVO() {
            PageResponseVO<String> vo = new PageResponseVO<>(100, Arrays.asList("item1"), 1, 10);

            assertThat(vo.getTotal()).isEqualTo(100);
            assertThat(vo.getItems()).hasSize(1);
            assertThat(vo.getPage()).isEqualTo(1);
            assertThat(vo.getCount()).isEqualTo(10);
        }

        @Test
        @DisplayName("无参构造函数 - 创建对象")
        void noArgsConstructor_ShouldCreateVO() {
            PageResponseVO<String> vo = new PageResponseVO<>();

            assertThat(vo).isNotNull();
            assertThat(vo.getTotal()).isNull();
            assertThat(vo.getItems()).isNull();
            assertThat(vo.getPage()).isNull();
            assertThat(vo.getCount()).isNull();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            PageResponseVO<String> vo = new PageResponseVO<>();

            vo.setTotal(200);
            vo.setItems(Arrays.asList("a", "b", "c"));
            vo.setPage(2);
            vo.setCount(20);

            assertThat(vo.getTotal()).isEqualTo(200);
            assertThat(vo.getItems()).hasSize(3);
            assertThat(vo.getPage()).isEqualTo(2);
            assertThat(vo.getCount()).isEqualTo(20);
        }
    }
}
