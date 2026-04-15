package io.github.talelin.latticy.service.impl;

import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import io.github.talelin.latticy.mapper.BookMapper;
import io.github.talelin.latticy.model.BookDO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * BookServiceImpl 单元测试
 * 测试点：图书创建、查询、更新、删除等业务逻辑
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("图书服务实现类测试")
class BookServiceImplTest {

    @Mock
    private BookMapper bookMapper;

    @InjectMocks
    private BookServiceImpl bookService;

    private CreateOrUpdateBookDTO createBookDTO;
    private CreateOrUpdateBookDTO updateBookDTO;
    private BookDO bookDO;

    @BeforeEach
    void setUp() {
        createBookDTO = new CreateOrUpdateBookDTO();
        createBookDTO.setTitle("测试图书");
        createBookDTO.setAuthor("测试作者");
        createBookDTO.setSummary("测试摘要");
        createBookDTO.setImage("test.jpg");

        updateBookDTO = new CreateOrUpdateBookDTO();
        updateBookDTO.setTitle("更新图书");
        updateBookDTO.setAuthor("更新作者");
        updateBookDTO.setSummary("更新摘要");
        updateBookDTO.setImage("update.jpg");

        bookDO = new BookDO();
        bookDO.setId(1);
        bookDO.setTitle("原图书");
        bookDO.setAuthor("原作者");
        bookDO.setSummary("原摘要");
        bookDO.setImage("original.jpg");
    }

    @Test
    @DisplayName("创建图书 - 正常情况")
    void createBook_WithValidDTO_ShouldReturnTrue() {
        // 输入参数：有效的图书创建DTO
        // 预期结果：返回true，表示创建成功
        // 测试点：验证图书创建逻辑
        when(bookMapper.insert(any(BookDO.class))).thenReturn(1);

        boolean result = bookService.createBook(createBookDTO);

        assertThat(result).isTrue();
        verify(bookMapper).insert(any(BookDO.class));
    }

