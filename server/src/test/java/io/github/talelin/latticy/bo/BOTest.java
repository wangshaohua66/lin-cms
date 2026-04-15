package io.github.talelin.latticy.bo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BO类单元测试
 */
@DisplayName("BO类测试")
class BOTest {

    @Test
    @DisplayName("FileBO - 测试")
    void fileBO_Test() {
        FileBO bo = new FileBO();
        bo.setId(1);
        bo.setKey("file");
        bo.setPath("/upload/test.jpg");
        bo.setUrl("http://localhost:5000/assets/upload/test.jpg");

        assertThat(bo.getId()).isEqualTo(1);
        assertThat(bo.getKey()).isEqualTo("file");
        assertThat(bo.getPath()).isEqualTo("/upload/test.jpg");
        assertThat(bo.getUrl()).isEqualTo("http://localhost:5000/assets/upload/test.jpg");
    }

    @Test
    @DisplayName("GroupPermissionBO - 测试")
    void groupPermissionBO_Test() {
        GroupPermissionBO bo = new GroupPermissionBO();
        bo.setId(1);
        bo.setName("测试分组");
        bo.setInfo("分组信息");

        assertThat(bo.getId()).isEqualTo(1);
        assertThat(bo.getName()).isEqualTo("测试分组");
        assertThat(bo.getInfo()).isEqualTo("分组信息");
    }

    @Test
    @DisplayName("LoginCaptchaBO - 测试")
    void loginCaptchaBO_Test() {
        LoginCaptchaBO bo = new LoginCaptchaBO();
        bo.setCaptcha("1234");
        bo.setExpired(300L);

        assertThat(bo.getCaptcha()).isEqualTo("1234");
        assertThat(bo.getExpired()).isEqualTo(300L);
    }
}
