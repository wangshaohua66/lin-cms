package io.github.talelin.latticy.vo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * CreatedVO 单元测试类
 * 测试目标：分支覆盖率>=90%, 方法覆盖率>=90%, 代码行覆盖率>=80%
 */
@DisplayName("创建成功VO测试")
class CreatedVOTest {

    @BeforeEach
    void setUp() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request, response);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @AfterEach
    void tearDown() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("无参构造函数 - 创建成功")
        void constructor_NoArgs_ShouldCreateInstance() {
            CreatedVO vo = new CreatedVO();

            assertThat(vo).isNotNull();
        }

        @Test
        @DisplayName("带code参数构造函数")
        void constructor_WithCode_ShouldSetCode() {
            int code = 201;

            CreatedVO vo = new CreatedVO(code);

            assertThat(vo.getCode()).isEqualTo(code);
        }

        @Test
        @DisplayName("带message参数构造函数")
        void constructor_WithMessage_ShouldSetMessage() {
            String message = "创建成功";

            CreatedVO vo = new CreatedVO(message);

            assertThat(vo.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("带code和message参数构造函数")
        void constructor_WithCodeAndMessage_ShouldSetBoth() {
            int code = 201;
            String message = "创建成功";

            CreatedVO vo = new CreatedVO(code, message);

            assertThat(vo.getCode()).isEqualTo(code);
            assertThat(vo.getMessage()).isEqualTo(message);
        }

        @Test
        @DisplayName("code为0")
        void constructor_WithZeroCode_ShouldAccept() {
            CreatedVO vo = new CreatedVO(0);

            assertThat(vo.getCode()).isEqualTo(0);
        }

        @Test
        @DisplayName("code为负数")
        void constructor_WithNegativeCode_ShouldAccept() {
            CreatedVO vo = new CreatedVO(-1);

            assertThat(vo.getCode()).isEqualTo(-1);
        }

        @Test
        @DisplayName("message为null")
        void constructor_WithNullMessage_ShouldAccept() {
            CreatedVO vo = new CreatedVO((String) null);

            assertThat(vo.getMessage()).isNull();
        }

        @Test
        @DisplayName("message为空字符串")
        void constructor_WithEmptyMessage_ShouldAccept() {
            CreatedVO vo = new CreatedVO("");

            assertThat(vo.getMessage()).isEmpty();
        }

        @Test
        @DisplayName("message包含中文")
        void constructor_WithChineseMessage_ShouldAccept() {
            String message = "创建成功，数据已保存";

            CreatedVO vo = new CreatedVO(message);

            assertThat(vo.getMessage()).isEqualTo(message);
        }
    }

    @Nested
    @DisplayName("toString方法测试")
    class ToStringTests {

        @Test
        @DisplayName("toString - 返回非空字符串")
        void toString_ShouldReturnString() {
            CreatedVO vo = new CreatedVO(201, "创建成功");

            String result = vo.toString();

            assertThat(result).isNotNull();
            assertThat(result).isNotEmpty();
        }

        @Test
        @DisplayName("toString - 包含code和message")
        void toString_ShouldContainCodeAndMessage() {
            CreatedVO vo = new CreatedVO(201, "创建成功");

            String result = vo.toString();

            assertThat(result).contains("201");
            assertThat(result).contains("创建成功");
        }
    }
}
