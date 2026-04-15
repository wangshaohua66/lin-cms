package io.github.talelin.latticy.controller.cms;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.talelin.latticy.BaseIntegrationTest;
import io.github.talelin.latticy.dto.admin.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("管理员控制器集成测试")
class AdminControllerIntegrationTest extends BaseIntegrationTest {

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
    @DisplayName("权限管理测试")
    class PermissionTests {

        @Test
        @DisplayName("获取所有权限 - 成功")
        void getAllPermissions_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/admin/permission")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("分配单个权限 - 成功")
        void dispatchPermission_ShouldSucceed() throws Exception {
            DispatchPermissionDTO dto = new DispatchPermissionDTO();
            dto.setGroupId(1);
            dto.setPermissionId(1);

            mockMvc.perform(post("/cms/admin/permission/dispatch")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("分配多个权限 - 成功")
        void dispatchPermissions_ShouldSucceed() throws Exception {
            DispatchPermissionsDTO dto = new DispatchPermissionsDTO();
            dto.setGroupId(1);
            dto.setPermissionIds(Arrays.asList(1, 2, 3));

            mockMvc.perform(post("/cms/admin/permission/dispatch/batch")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
        }

        @Test
        @DisplayName("删除多个权限 - 成功")
        void removePermissions_ShouldSucceed() throws Exception {
            RemovePermissionsDTO dto = new RemovePermissionsDTO();
            dto.setGroupId(1);
            dto.setPermissionIds(Arrays.asList(1, 2));

            mockMvc.perform(post("/cms/admin/permission/remove")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("用户管理测试")
    class UserTests {

        @Test
        @DisplayName("获取用户列表 - 成功")
        void getUsers_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/admin/users")
                            .header("Authorization", "Bearer " + adminToken)
                            .param("page", "1")
                            .param("count", "10"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("修改用户密码 - 成功")
        void changeUserPassword_ShouldSucceed() throws Exception {
            ResetPasswordDTO dto = new ResetPasswordDTO();
            dto.setNewPassword("newpassword123");
            dto.setConfirmPassword("newpassword123");

            mockMvc.perform(put("/cms/admin/user/1/password")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("删除用户 - 成功")
        void deleteUser_ShouldSucceed() throws Exception {
            mockMvc.perform(delete("/cms/admin/user/2")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk());
        }
    }

    @Nested
    @DisplayName("分组管理测试")
    class GroupTests {

        @Test
        @DisplayName("获取分组列表 - 成功")
        void getGroups_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/admin/group")
                            .header("Authorization", "Bearer " + adminToken)
                            .param("page", "1")
                            .param("count", "10"))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("获取所有分组 - 成功")
        void getAllGroups_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/admin/group/all")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("获取单个分组 - 成功")
        void getGroup_ShouldSucceed() throws Exception {
            mockMvc.perform(get("/cms/admin/group/1")
                            .header("Authorization", "Bearer " + adminToken))
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("创建分组 - 成功")
        void createGroup_ShouldSucceed() throws Exception {
            NewGroupDTO dto = new NewGroupDTO();
            dto.setName("test_group");
            dto.setInfo("测试分组");

            mockMvc.perform(post("/cms/admin/group")
                            .header("Authorization", "Bearer " + adminToken)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isCreated());
        }
    }
}
