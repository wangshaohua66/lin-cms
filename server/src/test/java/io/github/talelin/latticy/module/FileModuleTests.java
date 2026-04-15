package io.github.talelin.latticy.module;

import io.github.talelin.latticy.module.file.FileProperties;
import io.github.talelin.latticy.module.file.FileUtil;
import io.github.talelin.latticy.module.file.FileTypeEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("文件模块单元测试")
class FileModuleTests {

    @Nested
    @DisplayName("FileUtil工具测试")
    class FileUtilTests {

        @Test
        @DisplayName("输入参数：无；预期结果：返回文件系统；测试点：获取默认文件系统")
        void getDefaultFileSystem_ShouldReturnFileSystem() {
            FileSystem result = FileUtil.getDefaultFileSystem();

            assertThat(result).isNotNull();
        }

        @Test
        @DisplayName("输入参数：绝对路径；预期结果：返回true；测试点：检查绝对路径")
        void isAbsolute_WithAbsolutePath_ShouldReturnTrue() {
            boolean result = FileUtil.isAbsolute("/usr/local/test");

            assertThat(result).isTrue();
        }

        @Test
        @DisplayName("输入参数：相对路径；预期结果：返回false；测试点：检查相对路径")
        void isAbsolute_WithRelativePath_ShouldReturnFalse() {
            boolean result = FileUtil.isAbsolute("relative/path");

            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("输入参数：无；预期结果：返回当前目录；测试点：获取当前目录")
        void getCmd_ShouldReturnCurrentDirectory() {
            String result = FileUtil.getCmd();

            assertThat(result).isNotNull();
            assertThat(result).isEqualTo(System.getProperty("user.dir"));
        }

        @Test
        @DisplayName("输入参数：文件名；预期结果：返回文件扩展名；测试点：获取文件扩展名")
        void getFileExt_WithFilename_ShouldReturnExtension() {
            String result = FileUtil.getFileExt("test.jpg");

            assertThat(result).isEqualTo(".jpg");
        }

        @Test
        @DisplayName("输入参数：文件字节数组；预期结果：返回MD5；测试点：计算文件MD5")
        void getFileMD5_WithBytes_ShouldReturnMD5() {
            byte[] bytes = "test content".getBytes();

            String result = FileUtil.getFileMD5(bytes);

            assertThat(result).isNotNull();
            assertThat(result).hasSize(32);
        }

        @Test
        @DisplayName("输入参数：大小字符串；预期结果：返回字节数；测试点：解析文件大小")
        void parseSize_WithSizeString_ShouldReturnBytes() {
            Long result = FileUtil.parseSize("10MB");

            assertThat(result).isEqualTo(10 * 1024 * 1024);
        }

        @Test
        @DisplayName("输入参数：目录和文件名；预期结果：返回绝对路径；测试点：获取文件绝对路径")
        void getFileAbsolutePath_ShouldReturnFullPath() {
            String result = FileUtil.getFileAbsolutePath("testdir", "test.txt");

            assertThat(result).isNotNull();
        }
    }

    @Nested
    @DisplayName("FileTypeEnum测试")
    class FileTypeEnumTests {

        @Test
        @DisplayName("输入参数：枚举值；预期结果：返回正确值；测试点：枚举值正确性")
        void values_ShouldBeCorrect() {
            assertThat(FileTypeEnum.LOCAL.getValue()).isEqualTo("LOCAL");
            assertThat(FileTypeEnum.REMOTE.getValue()).isEqualTo("REMOTE");
        }
    }

    @Nested
    @DisplayName("FileProperties测试")
    class FilePropertiesTests {

        @Test
        @DisplayName("输入参数：属性设置；预期结果：getter返回正确值；测试点：属性getter/setter")
        void properties_GettersSetters_ShouldWork() {
            FileProperties properties = new FileProperties();
            properties.setSingleLimit("10MB");
            properties.setNums(10);
            properties.setStoreDir("uploads");

            assertThat(properties.getSingleLimit()).isEqualTo("10MB");
            assertThat(properties.getNums()).isEqualTo(10);
            assertThat(properties.getStoreDir()).isEqualTo("uploads");
        }

        @Test
        @DisplayName("输入参数：包含/排除扩展名；预期结果：正确返回；测试点：包含/排除属性")
        void includeExclude_ShouldWork() {
            FileProperties properties = new FileProperties();
            String[] include = {"jpg", "png"};
            String[] exclude = {"exe", "bat"};
            properties.setInclude(include);
            properties.setExclude(exclude);

            assertThat(properties.getInclude()).isEqualTo(include);
            assertThat(properties.getExclude()).isEqualTo(exclude);
        }
    }
}
