package io.github.talelin.latticy.common.util;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * BeanCopyUtil单元测试
 */
@DisplayName("BeanCopyUtil测试")
class BeanCopyUtilTest {

    @Data
    static class SourceBean {
        private String name;
        private Integer age;
        private String email;
    }

    @Data
    static class TargetBean {
        private String name;
        private Integer age;
        private String email;
    }

    @Test
    @DisplayName("copyProperties - 基本复制")
    void copyProperties_BasicCopy() {
        SourceBean source = new SourceBean();
        source.setName("张三");
        source.setAge(25);
        source.setEmail("zhangsan@example.com");

        TargetBean target = BeanCopyUtil.copyProperties(source, TargetBean::new);

        assertThat(target.getName()).isEqualTo("张三");
        assertThat(target.getAge()).isEqualTo(25);
        assertThat(target.getEmail()).isEqualTo("zhangsan@example.com");
    }

    @Test
    @DisplayName("copyProperties - 复制null值")
    void copyProperties_WithNullValue() {
        SourceBean source = new SourceBean();
        source.setName(null);
        source.setAge(25);
        source.setEmail(null);

        TargetBean target = new TargetBean();
        target.setName("默认值");
        target.setEmail("default@example.com");

        BeanCopyUtil.copyProperties(source, target);

        assertThat(target.getName()).isNull();
        assertThat(target.getAge()).isEqualTo(25);
        assertThat(target.getEmail()).isNull();
    }

    @Test
    @DisplayName("copyListProperties - 列表复制")
    void copyListProperties_ListCopy() {
        SourceBean source1 = new SourceBean();
        source1.setName("张三");
        source1.setAge(25);
        source1.setEmail("zhangsan@example.com");

        SourceBean source2 = new SourceBean();
        source2.setName("李四");
        source2.setAge(30);
        source2.setEmail("lisi@example.com");

        List<SourceBean> sourceList = Arrays.asList(source1, source2);

        List<TargetBean> targetList = BeanCopyUtil.copyListProperties(sourceList, TargetBean::new);

        assertThat(targetList).hasSize(2);
        assertThat(targetList.get(0).getName()).isEqualTo("张三");
        assertThat(targetList.get(0).getAge()).isEqualTo(25);
        assertThat(targetList.get(1).getName()).isEqualTo("李四");
        assertThat(targetList.get(1).getAge()).isEqualTo(30);
    }

    @Test
    @DisplayName("copyListProperties - 空列表")
    void copyListProperties_EmptyList() {
        List<SourceBean> sourceList = Arrays.asList();

        List<TargetBean> targetList = BeanCopyUtil.copyListProperties(sourceList, TargetBean::new);

        assertThat(targetList).isEmpty();
    }

    @Test
    @DisplayName("copyListProperties - null列表")
    void copyListProperties_NullList() {
        List<TargetBean> targetList = BeanCopyUtil.copyListProperties(null, TargetBean::new);

        assertThat(targetList).isEmpty();
    }

    @Test
    @DisplayName("copyNonNullProperties - 只复制非null属性")
    void copyNonNullProperties_OnlyNonNull() {
        SourceBean source = new SourceBean();
        source.setName("张三");
        source.setAge(null);
        source.setEmail("zhangsan@example.com");

        TargetBean target = new TargetBean();
        target.setName("默认值");
        target.setAge(20);
        target.setEmail("default@example.com");

        BeanCopyUtil.copyNonNullProperties(source, target);

        // name和email被复制，age保持原值
        assertThat(target.getName()).isEqualTo("张三");
        assertThat(target.getAge()).isEqualTo(20);
        assertThat(target.getEmail()).isEqualTo("zhangsan@example.com");
    }

    @Test
    @DisplayName("copyNonNullProperties - 源对象所有属性为null")
    void copyNonNullProperties_AllNullSource() {
        SourceBean source = new SourceBean();
        source.setName(null);
        source.setAge(null);
        source.setEmail(null);

        TargetBean target = new TargetBean();
        target.setName("默认值");
        target.setAge(20);
        target.setEmail("default@example.com");

        BeanCopyUtil.copyNonNullProperties(source, target);

        // 所有属性保持原值
        assertThat(target.getName()).isEqualTo("默认值");
        assertThat(target.getAge()).isEqualTo(20);
        assertThat(target.getEmail()).isEqualTo("default@example.com");
    }

    @Test
    @DisplayName("copyNonNullProperties - 源对象所有属性非null")
    void copyNonNullProperties_AllNonNullSource() {
        SourceBean source = new SourceBean();
        source.setName("张三");
        source.setAge(25);
        source.setEmail("zhangsan@example.com");

        TargetBean target = new TargetBean();
        target.setName("默认值");
        target.setAge(20);
        target.setEmail("default@example.com");

        BeanCopyUtil.copyNonNullProperties(source, target);

        // 所有属性被复制
        assertThat(target.getName()).isEqualTo("张三");
        assertThat(target.getAge()).isEqualTo(25);
        assertThat(target.getEmail()).isEqualTo("zhangsan@example.com");
    }
}
