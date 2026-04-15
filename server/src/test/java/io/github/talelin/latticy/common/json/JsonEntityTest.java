package io.github.talelin.latticy.common.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JSON实体测试")
class JsonEntityTest {

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            JsonEntity entity = new JsonEntity();

            entity.setId(1);
            entity.setCreateTime(System.currentTimeMillis());
            entity.setUpdateTime(System.currentTimeMillis());
            entity.setDeleteTime(null);
            entity.setIsDeleted(false);

            assertThat(entity.getId()).isEqualTo(1);
            assertThat(entity.getCreateTime()).isNotNull();
            assertThat(entity.getUpdateTime()).isNotNull();
            assertThat(entity.getDeleteTime()).isNull();
            assertThat(entity.getIsDeleted()).isFalse();
        }
    }

    @Nested
    @DisplayName("默认值测试")
    class DefaultValueTests {

        @Test
        @DisplayName("默认值 - isDeleted应为false")
        void defaultValue_IsDeletedShouldBeFalse() {
            JsonEntity entity = new JsonEntity();

            assertThat(entity.getIsDeleted()).isFalse();
        }
    }
}
