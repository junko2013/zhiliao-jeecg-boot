package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 加群邀请
 * </p>
 *
 * @author junko
 * @since 2021-03-06
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MucInvite.TABLE_NAME)
public class MucInvite extends BaseModel<MucInvite> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc_invite";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 群聊id
     */
    private Integer mucId;
    /**
     * 邀请者 成员id
     */
    private Integer inviter;
    /**
     * 受邀者 用户id
     */
    private Integer invitee;

    /**
     * 备注
     */
    private String comment;

    /**
     * 处理者 成员id
     */
    private Integer handler;

    private Long tsCreate;

    /**
     * 状态，0：待处理，1：验证通过，2：拒绝
     */
    @Dict(dicCode = "muc_invite_status")
    private Integer status;
    /**
     * 方式，0：手动邀请，1：邀请链接
     */
    @Dict(dicCode = "muc_invite_way")
    private Integer way;

    /**
     * 有效的
     */
    @Dict(dicCode = "yon")
    private Boolean isValid;
    /**
     * 需要验证
     */
    @Dict(dicCode = "yon")
    private Boolean isNeedVerify;

    /**
     * 处理时间
     */
    private Long tsDeal;

    @TableLogic
    private Integer delFlag;

    private Integer serverId;

    @TableField(exist = false)
    private MucMember inviterMember;
    @TableField(exist = false)
    private User inviteeUser;
    @TableField(exist = false)
    private MucMember handlerMember;

    public enum Status{
        Waiting(0),Accept(1),Reject(2);
        private int code;
        Status(int code){
            this.code = code;
        }

        public int getCode() {
            return code;
        }
    }

}
