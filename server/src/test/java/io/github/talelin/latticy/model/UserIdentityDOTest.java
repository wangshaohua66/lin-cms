package io.github.talelin.latticy.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("用户身份DO测试")
class UserIdentityDOTest {

    @Nested
    @DisplayName("Builder测试")
    class BuilderTests {

        @Test
        @DisplayName("Builder - 正常构建")
        void builder_ShouldBuildSuccessfully() {
            UserIdentityDO identity = UserIdentityDO.builder()
                    .userId(1)
                    .identityType("USERNAME_PASSWORD")
                    .identifier("testuser")
                    .credential("password123")
                    .build();

            assertThat(identity.getUserId()).isEqualTo(1);
            assertThat(identity.getIdentityType()).isEqualTo("USERNAME_PASSWORD");
            assertThat(identity.getIdentifier()).isEqualTo("testuser");
            assertThat(identity.getCredential()).isEqualTo("password123");
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            UserIdentityDO identity = new UserIdentityDO();

            identity.setUserId(1);
            identity.setIdentityType("USERNAME_PASSWORD");
            identity.setIdentifier("testuser");
            identity.setCredential("password123");

            assertThat(identity.getUserId()).isEqualTo(1);
            assertThat(identity.getIdentityType()).isEqualTo("USERNAME_PASSWORD");
            assertThat(identity.getIdentifier()).isEqualTo("testuser");
            assertThat(identity.getCredential()).isEqualTo("password123");
        }
    }

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("无参构造函数")
        void noArgsConstructor_ShouldCreateObject() {
            UserIdentityDO identity = new UserIdentityDO();

            assertThat(identity).isNotNull();
        }

        @Test
        @DisplayName("全参构造函数")
        void allArgsConstructor_ShouldCreateObject() {
            UserIdentityDO identity = new UserIdentityDO(1, "USERNAME_PASSWORD", "testuser", "password123");

            assertThat(identity.getUserId()).isEqualTo(1);
            assertThat(identity.getIdentityType()).isEqualTo("USERNAME_PASSWORD");
        }
    }
}
