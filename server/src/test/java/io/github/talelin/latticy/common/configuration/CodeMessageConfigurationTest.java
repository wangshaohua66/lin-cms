package io.github.talelin.latticy.common.configuration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("消息码配置测试")
class CodeMessageConfigurationTest {

    @Nested
    @DisplayName("getMessage测试")
    class GetMessageTests {

        @Test
        @DisplayName("获取消息 - 设置后获取")
        void getMessage_AfterSet_ShouldReturnMessage() {
            Map<Integer, String> messages = new HashMap<>();
            messages.put(10000, "测试消息");
            CodeMessageConfiguration config = new CodeMessageConfiguration();
            config.setCodeMessage(messages);

            String message = CodeMessageConfiguration.getMessage(10000);

            assertThat(message).isEqualTo("测试消息");
        }

        @Test
        @DisplayName("获取消息 - 不存在的code")
        void getMessage_WhenNotExists_ShouldReturnNull() {
            String message = CodeMessageConfiguration.getMessage(99999);

            assertThat(message).isNull();
        }
    }

    @Nested
    @DisplayName("Getter/Setter测试")
    class GetterSetterTests {

        @Test
        @DisplayName("getCodeMessage - 正常获取")
        void getCodeMessage_ShouldReturnMap() {
            Map<Integer, String> messages = new HashMap<>();
            messages.put(1, "消息1");
            CodeMessageConfiguration config = new CodeMessageConfiguration();
            config.setCodeMessage(messages);

            Map<Integer, String> result = config.getCodeMessage();

            assertThat(result).containsEntry(1, "消息1");
        }
    }
}
