package io.github.talelin.latticy.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("IP工具类测试")
class IPUtilTest {

    @Nested
    @DisplayName("getIPFromRequest测试")
    class GetIPFromRequestTests {

        @Test
        @DisplayName("从请求获取IP - 正常请求")
        void getIPFromRequest_WithNormalRequest_ShouldReturnIP() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRemoteAddr("192.168.1.1");

            String ip = IPUtil.getIPFromRequest(request);

            assertThat(ip).isEqualTo("192.168.1.1");
        }

        @Test
        @DisplayName("从请求获取IP - X-Forwarded-For头")
        void getIPFromRequest_WithXForwardedFor_ShouldReturnFirstIP() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("X-Forwarded-For", "10.0.0.1, 192.168.1.1");
            request.setRemoteAddr("192.168.1.2");

            String ip = IPUtil.getIPFromRequest(request);

            assertThat(ip).isEqualTo("10.0.0.1");
        }

        @Test
        @DisplayName("从请求获取IP - Proxy-Client-IP头")
        void getIPFromRequest_WithProxyClientIP_ShouldReturnIP() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("Proxy-Client-IP", "10.0.0.2");
            request.setRemoteAddr("192.168.1.2");

            String ip = IPUtil.getIPFromRequest(request);

            assertThat(ip).isEqualTo("10.0.0.2");
        }

        @Test
        @DisplayName("从请求获取IP - unknown值")
        void getIPFromRequest_WithUnknownValue_ShouldUseRemoteAddr() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("X-Forwarded-For", "unknown");
            request.setRemoteAddr("192.168.1.3");

            String ip = IPUtil.getIPFromRequest(request);

            assertThat(ip).isEqualTo("192.168.1.3");
        }

        @Test
        @DisplayName("从请求获取IP - 空值")
        void getIPFromRequest_WithEmptyValue_ShouldUseRemoteAddr() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("X-Forwarded-For", "");
            request.setRemoteAddr("192.168.1.4");

            String ip = IPUtil.getIPFromRequest(request);

            assertThat(ip).isEqualTo("192.168.1.4");
        }

        @Test
        @DisplayName("从请求获取IP - null请求且无上下文")
        void getIPFromRequest_WithNullRequestAndNoContext_ShouldReturnNull() {
            String ip = IPUtil.getIPFromRequest(null);

            assertThat(ip).isNull();
        }
    }

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("私有构造函数 - 抛出异常")
        void constructor_ShouldThrowException() {
            assertThatThrownBy(() -> {
                var constructor = IPUtil.class.getDeclaredConstructor();
                constructor.setAccessible(true);
                constructor.newInstance();
            }).hasCauseInstanceOf(IllegalStateException.class);
        }
    }
}
