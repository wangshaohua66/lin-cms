package io.github.talelin.latticy.controller.v1;

import io.github.talelin.autoconfigure.exception.NotFoundException;
import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.model.BookDO;
import io.github.talelin.latticy.service.BookService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@DisplayName("BookController单元测试")
class BookControllerTest {

    @MockBean
    private BookService bookService;

    @Autowired
    private BookController bookController;

    @Nested
    @DisplayName("图书查询测试")
    class BookQueryTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回图书列表；测试点：获取所有图书")
        void getBooks_ShouldReturnAllBooks() {
            List<BookDO> books = Arrays.asList(new BookDO(), new BookDO());
            when(bookService.findAll()).thenReturn(books);

            List<BookDO> result = bookController.getBooks();

            assertThat(result).hasSize(2);
        }

        @Test
        @DisplayName("输入参数：空数据库；预期结果：返回空列表；测试点：图书列表为空")
        void getBooks_WhenEmpty_ShouldReturnEmpty() {
            when(bookService.findAll()).thenReturn(Collections.emptyList());

            List<BookDO> result = bookController.getBooks();

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("输入参数：有效图书ID；预期结果：返回对应图书；测试点：通过ID获取图书")
        void getBook_WithValidId_ShouldReturnBook() {
            BookDO book = new BookDO();
            book.setId(1);
            book.setTitle("Test Book");
            when(bookService.getById(eq(1))).thenReturn(book);

            BookDO result = bookController.getBook(1);

            assertThat(result).isNotNull();
            assertThat(result.getTitle()).isEqualTo("Test Book");
        }

        @Test
        @DisplayName("输入参数：不存在的图书ID；预期结果：抛出NotFoundException；测试点：图书不存在场景")
        void getBook_WithNonExistentId_ShouldThrowNotFoundException() {
            when(bookService.getById(eq(999))).thenReturn(null);

            assertThrows(NotFoundException.class, () -> bookController.getBook(999));
        }

        @Test
        @DisplayName("输入参数：有效关键词；预期结果：返回匹配图书列表；测试点：关键词搜索图书")
        void searchBook_WithValidKeyword_ShouldReturnBooks() {
            List<BookDO> books = Collections.singletonList(new BookDO());
            when(bookService.getBookByKeyword(eq("%java%"))).thenReturn(books);

            List<BookDO> result = bookController.searchBook("java");

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("输入参数：无匹配关键词；预期结果：返回空列表；测试点：无匹配搜索结果")
        void searchBook_WithNoMatchKeyword_ShouldReturnEmpty() {
            when(bookService.getBookByKeyword(eq("%xyz%"))).thenReturn(Collections.emptyList());

            List<BookDO> result = bookController.searchBook("xyz");

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("图书创建测试")
    class CreateBookTests {

        @Test
        @DisplayName("输入参数：有效图书DTO；预期结果：返回CreatedVO；测试点：正常创建图书")
        void createBook_WithValidDTO_ShouldReturnCreatedVO() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("New Book");
            dto.setAuthor("Author");
            when(bookService.createBook(any(CreateOrUpdateBookDTO.class))).thenReturn(true);

            var result = bookController.createBook(dto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：图书DTO（空摘要）；预期结果：返回CreatedVO；测试点：摘要为空场景")
        void createBook_WithEmptySummary_ShouldReturnCreatedVO() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("New Book");
            dto.setAuthor("Author");
            dto.setSummary("");
            when(bookService.createBook(any(CreateOrUpdateBookDTO.class))).thenReturn(true);

            var result = bookController.createBook(dto);

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("图书更新测试")
    class UpdateBookTests {

        @Test
        @DisplayName("输入参数：有效ID和DTO；预期结果：返回UpdatedVO；测试点：正常更新图书")
        void updateBook_WithValidParams_ShouldReturnUpdatedVO() {
            BookDO book = new BookDO();
            book.setId(1);
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("Updated Book");
            when(bookService.getById(eq(1))).thenReturn(book);

            var result = bookController.updateBook(1, dto);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：不存在的ID；预期结果：抛出NotFoundException；测试点：更新不存在的图书")
        void updateBook_WithNonExistentId_ShouldThrowNotFoundException() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            when(bookService.getById(eq(999))).thenReturn(null);

            assertThrows(NotFoundException.class, () -> bookController.updateBook(999, dto));
        }
    }

    @Nested
    @DisplayName("图书删除测试")
    class DeleteBookTests {

        @Test
        @DisplayName("输入参数：有效图书ID；预期结果：返回DeletedVO；测试点：正常删除图书")
        void deleteBook_WithValidId_ShouldReturnDeletedVO() {
            BookDO book = new BookDO();
            book.setId(1);
            when(bookService.getById(eq(1))).thenReturn(book);

            var result = bookController.deleteBook(1);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：不存在的ID；预期结果：抛出NotFoundException；测试点：删除不存在的图书")
        void deleteBook_WithNonExistentId_ShouldThrowNotFoundException() {
            when(bookService.getById(eq(999))).thenReturn(null);

            assertThrows(NotFoundException.class, () -> bookController.deleteBook(999));
        }
    }
}
