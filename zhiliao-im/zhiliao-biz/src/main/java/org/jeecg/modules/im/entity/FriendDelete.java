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
 * 解除好友关系
 * </p>
 *
 * @author junko
 * @since 2024-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_friend_delete")
public class FriendDelete extends Model<FriendDelete> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer fromId;

    private Integer toId;

    private Long tsCreate;

    private Integer serverId;
    //发送人
    @TableField(exist = false)
    private User fromUser;
    //接收人
    @TableField(exist = false)
    private User toUser;

}
