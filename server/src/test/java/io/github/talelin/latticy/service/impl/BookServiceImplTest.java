package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.mapper.BookMapper;
import io.github.talelin.latticy.model.BookDO;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@DisplayName("BookServiceImpl单元测试")
class BookServiceImplTest {

    @MockBean
    private BookMapper bookMapper;

    @Autowired
    private BookServiceImpl bookService;

    @Nested
    @DisplayName("创建图书测试")
    class CreateBookTests {

        @Test
        @DisplayName("输入参数：有效CreateOrUpdateBookDTO；预期结果：返回true；测试点：成功创建图书")
        void createBook_WithValidDTO_ShouldReturnTrue() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("Test Book");
            dto.setAuthor("Author");
            dto.setImage("image.jpg");
            dto.setSummary("summary");

            when(bookMapper.insert(any(BookDO.class))).thenReturn(1);

            boolean result = bookService.createBook(dto);

            assertThat(result).isTrue();
            verify(bookMapper, times(1)).insert(any(BookDO.class));
        }

        @Test
        @DisplayName("输入参数：空字段的DTO；预期结果：返回false；测试点：创建失败场景")
        void createBook_WhenInsertFails_ShouldReturnFalse() {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("Test");

            when(bookMapper.insert(any(BookDO.class))).thenReturn(0);

            boolean result = bookService.createBook(dto);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("图书查询测试")
    class BookQueryTests {

        @Test
        @DisplayName("输入参数：关键词；预期结果：返回匹配的图书列表；测试点：按关键词搜索图书")
        void getBookByKeyword_WithValidKeyword_ShouldReturnMatchedBooks() {
            BookDO book1 = new BookDO();
            book1.setId(1);
            book1.setTitle("Java编程");

            when(bookMapper.selectByTitleLikeKeyword(eq("Java"))).thenReturn(Collections.singletonList(book1));

            List<BookDO> result = bookService.getBookByKeyword("Java");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTitle()).contains("Java");
        }

        @Test
        @DisplayName("输入参数：null关键词；预期结果：返回空列表；测试点：null关键词搜索")
        void getBookByKeyword_WithNullKeyword_ShouldReturnEmpty() {
            when(bookMapper.selectByTitleLikeKeyword(isNull())).thenReturn(Collections.emptyList());

            List<BookDO> result = bookService.getBookByKeyword(null);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("输入参数：有效图书ID；预期结果：返回图书；测试点：按ID查询图书")
        void getById_WithValidId_ShouldReturnBook() {
            BookDO book = new BookDO();
            book.setId(1);
            book.setTitle("Test Book");

            when(bookMapper.selectById(eq(1))).thenReturn(book);

            BookDO result = bookService.getById(1);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1);
        }

        @Test
        @DisplayName("输入参数：不存在的ID；预期结果：返回null；测试点：查询不存在的图书")
        void getById_WithNonExistentId_ShouldReturnNull() {
            when(bookMapper.selectById(eq(999))).thenReturn(null);

            BookDO result = bookService.getById(999);

            assertThat(result).isNull();
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回所有图书列表；测试点：查询所有图书")
        void findAll_ShouldReturnAllBooks() {
            BookDO book1 = new BookDO();
            book1.setId(1);
            BookDO book2 = new BookDO();
            book2.setId(2);

            when(bookMapper.selectList(isNull())).thenReturn(Arrays.asList(book1, book2));

            List<BookDO> result = bookService.findAll();

            assertThat(result).hasSize(2);
        }
    }

    @Nested
    @DisplayName("更新图书测试")
    class UpdateBookTests {

        @Test
        @DisplayName("输入参数：图书对象、更新DTO；预期结果：返回true；测试点：成功更新图书")
        void updateBook_WithValidParams_ShouldReturnTrue() {
            BookDO book = new BookDO();
            book.setId(1);
            book.setTitle("Old Title");

            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("New Title");
            dto.setAuthor("New Author");

            when(bookMapper.updateById(any(BookDO.class))).thenReturn(1);

            boolean result = bookService.updateBook(book, dto);

            assertThat(result).isTrue();
            assertThat(book.getTitle()).isEqualTo("New Title");
            verify(bookMapper, times(1)).updateById(any(BookDO.class));
        }

        @Test
        @DisplayName("输入参数：图书对象、更新DTO；预期结果：返回false；测试点：更新失败场景")
        void updateBook_WhenUpdateFails_ShouldReturnFalse() {
            BookDO book = new BookDO();
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();

            when(bookMapper.updateById(any(BookDO.class))).thenReturn(0);

            boolean result = bookService.updateBook(book, dto);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("删除图书测试")
    class DeleteBookTests {

        @Test
        @DisplayName("输入参数：有效图书ID；预期结果：返回true；测试点：成功删除图书")
        void deleteById_WithValidId_ShouldReturnTrue() {
            when(bookMapper.deleteById(eq(1))).thenReturn(1);

            boolean result = bookService.deleteById(1);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：无效ID；预期结果：返回false；测试点：删除不存在的图书")
        void deleteById_WithInvalidId_ShouldReturnFalse() {
            when(bookMapper.deleteById(eq(999))).thenReturn(0);

            boolean result = bookService.deleteById(999);

            assertThat(result).isFalse();
        }
    }
}
