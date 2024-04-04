package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 群聊消息删除记录
 * </p>
 *
 * @author junko
 * @since 2023-12-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_muc_msg_delete")
public class MucMsgDelete extends Model<MucMsgDelete> {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long msgId;

    private Integer userId;

}
