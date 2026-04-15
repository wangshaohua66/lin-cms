package io.github.talelin.latticy.common.enumeration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * GroupLevelEnum 单元测试
 * 测试点：分组级别枚举功能
 */
@DisplayName("分组级别枚举测试")
class GroupLevelEnumTest {

    @Test
    @DisplayName("获取ROOT级别值")
    void getValue_ForRoot_ShouldReturnOne() {
        // 测试点：验证ROOT级别值
        assertThat(GroupLevelEnum.ROOT.getValue()).isEqualTo(1);
    }

    @Test
    @DisplayName("获取GUEST级别值")
    void getValue_ForGuest_ShouldReturnTwo() {
        // 测试点：验证GUEST级别值
        assertThat(GroupLevelEnum.GUEST.getValue()).isEqualTo(2);
    }

    @Test
    @DisplayName("获取USER级别值")
    void getValue_ForUser_ShouldReturnThree() {
        // 测试点：验证USER级别值
        assertThat(GroupLevelEnum.USER.getValue()).isEqualTo(3);
    }

    @Test
    @DisplayName("枚举值数量")
    void values_ShouldHaveThreeElements() {
        // 测试点：验证枚举值数量
        GroupLevelEnum[] values = GroupLevelEnum.values();
        assertThat(values).hasSize(3);
    }

    @Test
    @DisplayName("根据值获取枚举 - ROOT")
    void valueOf_ForRootValue_ShouldReturnRoot() {
        // 测试点：验证根据值获取枚举
        for (GroupLevelEnum e : GroupLevelEnum.values()) {
            if (e.getValue() == 1) {
                assertThat(e).isEqualTo(GroupLevelEnum.ROOT);
            }
        }
    }

    @Test
    @DisplayName("枚举名称 - ROOT")
    void name_ForRoot_ShouldReturnRoot() {
        // 测试点：验证枚举名称
        assertThat(GroupLevelEnum.ROOT.name()).isEqualTo("ROOT");
    }

    @Test
    @DisplayName("枚举名称 - GUEST")
    void name_ForGuest_ShouldReturnGuest() {
        // 测试点：验证枚举名称
        assertThat(GroupLevelEnum.GUEST.name()).isEqualTo("GUEST");
    }

    @Test
    @DisplayName("枚举名称 - USER")
    void name_ForUser_ShouldReturnUser() {
        // 测试点：验证枚举名称
        assertThat(GroupLevelEnum.USER.name()).isEqualTo("USER");
    }

    @Test
    @DisplayName("枚举顺序 - ROOT")
    void ordinal_ForRoot_ShouldReturnZero() {
        // 测试点：验证枚举顺序
        assertThat(GroupLevelEnum.ROOT.ordinal()).isEqualTo(0);
    }

    @Test
    @DisplayName("枚举顺序 - GUEST")
    void ordinal_ForGuest_ShouldReturnOne() {
        // 测试点：验证枚举顺序
        assertThat(GroupLevelEnum.GUEST.ordinal()).isEqualTo(1);
    }

    @Test
    @DisplayName("枚举顺序 - USER")
    void ordinal_ForUser_ShouldReturnTwo() {
        // 测试点：验证枚举顺序
        assertThat(GroupLevelEnum.USER.ordinal()).isEqualTo(2);
    }
}
