package io.github.talelin.latticy.common.util;

import lombok.Data;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Bean复制工具类测试")
class BeanCopyUtilTest {

    @Data
    static class Source {
        private String name;
        private Integer age;
        private String address;

        public Source() {}

        public Source(String name, Integer age, String address) {
            this.name = name;
            this.age = age;
            this.address = address;
        }
    }

    @Data
    static class Target {
        private String name;
        private Integer age;
        private String address;

        public Target() {}
    }

    @Nested
    @DisplayName("复制非空属性测试")
    class CopyNonNullPropertiesTests {

        @Test
        @DisplayName("复制非空属性 - 全部非空")
        void copyNonNullProperties_AllNonNull_ShouldCopyAll() {
            Source source = new Source("张三", 25, "北京");
            Target target = new Target();

            BeanCopyUtil.copyNonNullProperties(source, target);

            assertThat(target.getName()).isEqualTo("张三");
            assertThat(target.getAge()).isEqualTo(25);
            assertThat(target.getAddress()).isEqualTo("北京");
        }

        @Test
        @DisplayName("复制非空属性 - 部分非空")
        void copyNonNullProperties_PartialNonNull_ShouldCopyOnlyNonNull() {
            Source source = new Source("张三", null, "北京");
            Target target = new Target();
            target.setAge(30);

            BeanCopyUtil.copyNonNullProperties(source, target);

            assertThat(target.getName()).isEqualTo("张三");
            assertThat(target.getAge()).isEqualTo(30);
            assertThat(target.getAddress()).isEqualTo("北京");
        }

        @Test
        @DisplayName("复制非空属性 - 全部为空")
        void copyNonNullProperties_AllNull_ShouldNotCopy() {
            Source source = new Source(null, null, null);
            Target target = new Target();
            target.setName("原名称");
            target.setAge(20);
            target.setAddress("原地址");

            BeanCopyUtil.copyNonNullProperties(source, target);

            assertThat(target.getName()).isEqualTo("原名称");
            assertThat(target.getAge()).isEqualTo(20);
            assertThat(target.getAddress()).isEqualTo("原地址");
        }
    }

    @Nested
    @DisplayName("复制单个对象测试")
    class CopyPropertiesTests {

        @Test
        @DisplayName("复制单个对象 - 正常复制")
        void copyProperties_ShouldCopySuccessfully() {
            Source source = new Source("李四", 30, "上海");

            Target target = BeanCopyUtil.copyProperties(source, Target::new);

            assertThat(target).isNotNull();
            assertThat(target.getName()).isEqualTo("李四");
            assertThat(target.getAge()).isEqualTo(30);
            assertThat(target.getAddress()).isEqualTo("上海");
        }

        @Test
        @DisplayName("复制单个对象 - 空源对象")
        void copyProperties_WithEmptySource_ShouldCopyEmptyValues() {
            Source source = new Source();

            Target target = BeanCopyUtil.copyProperties(source, Target::new);

            assertThat(target).isNotNull();
            assertThat(target.getName()).isNull();
            assertThat(target.getAge()).isNull();
            assertThat(target.getAddress()).isNull();
        }
    }

    @Nested
    @DisplayName("复制列表测试")
    class CopyListPropertiesTests {

        @Test
        @DisplayName("复制列表 - 正常复制")
        void copyListProperties_ShouldCopySuccessfully() {
            List<Source> sources = Arrays.asList(
                    new Source("张三", 25, "北京"),
                    new Source("李四", 30, "上海")
            );

            List<Target> targets = BeanCopyUtil.copyListProperties(sources, Target::new);

            assertThat(targets).hasSize(2);
            assertThat(targets.get(0).getName()).isEqualTo("张三");
            assertThat(targets.get(1).getName()).isEqualTo("李四");
        }

        @Test
        @DisplayName("复制列表 - 空列表")
        void copyListProperties_WithEmptyList_ShouldReturnEmptyList() {
            List<Target> targets = BeanCopyUtil.copyListProperties(Collections.emptyList(), Target::new);

            assertThat(targets).isEmpty();
        }

        @Test
        @DisplayName("复制列表 - null列表")
        void copyListProperties_WithNullList_ShouldReturnEmptyList() {
            List<Target> targets = BeanCopyUtil.copyListProperties(null, Target::new);

            assertThat(targets).isEmpty();
        }

        @Test
        @DisplayName("复制列表 - 带回调函数")
        void copyListProperties_WithCallBack_ShouldInvokeCallBack() {
            List<Source> sources = Arrays.asList(new Source("张三", 25, "北京"));

            List<Target> targets = BeanCopyUtil.copyListProperties(sources, Target::new, (s, t) -> {
                t.setName(s.getName() + "_modified");
            });

            assertThat(targets.get(0).getName()).isEqualTo("张三_modified");
        }
    }

    @Nested
    @DisplayName("复制单个属性测试")
    class CopySinglePropertiesTests {

        @Test
        @DisplayName("复制单个属性 - 正常复制")
        void copySingleProperties_ShouldCopySuccessfully() {
            Source source = new Source("王五", 35, "广州");

            Target target = BeanCopyUtil.copySingleProperties(source, Target::new, null);

            assertThat(target.getName()).isEqualTo("王五");
            assertThat(target.getAge()).isEqualTo(35);
            assertThat(target.getAddress()).isEqualTo("广州");
        }

        @Test
        @DisplayName("复制单个属性 - 带回调函数")
        void copySingleProperties_WithCallBack_ShouldInvokeCallBack() {
            Source source = new Source("王五", 35, "广州");

            Target target = BeanCopyUtil.copySingleProperties(source, Target::new, (s, t) -> {
                t.setAge(s.getAge() + 1);
            });

            assertThat(target.getAge()).isEqualTo(36);
        }
    }

    @Nested
    @DisplayName("转换集合对象测试")
    class CovertObjectTests {

        @Test
        @DisplayName("转换集合对象 - 正常转换")
        void covertObject_ShouldConvertSuccessfully() {
            Collection<Source> sources = Arrays.asList(
                    new Source("张三", 25, "北京"),
                    new Source("李四", 30, "上海")
            );

            Collection<Target> targets = BeanCopyUtil.covertObject(sources, Target.class, null);

            assertThat(targets).hasSize(2);
        }

        @Test
        @DisplayName("转换集合对象 - 空集合")
        void covertObject_WithEmptyCollection_ShouldReturnEmptyCollection() {
            Collection<Target> targets = BeanCopyUtil.covertObject(Collections.emptyList(), Target.class, null);

            assertThat(targets).isEmpty();
        }

        @Test
        @DisplayName("转换集合对象 - null集合")
        void covertObject_WithNullCollection_ShouldReturnEmptyCollection() {
            Collection<Target> targets = BeanCopyUtil.covertObject(null, Target.class, null);

            assertThat(targets).isEmpty();
        }

        @Test
        @DisplayName("转换集合对象 - 带回调函数")
        void covertObject_WithCallBack_ShouldInvokeCallBack() {
            Collection<Source> sources = Arrays.asList(new Source("张三", 25, "北京"));

            Collection<Target> targets = BeanCopyUtil.covertObject(sources, Target.class, (s, t) -> {
                t.setName(s.getName().toUpperCase());
            });

            assertThat(targets.iterator().next().getName()).isEqualTo("张三".toUpperCase());
        }
    }
}
