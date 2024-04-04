package org.jeecg.modules.im.entity;
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
 * 频道管理员权限
 * </p>
 *
 * @author junko
 * @since 2022-04-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_channel_permission")
public class ChannelPermission extends BaseModel<ChannelPermission> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer channelId;

    private Integer memberId;

    /**
     * 更改频道信息
     */
    @Dict(dicCode = "yon")
    private Boolean modifyInfo;

    /**
     * 发布消息
     */
    @Dict(dicCode = "yon")
    private Boolean publishNews;

    /**
     * 编辑其他管理员的消息
     */
    @Dict(dicCode = "yon")
    private Boolean editOthersPublish;

    /**
     * 删除其他管理员的消息
     */
    @Dict(dicCode = "yon")
    private Boolean deleteOthersPublish;

    /**
     * 添加成员
     */
    @Dict(dicCode = "yon")
    private Boolean addMember;

    /**
     * 管理直播
     */
    @Dict(dicCode = "yon")
    private Boolean manageLive;

    /**
     * 添加新管理员
     */
    @Dict(dicCode = "yon")
    private Boolean addAdmin;
}
