package io.github.talelin.latticy.module.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("文件工具类测试")
class FileUtilTest {

    @Nested
    @DisplayName("getFileExt测试")
    class GetFileExtTests {

        @Test
        @DisplayName("获取文件扩展名 - 正常")
        void getFileExt_ShouldReturnExtension() {
            String ext = FileUtil.getFileExt("test.jpg");

            assertThat(ext).isEqualTo(".jpg");
        }

        @Test
        @DisplayName("获取文件扩展名 - 多个点")
        void getFileExt_WithMultipleDots_ShouldReturnLastExtension() {
            String ext = FileUtil.getFileExt("test.file.name.png");

            assertThat(ext).isEqualTo(".png");
        }
    }

    @Nested
    @DisplayName("getFileMD5测试")
    class GetFileMD5Tests {

        @Test
        @DisplayName("获取文件MD5 - 正常")
        void getFileMD5_ShouldReturnMD5() {
            byte[] bytes = "test content".getBytes();

            String md5 = FileUtil.getFileMD5(bytes);

            assertThat(md5).isNotNull();
            assertThat(md5).hasSize(32);
        }
    }

    @Nested
    @DisplayName("parseSize测试")
    class ParseSizeTests {

        @Test
        @DisplayName("解析大小 - MB")
        void parseSize_WithMB_ShouldReturnBytes() {
            Long size = FileUtil.parseSize("10MB");

            assertThat(size).isEqualTo(10 * 1024 * 1024L);
        }

        @Test
        @DisplayName("解析大小 - KB")
        void parseSize_WithKB_ShouldReturnBytes() {
            Long size = FileUtil.parseSize("100KB");

            assertThat(size).isEqualTo(100 * 1024L);
        }

        @Test
        @DisplayName("解析大小 - GB")
        void parseSize_WithGB_ShouldReturnBytes() {
            Long size = FileUtil.parseSize("1GB");

            assertThat(size).isEqualTo(1024 * 1024 * 1024L);
        }
    }

    @Nested
    @DisplayName("isAbsolute测试")
    class IsAbsoluteTests {

        @Test
        @DisplayName("判断绝对路径 - 相对路径")
        void isAbsolute_WithRelativePath_ShouldReturnFalse() {
            boolean result = FileUtil.isAbsolute("relative/path");

            assertThat(result).isFalse();
        }
    }

    @Nested
    @DisplayName("getCmd测试")
    class GetCmdTests {

        @Test
        @DisplayName("获取当前目录")
        void getCmd_ShouldReturnCurrentDir() {
            String cmd = FileUtil.getCmd();

            assertThat(cmd).isNotNull();
            assertThat(cmd).isNotEmpty();
        }
    }
}
