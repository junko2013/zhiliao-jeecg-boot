package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

import java.util.List;

/**
 * <p>
 * 动态
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Post.TABLE_NAME)
public class Post extends BaseModel<Post> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_post";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 内容
     */
    private String content;

    /**
     * 动态创建时间
     */
    private Long tsCreate;

    /**
     * 当前位置
     */
    private String address;

    @TableLogic
    private Integer delFlag;

    /**
     * 置顶标识
     */
    private Long tsPin;

    /**
     * 1显示 0隐藏
     */
    @Dict(dicCode = "status")
    private Integer status;
    /**
     * 可见性
     */
    private Integer visibility;
    /**
     * 可见性的用户id
     */
    private String userIds;

    private String serverId;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private List<User> users;
    @TableField(exist = false)
    private List<PostComment> comments;
    @TableField(exist = false)
    private List<PostOperate> operates;
    @TableField(exist = false)
    private List<PostItem> items;


    public enum Visibility{
        all(0,"公开"),none(1,"仅自己可见"),not_all(2,"部分可见"),not_none(3,"部分不可见");
        int code;
        String info;
        Visibility(int code,String info){
            this.code = code;
            this.info = info;
        }
    }
}
