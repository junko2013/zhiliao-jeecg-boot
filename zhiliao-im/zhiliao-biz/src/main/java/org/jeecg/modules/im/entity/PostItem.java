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
 * 动态项
 * </p>
 *
 * @author junko
 * @since 2024-03-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_post_item")
public class PostItem extends Model<PostItem> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 动态
     */
    private Integer postId;

    /**
     * 用户
     */
    private Integer userId;

    /**
     * 原图
     */
    private String origin;

    /**
     * 缩略图
     */
    private String thumb;

    /**
     * 视频链接
     */
    private String url;

    /**
     * 宽
     */
    private Integer w;

    /**
     * 高
     */
    private Integer h;

    /**
     * 大小
     */
    private Integer size;

    /**
     * 文件类型，0：其他1：图片2：视频，3：音频
     */
    private Integer type;

    /**
     * 序号
     */
    private Integer orderNo;

    /**
     * 视频时长
     */
    private Integer duration;

    private Long tsCreate;
}
