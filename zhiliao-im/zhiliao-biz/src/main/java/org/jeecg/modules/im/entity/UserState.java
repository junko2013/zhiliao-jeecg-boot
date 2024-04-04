package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 用户状态历史
 * </p>
 *
 * @author junko
 * @since 2023-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_user_state")
public class UserState extends Model<UserState> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 自定义状态
     */
    @Dict(dicCode = "yon")
    private Boolean isDiy;

    /**
     * 内容
     */
    private String content;


    private Long tsCreate;

    /**
     * 失效时间，-1：长期有效，>0：具体时间
     */
    private Long tsValid;


    @TableField(exist = false)
    private User user;

}
