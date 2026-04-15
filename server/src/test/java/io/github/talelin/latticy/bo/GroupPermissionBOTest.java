package io.github.talelin.latticy.bo;

import io.github.talelin.latticy.model.GroupDO;
import io.github.talelin.latticy.model.PermissionDO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("分组权限BO测试")
class GroupPermissionBOTest {

    @Nested
    @DisplayName("构造函数测试")
    class ConstructorTests {

        @Test
        @DisplayName("构造函数 - GroupDO和权限列表")
        void constructor_WithGroupAndPermissions_ShouldCreateBO() {
            GroupDO group = GroupDO.builder()
                    .name("测试分组")
                    .info("测试分组信息")
                    .build();
            group.setId(1);

            PermissionDO permission = PermissionDO.builder()
                    .name("查看图书")
                    .module("图书")
                    .build();

            GroupPermissionBO bo = new GroupPermissionBO(group, Arrays.asList(permission));

            assertThat(bo.getId()).isEqualTo(1);
            assertThat(bo.getName()).isEqualTo("测试分组");
            assertThat(bo.getInfo()).isEqualTo("测试分组信息");
            assertThat(bo.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("构造函数 - 空权限列表")
        void constructor_WithEmptyPermissions_ShouldCreateBO() {
            GroupDO group = GroupDO.builder()
                    .name("测试分组")
                    .build();
            group.setId(1);

            GroupPermissionBO bo = new GroupPermissionBO(group, Collections.emptyList());

            assertThat(bo.getPermissions()).isEmpty();
        }

        @Test
        @DisplayName("全参构造函数")
        void allArgsConstructor_ShouldCreateBO() {
            PermissionDO permission = PermissionDO.builder().name("权限").build();

            GroupPermissionBO bo = new GroupPermissionBO(1, "分组名", "分组信息", Arrays.asList(permission));

            assertThat(bo.getId()).isEqualTo(1);
            assertThat(bo.getName()).isEqualTo("分组名");
            assertThat(bo.getInfo()).isEqualTo("分组信息");
            assertThat(bo.getPermissions()).hasSize(1);
        }

        @Test
        @DisplayName("无参构造函数")
        void noArgsConstructor_ShouldCreateBO() {
            GroupPermissionBO bo = new GroupPermissionBO();

            assertThat(bo).isNotNull();
            assertThat(bo.getId()).isNull();
            assertThat(bo.getName()).isNull();
            assertThat(bo.getInfo()).isNull();
            assertThat(bo.getPermissions()).isNull();
        }
    }

    @Nested
    @DisplayName("Setter/Getter测试")
    class SetterGetterTests {

        @Test
        @DisplayName("Setter/Getter - 正常设置和获取")
        void setterGetter_ShouldWorkCorrectly() {
            GroupPermissionBO bo = new GroupPermissionBO();
            PermissionDO permission = PermissionDO.builder().name("权限").build();

            bo.setId(1);
            bo.setName("分组名");
            bo.setInfo("分组信息");
            bo.setPermissions(Arrays.asList(permission));

            assertThat(bo.getId()).isEqualTo(1);
            assertThat(bo.getName()).isEqualTo("分组名");
            assertThat(bo.getInfo()).isEqualTo("分组信息");
            assertThat(bo.getPermissions()).hasSize(1);
        }
    }
}
