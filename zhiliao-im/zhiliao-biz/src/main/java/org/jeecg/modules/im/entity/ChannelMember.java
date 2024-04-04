package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 频道成员
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_channel_member")
public class ChannelMember extends BaseModel<ChannelMember> {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Integer channelId;

    private Integer userId;

    private Integer tsCreate;

    /**
     * 是管理员
     */
    @Dict(dicCode = "yon")
    private Boolean isManager;

    /**
     * 是创建者
     */
    @Dict(dicCode = "yon")
    private Boolean isCreator;
}
