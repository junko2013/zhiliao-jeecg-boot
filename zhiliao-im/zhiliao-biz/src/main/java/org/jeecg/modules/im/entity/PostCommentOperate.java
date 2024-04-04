package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 动态评论操作
 * </p>
 *
 * @author junko
 * @since 2024-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_post_comment_operate")
public class PostCommentOperate extends Model<PostCommentOperate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Long tsLike;

    /**
     * 点赞时间
     */
    private Long tsDislike;

    private Integer userId;

    private Integer commentId;

}
