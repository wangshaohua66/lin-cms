package io.github.talelin.latticy.module.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("文件对象测试")
class FileTest {

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            File file = File.builder()
                    .url("http://example.com/test.jpg")
                    .key("file1")
                    .path("/uploads/test.jpg")
                    .type("LOCAL")
                    .name("test.jpg")
                    .extension(".jpg")
                    .size(1024)
                    .md5("abc123")
                    .build();

            assertThat(file.getUrl()).isEqualTo("http://example.com/test.jpg");
            assertThat(file.getKey()).isEqualTo("file1");
            assertThat(file.getPath()).isEqualTo("/uploads/test.jpg");
            assertThat(file.getType()).isEqualTo("LOCAL");
            assertThat(file.getName()).isEqualTo("test.jpg");
            assertThat(file.getExtension()).isEqualTo(".jpg");
            assertThat(file.getSize()).isEqualTo(1024);
            assertThat(file.getMd5()).isEqualTo("abc123");
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            File file = new File();

            file.setUrl("http://example.com/test.png");
            file.setKey("file2");
            file.setPath("/uploads/test.png");
            file.setType("REMOTE");
            file.setName("test.png");
            file.setExtension(".png");
            file.setSize(2048);
            file.setMd5("def456");

            assertThat(file.getUrl()).isEqualTo("http://example.com/test.png");
            assertThat(file.getKey()).isEqualTo("file2");
            assertThat(file.getPath()).isEqualTo("/uploads/test.png");
            assertThat(file.getType()).isEqualTo("REMOTE");
            assertThat(file.getName()).isEqualTo("test.png");
            assertThat(file.getExtension()).isEqualTo(".png");
            assertThat(file.getSize()).isEqualTo(2048);
            assertThat(file.getMd5()).isEqualTo("def456");
        }
    }

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("无参构造函数")
        void noArgsConstructor_ShouldCreateObject() {
            File file = new File();

            assertThat(file).isNotNull();
        }

        @Test
        @DisplayName("全参构造函数")
        void allArgsConstructor_ShouldCreateObject() {
            File file = new File(
                    "http://example.com/test.gif",
                    "file3",
                    "/uploads/test.gif",
                    "LOCAL",
                    "test.gif",
                    ".gif",
                    512,
                    "ghi789"
            );

            assertThat(file.getUrl()).isEqualTo("http://example.com/test.gif");
            assertThat(file.getKey()).isEqualTo("file3");
            assertThat(file.getSize()).isEqualTo(512);
        }
    }
}
