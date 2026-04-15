package io.github.talelin.latticy.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BookDO 单元测试类
 * 测试目标：分支覆盖率>=90%, 方法覆盖率>=90%, 代码行覆盖率>=80%
 */
@DisplayName("图书DO测试")
class BookDOTest {

    private BookDO book;

    @BeforeEach
    void setUp() {
        book = new BookDO();
    }

    @Nested
    @DisplayName("Getter和Setter测试")
    class GetterSetterTests {

        @Test
        @DisplayName("设置和获取id")
        void setId_ShouldGetId() {
            book.setId(1);

            assertThat(book.getId()).isEqualTo(1);
        }

        @Test
        @DisplayName("设置和获取title")
        void setTitle_ShouldGetTitle() {
            book.setTitle("测试图书");

            assertThat(book.getTitle()).isEqualTo("测试图书");
        }

        @Test
        @DisplayName("设置和获取author")
        void setAuthor_ShouldGetAuthor() {
            book.setAuthor("测试作者");

            assertThat(book.getAuthor()).isEqualTo("测试作者");
        }

        @Test
        @DisplayName("设置和获取summary")
        void setSummary_ShouldGetSummary() {
            book.setSummary("测试摘要");

            assertThat(book.getSummary()).isEqualTo("测试摘要");
        }

        @Test
        @DisplayName("设置和获取image")
        void setImage_ShouldGetImage() {
            book.setImage("http://example.com/image.jpg");

            assertThat(book.getImage()).isEqualTo("http://example.com/image.jpg");
        }

        @Test
        @DisplayName("设置和获取createTime")
        void setCreateTime_ShouldGetCreateTime() {
            Date now = new Date();
            book.setCreateTime(now);

            assertThat(book.getCreateTime()).isEqualTo(now);
        }

        @Test
        @DisplayName("设置和获取updateTime")
        void setUpdateTime_ShouldGetUpdateTime() {
            Date now = new Date();
            book.setUpdateTime(now);

            assertThat(book.getUpdateTime()).isEqualTo(now);
        }

        @Test
        @DisplayName("设置和获取deleteTime")
        void setDeleteTime_ShouldGetDeleteTime() {
            Date now = new Date();
            book.setDeleteTime(now);

            assertThat(book.getDeleteTime()).isEqualTo(now);
        }

        @Test
        @DisplayName("设置和获取isDeleted")
        void setIsDeleted_ShouldGetIsDeleted() {
            book.setIsDeleted(true);

            assertThat(book.getIsDeleted()).isTrue();
        }
    }

    @Nested
    @DisplayName("边界值测试")
    class BoundaryTests {

        @Test
        @DisplayName("title为null")
        void title_WhenNull_ShouldAccept() {
            book.setTitle(null);

            assertThat(book.getTitle()).isNull();
        }

        @Test
        @DisplayName("title为空字符串")
        void title_WhenEmpty_ShouldAccept() {
            book.setTitle("");

            assertThat(book.getTitle()).isEmpty();
        }

        @Test
        @DisplayName("title为长字符串")
        void title_WhenLong_ShouldAccept() {
            String longTitle = "a".repeat(1000);
            book.setTitle(longTitle);

            assertThat(book.getTitle()).isEqualTo(longTitle);
        }

        @Test
        @DisplayName("author为null")
        void author_WhenNull_ShouldAccept() {
            book.setAuthor(null);

            assertThat(book.getAuthor()).isNull();
        }

        @Test
        @DisplayName("id为0")
        void id_WhenZero_ShouldAccept() {
            book.setId(0);

            assertThat(book.getId()).isEqualTo(0);
        }

        @Test
        @DisplayName("id为负数")
        void id_WhenNegative_ShouldAccept() {
            book.setId(-1);

            assertThat(book.getId()).isEqualTo(-1);
        }
    }

    @Nested
    @DisplayName("equals和hashCode测试")
    class EqualsHashCodeTests {

        @Test
        @DisplayName("相同对象 - equals返回true")
        void equals_WhenSameObject_ShouldReturnTrue() {
            book.setId(1);

            assertThat(book.equals(book)).isTrue();
        }

        @Test
        @DisplayName("null对象 - equals返回false")
        void equals_WhenNull_ShouldReturnFalse() {
            assertThat(book.equals(null)).isFalse();
        }

        @Test
        @DisplayName("不同类型对象 - equals返回false")
        void equals_WhenDifferentType_ShouldReturnFalse() {
            assertThat(book.equals("string")).isFalse();
        }

        @Test
        @DisplayName("相同id - equals返回true")
        void equals_WhenSameId_ShouldReturnTrue() {
            BookDO book1 = new BookDO();
            book1.setId(1);
            BookDO book2 = new BookDO();
            book2.setId(1);

            assertThat(book1.equals(book2)).isTrue();
        }

        @Test
        @DisplayName("不同id - equals返回false")
        void equals_WhenDifferentId_ShouldReturnFalse() {
            BookDO book1 = new BookDO();
            book1.setId(1);
            BookDO book2 = new BookDO();
            book2.setId(2);

            assertThat(book1.equals(book2)).isFalse();
        }

        @Test
        @DisplayName("hashCode一致性")
        void hashCode_WhenSameId_ShouldBeConsistent() {
            book.setId(1);
            int hash1 = book.hashCode();
            int hash2 = book.hashCode();

            assertThat(hash1).isEqualTo(hash2);
        }
    }

    @Nested
    @DisplayName("toString测试")
    class ToStringTests {

        @Test
        @DisplayName("toString - 包含字段信息")
        void toString_ShouldContainFieldInfo() {
            book.setId(1);
            book.setTitle("测试图书");
            book.setAuthor("测试作者");

            String result = book.toString();

            assertThat(result).isNotNull();
            assertThat(result).contains("测试图书");
            assertThat(result).contains("测试作者");
        }
    }

    @Nested
    @DisplayName("特殊字符测试")
    class SpecialCharacterTests {

        @Test
        @DisplayName("title包含中文")
        void title_WithChinese_ShouldAccept() {
            book.setTitle("深入理解计算机系统");

            assertThat(book.getTitle()).isEqualTo("深入理解计算机系统");
        }

        @Test
        @DisplayName("title包含特殊字符")
        void title_WithSpecialChars_ShouldAccept() {
            book.setTitle("C++ Primer (第5版)");

            assertThat(book.getTitle()).isEqualTo("C++ Primer (第5版)");
        }

        @Test
        @DisplayName("image为URL")
        void image_WithURL_ShouldAccept() {
            book.setImage("https://example.com/path/image.jpg?param=value");

            assertThat(book.getImage()).contains("https://");
        }
    }
}
