package io.github.talelin.latticy.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.github.talelin.latticy.bo.LoginCaptchaBO;
import io.github.talelin.latticy.common.util.BeanCopyUtil;
import io.github.talelin.latticy.common.util.CaptchaUtil;
import io.github.talelin.latticy.common.util.EncryptUtil;
import io.github.talelin.latticy.common.util.IPUtil;
import io.github.talelin.latticy.common.util.PageUtil;
import io.github.talelin.latticy.common.util.ResponseUtil;
import io.github.talelin.latticy.model.BookDO;
import io.github.talelin.latticy.vo.PageResponseVO;
import io.github.talelin.latticy.vo.UnifyResponseVO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.security.GeneralSecurityException;
import java.util.*;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@DisplayName("工具类单元测试")
class UtilityClassesTest {

    @Nested
    @DisplayName("EncryptUtil加密工具测试")
    class EncryptUtilTests {

        @Test
        @DisplayName("输入参数：明文密码；预期结果：返回加密字符串；测试点：密码加密")
        void encrypt_WithPlainPassword_ShouldReturnEncrypted() {
            String result = EncryptUtil.encrypt("password123");

            assertThat(result).isNotNull();
            assertThat(result).isNotEqualTo("password123");
        }

        @Test
        @DisplayName("输入参数：空字符串；预期结果：返回加密字符串；测试点：空字符串加密")
        void encrypt_WithEmptyString_ShouldReturnEncrypted() {
            String result = EncryptUtil.encrypt("");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：正确的明文和密文；预期结果：返回true；测试点：密码验证成功")
        void verify_WithCorrectPassword_ShouldReturnTrue() {
            String plain = "testpass";
            String encrypted = EncryptUtil.encrypt(plain);

            boolean result = EncryptUtil.verify(encrypted, plain);

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：错误的明文和密文；预期结果：返回false；测试点：密码验证失败")
        void verify_WithWrongPassword_ShouldReturnFalse() {
            String encrypted = EncryptUtil.encrypt("correct");

            boolean result = EncryptUtil.verify(encrypted, "wrong");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("输入参数：null密文；预期结果：返回false；测试点：null密文验证")
        void verify_WithNullEncrypted_ShouldReturnFalse() {
            boolean result = EncryptUtil.verify(null, "password");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("输入参数：null明文；预期结果：返回false；测试点：null明文验证")
        void verify_WithNullPlain_ShouldReturnFalse() {
            String encrypted = EncryptUtil.encrypt("password");
            boolean result = EncryptUtil.verify(encrypted, null);

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("IPUtil工具测试")
    class IPUtilTests {

        @Test
        @DisplayName("输入参数：标准请求；预期结果：返回IP地址；测试点：获取客户端IP")
        void getIPFromRequest_WithStandardRequest_ShouldReturnIP() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.setRemoteAddr("192.168.1.1");

            String result = IPUtil.getIPFromRequest(request);

            assertThat(result).isEqualTo("192.168.1.1");
        }

        @Test
        @DisplayName("输入参数：X-Forwarded-For；预期结果：返回第一个IP；测试点：处理代理IP")
        void getIPFromRequest_WithXForwardedFor_ShouldHandle() {
            MockHttpServletRequest request = new MockHttpServletRequest();
            request.addHeader("X-Forwarded-For", "10.0.0.1, 172.16.0.1");
            request.setRemoteAddr("192.168.1.1");

            String result = IPUtil.getIPFromRequest(request);

            assertThat(result).isEqualTo("10.0.0.1");
        }

        @Test
        @DisplayName("输入参数：null请求；预期结果：返回null；测试点：null请求处理")
        void getIPFromRequest_WithNullRequest_ShouldReturnNull() {
            String result = IPUtil.getIPFromRequest(null);

            assertThat(result).isNull();
        }
    }

    @Nested
    @DisplayName("CaptchaUtil验证码工具测试")
    class CaptchaUtilTests {

        @Test
        @DisplayName("输入参数：验证码长度；预期结果：返回随机字符串；测试点：生成随机字符串")
        void getRandomString_WithLength_ShouldReturnCorrectLength() {
            String result = CaptchaUtil.getRandomString(4);

            assertThat(result).hasSize(4);
        }

        @Test
        @DisplayName("输入参数：0长度；预期结果：返回最大长度字符串；测试点：边界值处理")
        void getRandomString_WithZeroLength_ShouldReturnFullLength() {
            String result = CaptchaUtil.getRandomString(0);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：负数长度；预期结果：返回最大长度字符串；测试点：负数处理")
        void getRandomString_WithNegativeLength_ShouldHandle() {
            String result = CaptchaUtil.getRandomString(-1);

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：标签密钥；预期结果：返回加密标签；测试点：生成标签")
        void getTag_WithValidParams_ShouldReturnTag() throws Exception {
            String result = CaptchaUtil.getTag("1234", "1234567890123456", "1234567890123456");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：加密标签；预期结果：返回解密对象；测试点：解密标签")
        void decodeTag_WithValidTag_ShouldDecrypt() throws Exception {
            String secret = "1234567890123456";
            String iv = "1234567890123456";
            String tag = CaptchaUtil.getTag("1234", secret, iv);

            LoginCaptchaBO result = CaptchaUtil.decodeTag(secret, iv, tag);

            assertThat(result).isNotNull();
            assertThat(result.getCaptcha()).isEqualTo("1234");
        }

        @Test
        @DisplayName("输入参数：验证码；预期结果：返回base64图片；测试点：生成验证码图片")
        void getRandomCodeBase64_ShouldReturnBase64() throws Exception {
            String result = CaptchaUtil.getRandomCodeBase64("1234");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：明文；预期结果：返回加密字符串；测试点：AES加密")
        void aesEncode_WithContent_ShouldEncrypt() throws GeneralSecurityException {
            String result = CaptchaUtil.aesEncode("1234567890123456", "1234567890123456", "test content");

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：加密内容；预期结果：返回解密字符串；测试点：AES解密")
        void aesDecode_WithEncrypted_ShouldDecrypt() throws GeneralSecurityException {
            String secret = "1234567890123456";
            String iv = "1234567890123456";
            String content = "test content";
            String encrypted = CaptchaUtil.aesEncode(secret, iv, content);

            String result = CaptchaUtil.aesDecode(secret, iv, encrypted);

            assertThat(result).isEqualTo(content);
        }
    }

    @Nested
    @DisplayName("BeanCopyUtil工具测试")
    class BeanCopyUtilTests {

        @Test
        @DisplayName("输入参数：源对象和目标Supplier；预期结果：返回目标对象；测试点：对象属性拷贝")
        void copyProperties_WithValidObject_ShouldCopyProperties() {
            BookDO source = new BookDO();
            source.setId(1);
            source.setTitle("Test Book");
            Supplier<BookDO> target = BookDO::new;

            BookDO result = BeanCopyUtil.copyProperties(source, target);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1);
            assertThat(result.getTitle()).isEqualTo("Test Book");
        }

        @Test
        @DisplayName("输入参数：源列表和目标Supplier；预期结果：返回目标列表；测试点：列表属性拷贝")
        void copyListProperties_WithValidList_ShouldReturnList() {
            List<BookDO> source = Arrays.asList(new BookDO(), new BookDO());
            source.get(0).setId(1);
            source.get(1).setId(2);

            List<BookDO> result = BeanCopyUtil.copyListProperties(source, BookDO::new);

            assertThat(result).hasSize(2);
            assertThat(result.get(0).getId()).isEqualTo(1);
        }

        @Test
        @DisplayName("输入参数：空列表；预期结果：返回空列表；测试点：空列表处理")
        void copyListProperties_WithEmptyList_ShouldReturnEmpty() {
            List<BookDO> result = BeanCopyUtil.copyListProperties(Collections.emptyList(), BookDO::new);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("输入参数：非null属性；预期结果：只拷贝非null属性；测试点：非null属性拷贝")
        void copyNonNullProperties_ShouldSkipNullValues() {
            BookDO source = new BookDO();
            source.setId(1);
            source.setTitle(null);
            BookDO target = new BookDO();
            target.setTitle("Original Title");

            BeanCopyUtil.copyNonNullProperties(source, target);

            assertThat(target.getId()).isEqualTo(1);
            assertThat(target.getTitle()).isEqualTo("Original Title");
        }

        @Test
        @DisplayName("输入参数：带回调的单个对象拷贝；预期结果：执行回调；测试点：带回调的属性拷贝")
        void copySingleProperties_WithCallback_ShouldExecuteCallback() {
            BookDO source = new BookDO();
            source.setId(1);

            BookDO result = BeanCopyUtil.copySingleProperties(source, BookDO::new, (s, t) -> t.setAuthor("test"));

            assertThat(result).isNotNull();
            assertThat(result.getAuthor()).isEqualTo("test");
        }

        @Test
        @DisplayName("输入参数：集合对象转换；预期结果：返回转换后的集合；测试点：集合类型转换")
        void covertObject_WithCollection_ShouldConvert() {
            List<BookDO> source = Collections.singletonList(new BookDO());
            source.get(0).setId(1);

            Collection<BookDO> result = BeanCopyUtil.covertObject(source, BookDO.class, null);

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("输入参数：空集合转换；预期结果：返回空集合；测试点：空集合转换处理")
        void covertObject_WithEmptyCollection_ShouldReturnEmpty() {
            Collection<BookDO> result = BeanCopyUtil.covertObject(Collections.emptyList(), BookDO.class, null);

            assertThat(result).isEmpty();
        }
    }

    @Nested
    @DisplayName("PageUtil工具测试")
    class PageUtilTests {

        @Test
        @DisplayName("输入参数：IPage对象；预期结果：返回PageResponseVO；测试点：构建分页响应")
        void build_WithIPage_ShouldReturnPageResponse() {
            IPage<BookDO> iPage = new Page<>(1, 10);
            iPage.setTotal(100);

            PageResponseVO<BookDO> result = PageUtil.build(iPage);

            assertThat(result).isNotNull();
            assertThat(result.getPage()).isEqualTo(1);
            assertThat(result.getCount()).isEqualTo(10);
            assertThat(result.getTotal()).isEqualTo(100);
        }

        @Test
        @DisplayName("输入参数：IPage和记录列表；预期结果：返回包含记录的分页响应；测试点：带记录的分页构建")
        void build_WithIPageAndRecords_ShouldReturnPageResponse() {
            IPage<BookDO> iPage = new Page<>(1, 10);
            iPage.setTotal(100);
            List<String> records = Arrays.asList("a", "b", "c");

            PageResponseVO<String> result = PageUtil.build(iPage, records);

            assertThat(result).isNotNull();
            assertThat(result.getItems()).isEqualTo(records);
        }
    }
}
