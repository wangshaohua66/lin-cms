package io.github.talelin.latticy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("文件DO测试")
class FileDOTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            FileDO file = new FileDO();

            file.setPath("/uploads/test.jpg");
            file.setType("LOCAL");
            file.setName("test.jpg");
            file.setExtension(".jpg");
            file.setSize(1024);
            file.setMd5("abc123");

            assertThat(file.getPath()).isEqualTo("/uploads/test.jpg");
            assertThat(file.getType()).isEqualTo("LOCAL");
            assertThat(file.getName()).isEqualTo("test.jpg");
            assertThat(file.getExtension()).isEqualTo(".jpg");
            assertThat(file.getSize()).isEqualTo(1024);
            assertThat(file.getMd5()).isEqualTo("abc123");
        }
    }

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("无参构造函数 - 创建对象")
        void noArgsConstructor_ShouldCreateObject() {
            FileDO file = new FileDO();

            assertThat(file).isNotNull();
            assertThat(file.getPath()).isNull();
            assertThat(file.getType()).isNull();
            assertThat(file.getName()).isNull();
        }
    }
}