    @Test
    @DisplayName("创建图书 - 插入失败返回0")
    void createBook_WhenInsertReturnsZero_ShouldReturnFalse() {
        // 输入参数：有效的图书创建DTO
        // 预期结果：返回false，表示创建失败
        // 测试点：验证插入失败时的处理
        when(bookMapper.insert(any(BookDO.class))).thenReturn(0);

        boolean result = bookService.createBook(createBookDTO);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("创建图书 - null参数处理")
    void createBook_WithNullImage_ShouldHandleNull() {
        // 输入参数：image为null的DTO
        // 预期结果：正常处理，不抛出异常
        // 测试点：验证null值的处理
        CreateOrUpdateBookDTO dtoWithNullImage = new CreateOrUpdateBookDTO();
        dtoWithNullImage.setTitle("测试");
        dtoWithNullImage.setAuthor("作者");
        dtoWithNullImage.setSummary("摘要");
        dtoWithNullImage.setImage(null);

        when(bookMapper.insert(any(BookDO.class))).thenReturn(1);

        boolean result = bookService.createBook(dtoWithNullImage);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("根据关键字获取图书 - 正常情况")
    void getBookByKeyword_WithValidKeyword_ShouldReturnList() {
        // 输入参数：有效关键字
        // 预期结果：返回匹配的图书列表
        // 测试点：验证关键字查询逻辑
        String keyword = "测试";
        List<BookDO> expectedBooks = Arrays.asList(bookDO);
        when(bookMapper.selectByTitleLikeKeyword(keyword)).thenReturn(expectedBooks);

        List<BookDO> result = bookService.getBookByKeyword(keyword);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("原图书");
    }

    @Test
    @DisplayName("根据关键字获取图书 - 空关键字")
    void getBookByKeyword_WithEmptyKeyword_ShouldReturnEmptyList() {
        // 输入参数：空关键字
        // 预期结果：返回空列表
        // 测试点：验证空关键字的处理
        when(bookMapper.selectByTitleLikeKeyword("")).thenReturn(Collections.emptyList());

        List<BookDO> result = bookService.getBookByKeyword("");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据关键字获取图书 - null关键字")
    void getBookByKeyword_WithNullKeyword_ShouldHandleNull() {
        // 输入参数：null关键字
        // 预期结果：正常处理
        // 测试点：验证null关键字的处理
        when(bookMapper.selectByTitleLikeKeyword(null)).thenReturn(Collections.emptyList());

        List<BookDO> result = bookService.getBookByKeyword(null);

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("更新图书 - 正常情况")
    void updateBook_WithValidBookAndDTO_ShouldReturnTrue() {
        // 输入参数：有效的图书对象和更新DTO
        // 预期结果：返回true，表示更新成功
        // 测试点：验证图书更新逻辑
        when(bookMapper.updateById(any(BookDO.class))).thenReturn(1);

        boolean result = bookService.updateBook(bookDO, updateBookDTO);

        assertThat(result).isTrue();
        assertThat(bookDO.getTitle()).isEqualTo("更新图书");
        assertThat(bookDO.getAuthor()).isEqualTo("更新作者");
        verify(bookMapper).updateById(bookDO);
    }

    @Test
    @DisplayName("更新图书 - 更新失败返回0")
    void updateBook_WhenUpdateReturnsZero_ShouldReturnFalse() {
        // 输入参数：有效的图书对象和更新DTO
        // 预期结果：返回false，表示更新失败
        // 测试点：验证更新失败时的处理
        when(bookMapper.updateById(any(BookDO.class))).thenReturn(0);

        boolean result = bookService.updateBook(bookDO, updateBookDTO);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("更新图书 - null参数处理")
    void updateBook_WithNullImage_ShouldHandleNull() {
        // 输入参数：image为null的DTO
        // 预期结果：正常处理，不抛出异常
        // 测试点：验证null值的处理
        CreateOrUpdateBookDTO dtoWithNullImage = new CreateOrUpdateBookDTO();
        dtoWithNullImage.setTitle("更新");
        dtoWithNullImage.setAuthor("作者");
        dtoWithNullImage.setSummary("摘要");
        dtoWithNullImage.setImage(null);

        when(bookMapper.updateById(any(BookDO.class))).thenReturn(1);

        boolean result = bookService.updateBook(bookDO, dtoWithNullImage);

        assertThat(result).isTrue();
        assertThat(bookDO.getImage()).isNull();
    }

    @Test
    @DisplayName("根据ID获取图书 - 正常情况")
    void getById_WithValidId_ShouldReturnBook() {
        // 输入参数：有效的图书ID
        // 预期结果：返回对应的图书对象
        // 测试点：验证ID查询逻辑
        when(bookMapper.selectById(1)).thenReturn(bookDO);

        BookDO result = bookService.getById(1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1);
    }

    @Test
    @DisplayName("根据ID获取图书 - 不存在的ID")
    void getById_WithNonExistentId_ShouldReturnNull() {
        // 输入参数：不存在的图书ID
        // 预期结果：返回null
        // 测试点：验证不存在ID的处理
        when(bookMapper.selectById(999)).thenReturn(null);

        BookDO result = bookService.getById(999);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("根据ID获取图书 - null参数")
    void getById_WithNullId_ShouldReturnNull() {
        // 输入参数：null ID
        // 预期结果：返回null
        // 测试点：验证null ID的处理
        when(bookMapper.selectById(null)).thenReturn(null);

        BookDO result = bookService.getById(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("查询所有图书 - 正常情况")
    void findAll_WithBooksExist_ShouldReturnList() {
        // 输入参数：无
        // 预期结果：返回所有图书列表
        // 测试点：验证查询所有图书逻辑
        List<BookDO> expectedBooks = Arrays.asList(bookDO, new BookDO());
        when(bookMapper.selectList(null)).thenReturn(expectedBooks);

        List<BookDO> result = bookService.findAll();

        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("查询所有图书 - 无图书时返回空列表")
    void findAll_WithNoBooks_ShouldReturnEmptyList() {
        // 输入参数：无
        // 预期结果：返回空列表
        // 测试点：验证无图书时的处理
        when(bookMapper.selectList(null)).thenReturn(Collections.emptyList());

        List<BookDO> result = bookService.findAll();

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("根据ID删除图书 - 正常情况")
    void deleteById_WithValidId_ShouldReturnTrue() {
        // 输入参数：有效的图书ID
        // 预期结果：返回true，表示删除成功
        // 测试点：验证删除逻辑
        when(bookMapper.deleteById(1)).thenReturn(1);

        boolean result = bookService.deleteById(1);

        assertThat(result).isTrue();
    }

    @Test
    @DisplayName("根据ID删除图书 - 删除失败返回0")
    void deleteById_WhenDeleteReturnsZero_ShouldReturnFalse() {
        // 输入参数：不存在的图书ID
        // 预期结果：返回false，表示删除失败
        // 测试点：验证删除失败时的处理
        when(bookMapper.deleteById(999)).thenReturn(0);

        boolean result = bookService.deleteById(999);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("根据ID删除图书 - 边界值0")
    void deleteById_WithZeroId_ShouldReturnFalse() {
        // 输入参数：ID为0（边界值）
        // 预期结果：返回false
        // 测试点：验证边界值处理
        when(bookMapper.deleteById(0)).thenReturn(0);

        boolean result = bookService.deleteById(0);

        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("根据ID删除图书 - 负值ID")
    void deleteById_WithNegativeId_ShouldReturnFalse() {
        // 输入参数：负值ID（非法参数）
        // 预期结果：返回false
        // 测试点：验证非法参数处理
        when(bookMapper.deleteById(-1)).thenReturn(0);

        boolean result = bookService.deleteById(-1);

        assertThat(result).isFalse();
    }
}
