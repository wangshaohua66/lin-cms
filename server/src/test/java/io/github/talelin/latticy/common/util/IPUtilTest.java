package io.github.talelin.latticy.common.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * IPUtil 单元测试
 * 测试点：IP地址获取功能
 */
@DisplayName("IP工具类测试")
class IPUtilTest {

    @Test
    @DisplayName("从请求获取IP - X-Forwarded-For头")
    void getIPFromRequest_WithXForwardedFor_ShouldReturnIP() {
        // 输入参数：包含X-Forwarded-For头的请求
        // 预期结果：返回正确的IP地址
        // 测试点：验证X-Forwarded-For头处理
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "192.168.1.1, 10.0.0.1");

        String result = IPUtil.getIPFromRequest(request);

        assertThat(result).isEqualTo("192.168.1.1");
    }

    @Test
    @DisplayName("从请求获取IP - Proxy-Client-IP头")
    void getIPFromRequest_WithProxyClientIP_ShouldReturnIP() {
        // 输入参数：包含Proxy-Client-IP头的请求
        // 预期结果：返回正确的IP地址
        // 测试点：验证Proxy-Client-IP头处理
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Proxy-Client-IP", "192.168.1.2");

        String result = IPUtil.getIPFromRequest(request);

        assertThat(result).isEqualTo("192.168.1.2");
    }

    @Test
    @DisplayName("从请求获取IP - 未知头")
    void getIPFromRequest_WithUnknownHeader_ShouldSkipUnknown() {
        // 输入参数：包含unknown值的请求头
        // 预期结果：跳过unknown，使用下一个头或remoteAddr
        // 测试点：验证unknown值处理
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "unknown");
        request.setRemoteAddr("192.168.1.3");

        String result = IPUtil.getIPFromRequest(request);

        assertThat(result).isEqualTo("192.168.1.3");
    }

    @Test
    @DisplayName("从请求获取IP - 空请求头")
    void getIPFromRequest_WithEmptyHeader_ShouldSkipEmpty() {
        // 输入参数：空请求头
        // 预期结果：跳过空值，使用remoteAddr
        // 测试点：验证空值处理
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "");
        request.setRemoteAddr("192.168.1.4");

        String result = IPUtil.getIPFromRequest(request);

        assertThat(result).isEqualTo("192.168.1.4");
    }

    @Test
    @DisplayName("从请求获取IP - 无特殊头")
    void getIPFromRequest_WithNoSpecialHeaders_ShouldReturnRemoteAddr() {
        // 输入参数：无特殊头的请求
        // 预期结果：返回remoteAddr
        // 测试点：验证默认处理
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("192.168.1.5");

        String result = IPUtil.getIPFromRequest(request);

        assertThat(result).isEqualTo("192.168.1.5");
    }

    @Test
    @DisplayName("从请求获取IP - null请求")
    void getIPFromRequest_WithNullRequest_ShouldReturnNull() {
        // 输入参数：null请求且无线程上下文
        // 预期结果：返回null
        // 测试点：验证null请求处理
        // 清除RequestContextHolder
        RequestContextHolder.resetRequestAttributes();

        String result = IPUtil.getIPFromRequest(null);

        assertThat(result).isNull();
    }

    @Test
    @DisplayName("从请求获取IP - IPv6地址")
    void getIPFromRequest_WithIPv6_ShouldReturnIPv6() {
        // 输入参数：IPv6地址
        // 预期结果：返回IPv6地址
        // 测试点：验证IPv6处理
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "2001:db8::1");

        String result = IPUtil.getIPFromRequest(request);

        assertThat(result).isEqualTo("2001:db8::1");
    }

    @Test
    @DisplayName("从请求获取IP - 多个逗号分隔的IP")
    void getIPFromRequest_WithMultipleIPs_ShouldReturnFirst() {
        // 输入参数：多个逗号分隔的IP
        // 预期结果：返回第一个IP
        // 测试点：验证多IP处理
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("X-Forwarded-For", "10.0.0.1, 10.0.0.2, 10.0.0.3");

        String result = IPUtil.getIPFromRequest(request);

        assertThat(result).isEqualTo("10.0.0.1");
    }

    @Test
    @DisplayName("私有构造函数 - 工具类不可实例化")
    void privateConstructor_UtilityClass_ShouldNotBeInstantiated() {
        // 测试点：验证工具类私有构造函数
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            java.lang.reflect.Constructor<IPUtil> constructor = IPUtil.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
        });
    }
}
