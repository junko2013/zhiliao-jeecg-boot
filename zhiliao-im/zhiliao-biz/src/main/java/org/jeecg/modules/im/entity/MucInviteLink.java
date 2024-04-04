package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 群组邀请链接
 * </p>
 *
 * @author junko
 * @since 2023-07-22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_muc_invite_link")
public class MucInviteLink extends BaseModel<MucInviteLink> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String code;

    private Integer mucId;

    /**
     * 名称
     */
    private String name;

    /**
     * 需要审核
     */
    @Dict(dicCode = "yon")
    private Boolean needVerify;

    /**
     * 有效时长
     */
    private Integer duration;

    /**
     * 次数限制
     */
    private Integer times;
    //使用次数
    private Integer usedTimes;

    /**
     * 创建时间
     */
    private Long tsCreate;

    /**
     * 失效时间
     */
    private Long tsValid;

    /**
     * 创建人
     */
    private Integer creatorId;

    private Integer serverId;

    @Dict(dicCode = "status")
    private Integer status;
    @TableLogic
    private Integer delFlag;

    @TableField(exist = false)
    private Muc muc;
    @TableField(exist = false)
    private MucMember creator;
}
