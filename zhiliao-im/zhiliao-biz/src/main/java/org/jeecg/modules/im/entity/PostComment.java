package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 动态评论列表
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_post_comment")
public class PostComment extends BaseModel<PostComment> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 动态id
     */
    private Integer postId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 回复对象
     */
    private Integer parentId;

    /**
     * 主评论id
     */
    private Integer commentId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论创建时间
     */
    private Long tsCreate;

    /**
     * 置顶时间
     */
    private Long tsPin;

    @TableLogic
    private Integer delFlag;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private PostComment parent;

}
