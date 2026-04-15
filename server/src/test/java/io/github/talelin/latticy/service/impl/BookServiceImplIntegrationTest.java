package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.model.BookDO;
import io.github.talelin.latticy.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("图书服务集成测试")
class BookServiceImplIntegrationTest {

    @Autowired
    private BookService bookService;

    @Nested
    @DisplayName("创建图书测试")
    class CreateBookTests {

        @Test
        @DisplayName("创建图书 - 正常创建")
        void createBook_ShouldReturnTrue() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("测试图书");
            dto.setAuthor("测试作者");
            dto.setSummary("测试摘要");
            dto.setImage("http://example.com/image.jpg");

            boolean result = bookService.createBook(dto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("创建图书 - 无图片")
        void createBook_WithoutImage_ShouldReturnTrue() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("测试图书2");
            dto.setAuthor("测试作者");
            dto.setSummary("测试摘要");

            boolean result = bookService.createBook(dto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("创建图书 - 标题包含特殊字符")
        void createBook_WithSpecialCharacters_ShouldReturnTrue() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("测试图书!@#$%");
            dto.setAuthor("测试作者");
            dto.setSummary("测试摘要");

            boolean result = bookService.createBook(dto);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("查询图书测试")
    class GetBookTests {

        @Test
        @DisplayName("根据ID查询 - 存在的图书")
        void getById_WhenExists_ShouldReturnBook() {
            BookDO book = bookService.getById(1);

            assertThat(book).isNotNull();
            assertThat(book.getId()).isEqualTo(1);
        }

        @Test
        @DisplayName("根据ID查询 - 不存在的图书")
        void getById_WhenNotExists_ShouldReturnNull() {
            BookDO book = bookService.getById(99999);

            assertThat(book).isNull();
        }

        @Test
        @DisplayName("查询所有图书")
        void findAll_ShouldReturnList() {
            List<BookDO> books = bookService.findAll();

            assertThat(books).isNotNull();
        }

        @Test
        @DisplayName("根据关键字查询 - 有结果")
        void getBookByKeyword_WhenMatches_ShouldReturnList() {
            List<BookDO> books = bookService.getBookByKeyword("图书");

            assertThat(books).isNotNull();
        }

        @Test
        @DisplayName("根据关键字查询 - 无结果")
        void getBookByKeyword_WhenNoMatches_ShouldReturnEmptyList() {
            List<BookDO> books = bookService.getBookByKeyword("不存在的关键字xyz");

            assertThat(books).isEmpty();
        }

        @Test
        @DisplayName("根据关键字查询 - 空关键字")
        void getBookByKeyword_WithEmptyKeyword_ShouldReturnList() {
            List<BookDO> books = bookService.getBookByKeyword("");

            assertThat(books).isNotNull();
        }

        @Test
        @DisplayName("根据关键字查询 - null关键字")
        void getBookByKeyword_WithNullKeyword_ShouldHandle() {
            List<BookDO> books = bookService.getBookByKeyword(null);

            assertThat(books).isNotNull();
        }
    }

    @Nested
    @DisplayName("更新图书测试")
    class UpdateBookTests {

        @Test
        @DisplayName("更新图书 - 正常更新")
        void updateBook_ShouldReturnTrue() {
            BookDO book = bookService.getById(1);
            assertThat(book).isNotNull();

            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("更新后的标题");
            dto.setAuthor("更新后的作者");
            dto.setSummary("更新后的摘要");

            boolean result = bookService.updateBook(book, dto);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("更新图书 - 部分更新")
        void updateBook_PartialUpdate_ShouldReturnTrue() {
            BookDO book = bookService.getById(1);
            assertThat(book).isNotNull();

            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("仅更新标题");
            dto.setAuthor(book.getAuthor());
            dto.setSummary(book.getSummary());

            boolean result = bookService.updateBook(book, dto);

            assertThat(result).isTrue();
        }
    }

    @Nested
    @DisplayName("删除图书测试")
    class DeleteBookTests {

        @Test
        @DisplayName("删除图书 - 正常删除")
        void deleteById_ShouldReturnTrue() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("待删除的图书");
            dto.setAuthor("作者");
            dto.setSummary("摘要");
            bookService.createBook(dto);

            List<BookDO> books = bookService.getBookByKeyword("待删除的图书");
            assertThat(books).isNotEmpty();

            boolean result = bookService.deleteById(books.get(0).getId());

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("删除图书 - 不存在的图书")
        void deleteById_WhenNotExists_ShouldReturnFalse() {
            boolean result = bookService.deleteById(99999);

            assertThat(result).isFalse();
        }
    }
}
