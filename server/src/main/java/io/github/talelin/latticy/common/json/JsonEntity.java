package io.github.talelin.latticy.common.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.io.Serializable;

/**
 * JSON 数据实体基类
 * 替代 MyBatis 的 BaseModel
 */
@Data
public class JsonEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @JsonIgnore
    private Long createTime;

    @JsonIgnore
    private Long updateTime;

    @JsonIgnore
    private Long deleteTime;

    @JsonIgnore
    private Boolean isDeleted = false;
}
