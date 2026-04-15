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

/**
 * DeletedVO 单元测试类
 */
@DisplayName("删除成功VO测试")
class DeletedVOTest {

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
        @DisplayName("无参构造函数")
        void constructor_NoArgs_ShouldCreateInstance() {
            DeletedVO vo = new DeletedVO();

            assertThat(vo).isNotNull();
        }

        @Test
        @DisplayName("带code参数构造函数")
        void constructor_WithCode_ShouldSetCode() {
            DeletedVO vo = new DeletedVO(200);

            assertThat(vo.getCode()).isEqualTo(200);
        }

        @Test
        @DisplayName("带message参数构造函数")
        void constructor_WithMessage_ShouldSetMessage() {
            DeletedVO vo = new DeletedVO("删除成功");

            assertThat(vo.getMessage()).isEqualTo("删除成功");
        }

        @Test
        @DisplayName("带code和message参数构造函数")
        void constructor_WithCodeAndMessage_ShouldSetBoth() {
            DeletedVO vo = new DeletedVO(200, "删除成功");

            assertThat(vo.getCode()).isEqualTo(200);
            assertThat(vo.getMessage()).isEqualTo("删除成功");
        }
    }
}
