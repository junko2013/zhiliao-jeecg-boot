package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 动态操作
 * </p>
 *
 * @author junko
 * @since 2024-02-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_post_operate")
public class PostOperate extends Model<PostOperate> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //浏览时间
    private Long tsCreate;
    //点赞时间
    private Long tsLike;
    //点踩时间
    private Long tsDislike;
    //收藏时间
    private Long tsStar;

    private Integer userId;

    private Integer postId;

    @TableField(exist = false)
    private User user;
}
