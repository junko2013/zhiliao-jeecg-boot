package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 意见反馈
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_feedback")
public class Feedback extends BaseModel<Feedback> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer typeId;

    private Integer userId;

    private Long tsCreate;

    private String imgUrl;

    private String title;

    private String content;
    //处理反馈
    private String reply;

    /**
     * 联系方式
     */
    private String contact;

    private Long tsDeal;

    private Integer serverId;
    @TableLogic
    private Integer delFlag;

    @TableField(exist = false)
    private Boolean isDeal;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private FeedbackType type;

}
