package io.github.talelin.latticy.vo;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("VO数据类单元测试")
class ValueObjectTest {

    @Test
    @DisplayName("输入参数：LoginCaptchaVO属性；预期结果：getter/setter正常工作；测试点：LoginCaptchaVO测试")
    void loginCaptchaVO_GettersAndSetters_ShouldWorkCorrectly() {
        LoginCaptchaVO vo = new LoginCaptchaVO("tag123", "base64image");

        assertThat(vo.getTag()).isEqualTo("tag123");
        assertThat(vo.getImage()).isEqualTo("base64image");
    }

    @Test
    @DisplayName("输入参数：无参构造；预期结果：对象创建成功；测试点：LoginCaptchaVO无参构造测试")
    void loginCaptchaVO_NoArgsConstructor_ShouldCreateObject() {
        LoginCaptchaVO vo = new LoginCaptchaVO();
        vo.setTag("newtag");
        vo.setImage("newimage");

        assertThat(vo.getTag()).isEqualTo("newtag");
        assertThat(vo.getImage()).isEqualTo("newimage");
    }



    @Test
    @DisplayName("输入参数：UnifyResponseVO属性；预期结果：getter/setter正常工作；测试点：UnifyResponseVO测试")
    void unifyResponseVO_GettersAndSetters_ShouldWorkCorrectly() {
        UnifyResponseVO<String> vo = new UnifyResponseVO<>();
        vo.setCode(200);
        vo.setMessage("Success");

        assertThat(vo.getCode()).isEqualTo(200);
        assertThat(vo.getMessage()).isEqualTo("Success");
    }

    @Test
    @DisplayName("输入参数：UserInfoVO属性；预期结果：getter/setter正常工作；测试点：UserInfoVO测试")
    void userInfoVO_GettersAndSetters_ShouldWorkCorrectly() {
        UserInfoVO vo = new UserInfoVO();
        vo.setId(1);
        vo.setUsername("testuser");
        vo.setNickname("Test User");
        vo.setEmail("test@example.com");

        assertThat(vo.getId()).isEqualTo(1);
        assertThat(vo.getUsername()).isEqualTo("testuser");
    }

    @Test
    @DisplayName("输入参数：UserPermissionVO属性；预期结果：getter/setter正常工作；测试点：UserPermissionVO测试")
    void userPermissionVO_GettersAndSetters_ShouldWorkCorrectly() {
        UserPermissionVO vo = new UserPermissionVO();
        vo.setId(1);
        vo.setPermissions(Collections.singletonList(Collections.singletonMap("user", Collections.emptyList())));

        assertThat(vo.getId()).isEqualTo(1);
        assertThat(vo.getPermissions()).hasSize(1);
    }

    @Test
    @DisplayName("输入参数：PageResponseVO属性；预期结果：getter/setter正常工作；测试点：PageResponseVO测试")
    void pageResponseVO_GettersAndSetters_ShouldWorkCorrectly() {
        PageResponseVO<String> vo = new PageResponseVO<>();
        vo.setTotal(100);
        vo.setCount(10);
        vo.setPage(1);
        vo.setItems(Collections.singletonList("item1"));

        assertThat(vo.getTotal()).isEqualTo(100);
        assertThat(vo.getCount()).isEqualTo(10);
        assertThat(vo.getPage()).isEqualTo(1);
        assertThat(vo.getItems()).containsExactly("item1");
    }

    @Test
    @DisplayName("输入参数：完整构造；预期结果：正确设置所有字段；测试点：PageResponseVO完整构造测试")
    void pageResponseVO_FullConstructor_ShouldSetAllFields() {
        PageResponseVO<String> vo = new PageResponseVO<>(100, Collections.singletonList("item1"), 1, 10);

        assertThat(vo.getTotal()).isEqualTo(100);
        assertThat(vo.getCount()).isEqualTo(10);
        assertThat(vo.getPage()).isEqualTo(1);
        assertThat(vo.getItems()).containsExactly("item1");
    }
}
