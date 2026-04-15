package io.github.talelin.latticy.module.file;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 文件模块单元测试
 */
@DisplayName("文件模块测试")
class FileModuleTest {

    @Test
    @DisplayName("FileTypeEnum - 获取值")
    void fileTypeEnum_GetValue() {
        assertThat(FileTypeEnum.LOCAL.getValue()).isEqualTo("LOCAL");
        assertThat(FileTypeEnum.REMOTE.getValue()).isEqualTo("REMOTE");
    }

    @Test
    @DisplayName("File - 构造和getter/setter")
    void file_Test() {
        File file = new File();
        file.setUrl("http://example.com/file.jpg");
        file.setKey("upload");
        file.setPath("/upload/file.jpg");
        file.setType("LOCAL");
        file.setName("file.jpg");
        file.setExtension("jpg");
        file.setSize(1024);
        file.setMd5("abc123");

        assertThat(file.getUrl()).isEqualTo("http://example.com/file.jpg");
        assertThat(file.getKey()).isEqualTo("upload");
        assertThat(file.getPath()).isEqualTo("/upload/file.jpg");
        assertThat(file.getType()).isEqualTo("LOCAL");
        assertThat(file.getName()).isEqualTo("file.jpg");
        assertThat(file.getExtension()).isEqualTo("jpg");
        assertThat(file.getSize()).isEqualTo(1024);
        assertThat(file.getMd5()).isEqualTo("abc123");
    }

    @Test
    @DisplayName("File - Builder模式")
    void file_BuilderTest() {
        File file = File.builder()
                .url("http://example.com/file.jpg")
                .key("upload")
                .path("/upload/file.jpg")
                .type("LOCAL")
                .name("file.jpg")
                .extension("jpg")
                .size(1024)
                .md5("abc123")
                .build();

        assertThat(file.getUrl()).isEqualTo("http://example.com/file.jpg");
        assertThat(file.getKey()).isEqualTo("upload");
        assertThat(file.getPath()).isEqualTo("/upload/file.jpg");
        assertThat(file.getType()).isEqualTo("LOCAL");
        assertThat(file.getName()).isEqualTo("file.jpg");
        assertThat(file.getExtension()).isEqualTo("jpg");
        assertThat(file.getSize()).isEqualTo(1024);
        assertThat(file.getMd5()).isEqualTo("abc123");
    }

    @Test
    @DisplayName("File - 全参构造")
    void file_AllArgsConstructor() {
        File file = new File(
                "http://example.com/file.jpg",
                "upload",
                "/upload/file.jpg",
                "LOCAL",
                "file.jpg",
                "jpg",
                1024,
                "abc123"
        );

        assertThat(file.getUrl()).isEqualTo("http://example.com/file.jpg");
        assertThat(file.getKey()).isEqualTo("upload");
        assertThat(file.getPath()).isEqualTo("/upload/file.jpg");
        assertThat(file.getType()).isEqualTo("LOCAL");
        assertThat(file.getName()).isEqualTo("file.jpg");
        assertThat(file.getExtension()).isEqualTo("jpg");
        assertThat(file.getSize()).isEqualTo(1024);
        assertThat(file.getMd5()).isEqualTo("abc123");
    }

    @Test
    @DisplayName("FileProperties - 测试")
    void fileProperties_Test() {
        FileProperties properties = new FileProperties();
        properties.setServePath("/assets");
        properties.setDomain("http://localhost:5000");
        properties.setStoreDir("/data/upload");
        properties.setSingleLimit("2MB");
        properties.setNums(10);
        properties.setExclude(new String[]{"exe", "bat", "sh"});
        properties.setInclude(new String[]{"jpg", "png", "gif"});

        assertThat(properties.getServePath()).isEqualTo("/assets");
        assertThat(properties.getDomain()).isEqualTo("http://localhost:5000");
        assertThat(properties.getStoreDir()).isEqualTo("/data/upload");
        assertThat(properties.getSingleLimit()).isEqualTo("2MB");
        assertThat(properties.getNums()).isEqualTo(10);
        assertThat(properties.getExclude()).containsExactly("exe", "bat", "sh");
        assertThat(properties.getInclude()).containsExactly("jpg", "png", "gif");
    }
}
