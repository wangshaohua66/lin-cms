package io.github.talelin.latticy.dto.book;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CreateOrUpdateBookDTO 单元测试类
 * 测试目标：分支覆盖率>=90%, 方法覆盖率>=90%, 代码行覆盖率>=80%
 */
@DisplayName("图书DTO测试")
class CreateOrUpdateBookDTOTest {

    private Validator validator;
    private CreateOrUpdateBookDTO dto;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        dto = new CreateOrUpdateBookDTO();
    }

    @Nested
    @DisplayName("Getter和Setter测试")
    class GetterSetterTests {

        @Test
        @DisplayName("设置和获取title")
        void setTitle_ShouldGetTitle() {
            dto.setTitle("测试图书");

            assertThat(dto.getTitle()).isEqualTo("测试图书");
        }

        @Test
        @DisplayName("设置和获取author")
        void setAuthor_ShouldGetAuthor() {
            dto.setAuthor("测试作者");

            assertThat(dto.getAuthor()).isEqualTo("测试作者");
        }

        @Test
        @DisplayName("设置和获取summary")
        void setSummary_ShouldGetSummary() {
            dto.setSummary("测试摘要");

            assertThat(dto.getSummary()).isEqualTo("测试摘要");
        }

        @Test
        @DisplayName("设置和获取image")
        void setImage_ShouldGetImage() {
            dto.setImage("http://example.com/image.jpg");

            assertThat(dto.getImage()).isEqualTo("http://example.com/image.jpg");
        }
    }

    @Nested
    @DisplayName("验证测试 - title字段")
    class TitleValidationTests {

        @Test
        @DisplayName("title为null - 验证失败")
        void title_WhenNull_ShouldFailValidation() {
            dto.setTitle(null);
            dto.setAuthor("作者");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
            assertThat(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("title"))).isTrue();
        }

        @Test
        @DisplayName("title为空字符串 - 验证失败")
        void title_WhenEmpty_ShouldFailValidation() {
            dto.setTitle("");
            dto.setAuthor("作者");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("title为空格 - 验证通过（@NotEmpty只检查非空）")
        void title_WhenBlank_ShouldPassValidation() {
            dto.setTitle("   ");
            dto.setAuthor("作者");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("title"))).isTrue();
        }

        @Test
        @DisplayName("title超过50字符 - 验证失败")
        void title_WhenTooLong_ShouldFailValidation() {
            dto.setTitle("a".repeat(51));
            dto.setAuthor("作者");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("title正好50字符 - 验证通过")
        void title_WhenExactly50Chars_ShouldPassValidation() {
            dto.setTitle("a".repeat(50));
            dto.setAuthor("作者");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("title"))).isTrue();
        }

        @Test
        @DisplayName("title正常值 - 验证通过")
        void title_WhenValid_ShouldPassValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("title"))).isTrue();
        }
    }

    @Nested
    @DisplayName("验证测试 - author字段")
    class AuthorValidationTests {

        @Test
        @DisplayName("author为null - 验证失败")
        void author_WhenNull_ShouldFailValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor(null);
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("author为空字符串 - 验证失败")
        void author_WhenEmpty_ShouldFailValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("author超过50字符 - 验证失败")
        void author_WhenTooLong_ShouldFailValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("a".repeat(51));
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("author正常值 - 验证通过")
        void author_WhenValid_ShouldPassValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("测试作者");
            dto.setSummary("摘要");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("author"))).isTrue();
        }
    }

    @Nested
    @DisplayName("验证测试 - summary字段")
    class SummaryValidationTests {

        @Test
        @DisplayName("summary为null - 验证失败")
        void summary_WhenNull_ShouldFailValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary(null);

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("summary为空字符串 - 验证失败")
        void summary_WhenEmpty_ShouldFailValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("summary超过1000字符 - 验证失败")
        void summary_WhenTooLong_ShouldFailValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("a".repeat(1001));

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("summary正好1000字符 - 验证通过")
        void summary_WhenExactly1000Chars_ShouldPassValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("a".repeat(1000));

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("summary"))).isTrue();
        }

        @Test
        @DisplayName("summary正常值 - 验证通过")
        void summary_WhenValid_ShouldPassValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("这是一本很好的书");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations.stream().noneMatch(v -> v.getPropertyPath().toString().equals("summary"))).isTrue();
        }
    }

    @Nested
    @DisplayName("验证测试 - image字段")
    class ImageValidationTests {

        @Test
        @DisplayName("image为null - 验证通过（可选字段）")
        void image_WhenNull_ShouldPassValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("摘要");
            dto.setImage(null);

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("image超过100字符 - 验证失败")
        void image_WhenTooLong_ShouldFailValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("摘要");
            dto.setImage("http://example.com/" + "a".repeat(100));

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isNotEmpty();
        }

        @Test
        @DisplayName("image正常值 - 验证通过")
        void image_WhenValid_ShouldPassValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("作者");
            dto.setSummary("摘要");
            dto.setImage("http://example.com/image.jpg");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isEmpty();
        }
    }

    @Nested
    @DisplayName("完整DTO验证测试")
    class FullDTOValidationTests {

        @Test
        @DisplayName("所有字段都有效 - 验证通过")
        void allFieldsValid_ShouldPassValidation() {
            dto.setTitle("测试图书");
            dto.setAuthor("测试作者");
            dto.setSummary("这是一本很好的书");
            dto.setImage("http://example.com/image.jpg");

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).isEmpty();
        }

        @Test
        @DisplayName("所有必填字段都无效 - 多个验证错误")
        void allRequiredFieldsInvalid_ShouldHaveMultipleViolations() {
            dto.setTitle(null);
            dto.setAuthor(null);
            dto.setSummary(null);

            Set<ConstraintViolation<CreateOrUpdateBookDTO>> violations = validator.validate(dto);

            assertThat(violations).hasSize(3);
        }
    }
}
