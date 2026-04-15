package io.github.talelin.latticy.controller.v1;

import io.github.talelin.latticy.BaseIntegrationTest;
import io.github.talelin.latticy.dto.book.CreateOrUpdateBookDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("图书控制器集成测试")
class BookControllerIntegrationTest extends BaseIntegrationTest {

    @Nested
    @DisplayName("获取图书测试")
    class GetBookTests {

        @Test
        @DisplayName("获取图书 - 存在的图书")
        void getBook_WhenExists_ShouldReturnBook() throws Exception {
            mockMvc.perform(get("/v1/book/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1));
        }

        @Test
        @DisplayName("获取图书 - 不存在的图书")
        void getBook_WhenNotExists_ShouldReturn404() throws Exception {
            mockMvc.perform(get("/v1/book/99999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("获取图书 - 无效ID")
        void getBook_WithInvalidId_ShouldReturn400() throws Exception {
            mockMvc.perform(get("/v1/book/-1"))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("获取所有图书测试")
    class GetBooksTests {

        @Test
        @DisplayName("获取所有图书 - 正常获取")
        void getBooks_ShouldReturnList() throws Exception {
            mockMvc.perform(get("/v1/book"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Nested
    @DisplayName("搜索图书测试")
    class SearchBookTests {

        @Test
        @DisplayName("搜索图书 - 带关键字")
        void searchBook_WithKeyword_ShouldReturnList() throws Exception {
            mockMvc.perform(get("/v1/book/search").param("q", "图书"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("搜索图书 - 空关键字")
        void searchBook_WithEmptyKeyword_ShouldReturnList() throws Exception {
            mockMvc.perform(get("/v1/book/search").param("q", ""))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }

        @Test
        @DisplayName("搜索图书 - 无参数")
        void searchBook_WithoutKeyword_ShouldReturnList() throws Exception {
            mockMvc.perform(get("/v1/book/search"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray());
        }
    }

    @Nested
    @DisplayName("创建图书测试")
    class CreateBookTests {

        @Test
        @DisplayName("创建图书 - 正常创建")
        void createBook_ShouldReturnCreated() throws Exception {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("新图书");
            dto.setAuthor("新作者");
            dto.setSummary("新摘要");

            mockMvc.perform(post("/v1/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(dto)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("创建图书 - 标题为空")
        void createBook_WithEmptyTitle_ShouldReturn400() throws Exception {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("");
            dto.setAuthor("作者");

            mockMvc.perform(post("/v1/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(dto)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("创建图书 - 缺少必填字段")
        void createBook_WithoutRequiredFields_ShouldReturn400() throws Exception {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();

            mockMvc.perform(post("/v1/book")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("更新图书测试")
    class UpdateBookTests {

        @Test
        @DisplayName("更新图书 - 正常更新")
        void updateBook_ShouldReturnUpdated() throws Exception {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("更新后的标题");
            dto.setAuthor("更新后的作者");
            dto.setSummary("更新后的摘要");

            mockMvc.perform(put("/v1/book/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").exists());
        }

        @Test
        @DisplayName("更新图书 - 不存在的图书")
        void updateBook_WhenNotExists_ShouldReturn404() throws Exception {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("标题");
            dto.setAuthor("作者");
            dto.setSummary("摘要");

            mockMvc.perform(put("/v1/book/99999")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(dto)))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("更新图书 - 无效ID")
        void updateBook_WithInvalidId_ShouldReturn400() throws Exception {
            CreateOrUpdateBookDTO dto = new CreateOrUpdateBookDTO();
            dto.setTitle("标题");
            dto.setAuthor("作者");

            mockMvc.perform(put("/v1/book/-1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(asJson(dto)))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("删除图书测试")
    class DeleteBookTests {

        @Test
        @DisplayName("删除图书 - 不存在的图书")
        void deleteBook_WhenNotExists_ShouldReturn404() throws Exception {
            mockMvc.perform(delete("/v1/book/99999"))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("删除图书 - 无效ID")
        void deleteBook_WithInvalidId_ShouldReturn400() throws Exception {
            mockMvc.perform(delete("/v1/book/-1"))
                    .andExpect(status().isBadRequest());
        }
    }
}
