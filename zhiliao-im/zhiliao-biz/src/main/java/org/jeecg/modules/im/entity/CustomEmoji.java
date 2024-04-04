package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import io.swagger.models.auth.In;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 自定义表情
 * </p>
 *
 * @author junko
 * @since 2023-02-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_custom_emoji")
public class CustomEmoji extends Model<CustomEmoji> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String origin;

    private String thumb;
    private Integer width;
    private Integer height;

    private Long tsCreate;
    private Long tsSend;

    @Dict(dicCode = "status")
    private Integer status;
    @TableLogic
    private Integer delFlag;
    private Long tsPin;
    private String keyword;

    private Integer serverId;

    @TableField(exist = false)
    private User user;
}
