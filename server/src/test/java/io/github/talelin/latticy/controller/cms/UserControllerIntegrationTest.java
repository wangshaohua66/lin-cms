package io.github.talelin.latticy.controller.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.talelin.latticy.BaseIntegrationTest;
import io.github.talelin.latticy.dto.user.ChangePasswordDTO;
import io.github.talelin.latticy.dto.user.UpdateInfoDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("用户控制器集成测试")
class UserControllerIntegrationTest extends BaseIntegrationTest {

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
    @DisplayName("登录测试")
    class LoginTests {

        @Test
        @DisplayName("登录 - 成功")
        void login_ShouldSucceed() throws Exception {
            String json = "{\"username\":\"root\",\"password\":\"123456\"}";

            mockMvc.perform(post("/cms/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.access_token").exists())
                    .andExpect(jsonPath("$.refresh_token").exists());
        }

        @Test
        @DisplayName("登录 - 用户不存在")
        void login_WhenUserNotFound_ShouldReturn404() throws Exception {
            String json = "{\"username\":\"nonexistent\",\"password\":\"123456\"}";

            mockMvc.perform(post("/cms/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("登录 - 密码错误")
        void login_WhenPasswordWrong_ShouldReturn400() throws Exception {
            String json = "{\"username\":\"root\",\"password\":\"wrongpassword\"}";

            mockMvc.perform(post("/cms/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(json))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("验证码测试")
    class CaptchaTests {

        @Test
        @DisplayName("获取验证码 - 成功")
        void getCaptcha_ShouldSucceed() throws Exception {
            mockMvc.perform(post("/cms/user/captcha"))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("刷新令牌测试")
    class RefreshTokenTests {

        @Test
        @DisplayName("刷新令牌 - 未登录")
        void refreshToken_WhenNotLoggedIn_ShouldReturn401() throws Exception {
            mockMvc.perform(get("/cms/user/refresh"))
                    .andExpect(status().isUnauthorized());
        }
    }

    @Nested
    @DisplayName("获取权限测试")
    class GetPermissionsTests {

        @Test
        @DisplayName("获取权限 - 未登录")
        void getPermissions_WhenNotLoggedIn_ShouldReturn401() throws Exception {
            mockMvc.perform(get("/cms/user/permissions"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("获取权限 - 已登录")
        void getPermissions_WhenLoggedIn_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/user/permissions")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("获取用户信息测试")
    class GetInformationTests {

        @Test
        @DisplayName("获取用户信息 - 未登录")
        void getInformation_WhenNotLoggedIn_ShouldReturn401() throws Exception {
            mockMvc.perform(get("/cms/user/information"))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("获取用户信息 - 已登录")
        void getInformation_WhenLoggedIn_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/user/information")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("更新用户信息测试")
    class UpdateInfoTests {

        @Test
        @DisplayName("更新用户信息 - 成功")
        void updateInfo_ShouldSucceed() throws Exception {
            UpdateInfoDTO dto = new UpdateInfoDTO();
            dto.setNickname("新昵称");
            dto.setEmail("newemail@example.com");

            mockMvc.perform(put("/cms/user")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("修改密码测试")
    class ChangePasswordTests {

        @Test
        @DisplayName("修改密码 - 成功")
        void changePassword_ShouldSucceed() throws Exception {
            ChangePasswordDTO dto = new ChangePasswordDTO();
            dto.setOldPassword("123456");
            dto.setNewPassword("newpassword123");
            dto.setConfirmPassword("newpassword123");

            mockMvc.perform(put("/cms/user/change_password")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }
    }
}
