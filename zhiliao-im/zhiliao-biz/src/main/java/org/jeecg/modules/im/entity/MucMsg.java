package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

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
@TableName("im_muc_msg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_msg对象", description="群聊消息")
public class MucMsg extends BaseModel<MucMsg> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_muc_msg";

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
    /**发送人*/
    @Excel(name = "发送人", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "发送人")
    private java.lang.Integer userId;
    /**群聊*/
    @Excel(name = "群聊", width = 15, dictTable = "im_muc", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "群聊")
    private java.lang.Integer mucId;
    /**类型*/
    @Excel(name = "类型", width = 15, dicCode = "msg_type")
    @Dict(dicCode = "msg_type")
    @ApiModelProperty(value = "类型")
    private java.lang.Integer type;
    /**消息ID*/
    @Excel(name = "消息ID", width = 15)
    @ApiModelProperty(value = "消息ID")
    private java.lang.String stanzaId;
    /**回复的消息id*/
    @Excel(name = "回复的消息id", width = 15)
    @ApiModelProperty(value = "回复的消息id")
    private java.lang.String replyStanzaId;
    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**加密*/
    @Excel(name = "加密", width = 15)
    @ApiModelProperty(value = "加密")
    private java.lang.Boolean isEncrypt;
    /**垃圾信息*/
    @Excel(name = "垃圾信息", width = 15)
    @ApiModelProperty(value = "垃圾信息")
    private java.lang.Boolean isSpam;
    /**置顶时间*/
    @Excel(name = "置顶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "置顶时间")
    private java.util.Date tsPin;
    /**发送时间*/
    @Excel(name = "发送时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发送时间")
    private java.util.Date tsSend;
    /**送达时间*/
    @Excel(name = "送达时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "送达时间")
    private java.util.Date tsReceived;
    /**归档时间*/
    @Excel(name = "归档时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "归档时间")
    private java.util.Date tsArchived;
    /**逻辑删除时间*/
    @Excel(name = "逻辑删除时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "逻辑删除时间")
    private java.util.Date tsDelete;
    /**阅读数*/
    @Excel(name = "阅读数", width = 15)
    @ApiModelProperty(value = "阅读数")
    private java.lang.Integer readCount;
    /**撤回时间*/
    @Excel(name = "撤回时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "撤回时间")
    private java.util.Date tsRevoke;
    /**撤回人*/
    @Excel(name = "撤回人", width = 15)
    @ApiModelProperty(value = "撤回人")
    private java.lang.Integer revokerId;
    /**撤回方式*/
    @Excel(name = "撤回方式", width = 15, dicCode = "muc_msg_revoke_type")
    @Dict(dicCode = "muc_msg_revoke_type")
    @ApiModelProperty(value = "撤回方式")
    private java.lang.Integer revokeType;
    /**阅后即焚*/
    @Excel(name = "阅后即焚", width = 15)
    @ApiModelProperty(value = "阅后即焚")
    private java.lang.Boolean isReadDel;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

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
