package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 群消息已读记录
 * </p>
 *
 * @author junko
 * @since 2023-12-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_muc_msg_read")
public class MucMsgRead extends BaseModel<MucMsgRead> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String stanzaId;

    private Integer userId;

    private Long tsRead;


}
