package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

import java.util.List;

/**
 * <p>
 * 群聊消息
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(MucMsg.TABLE_NAME)
public class MucMsg extends BaseModel<MucMsg> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc_msg";

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 发送者
     */
    private Integer userId;
    /**
     * 群组id
     */
    private Integer mucId;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 消息id
     */
    private String stanzaId;
    /**
     * 回复的消息id
     */
    private String replyStanzaId;
    /**
     * 内容
     */
    private String content;
    /**
     * 加密
     */
    @Dict(dicCode = "yon")
    private Boolean isEncrypt = false;
    /**
     * 敏感内容
     */
    @Dict(dicCode = "yon")
    private Boolean isSpam = false;
    //置顶时间
    private Long tsPin;

    /**
     * 发送时间戳
     */
    private Long tsSend;
    /**
     * 送达时间
     */
    private Long tsReceived;
    /**
     * 归档时间
     */
    private Long tsArchived;
    /**
     * 删除时间戳
     */
    private Long tsDelete;
    /**
     * 已读数
     */
    private Integer readCount;
    /**
     * 撤回时间
     */
    private Long tsRevoke;
    /**
     * 撤回人
     */
    private Integer revokerId;
    /**
     *0：正常，1：发送人撤回，2：群主或管理员撤回，3：系统撤回
     */
    private Integer revokeType=0;
    /**
     * 阅后即焚
     */
    @Dict(dicCode = "yon")
    private Boolean isReadDel = false;

    private Integer serverId;

    //阅读记录
    @TableField(exist = false)
    private List<MucMsgRead> reads;

    @TableField(exist = false)
    public Boolean isSend;
    @TableField(exist = false)
    public MucMember sender;
    @TableField(exist = false)
    public Muc muc;
    @TableField(exist = false)
    public String deleteUserIds;

}
