package io.github.talelin.latticy.controller.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.talelin.latticy.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("日志控制器集成测试")
class LogControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        adminToken = obtainAdminToken();
    }

    private String obtainAdminToken() throws Exception {
        String loginJson = "{\"username\":\"root\",\"password\":\"123456\"}";
        String response = mockMvc.perform(post("/cms/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginJson))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("access_token").asText();
    }

    @Nested
    @DisplayName("日志查询测试")
    class LogQueryTests {

        @Test
        @DisplayName("获取日志列表 - 成功")
        void getLogs_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/log")
                            .header("Authorization", "Bearer " + adminToken)
                            .param("page", "1")
                            .param("count", "10"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("搜索日志 - 成功")
        void searchLogs_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/log/search")
                            .header("Authorization", "Bearer " + adminToken)
                            .param("page", "1")
                            .param("count", "10")
                            .param("keyword", "test"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("获取日志用户列表 - 成功")
        void getUsers_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/log/users")
                            .header("Authorization", "Bearer " + adminToken)
                            .param("page", "1")
                            .param("count", "10"))
                    .andExpect(status().isOk());
        }
    }
}
