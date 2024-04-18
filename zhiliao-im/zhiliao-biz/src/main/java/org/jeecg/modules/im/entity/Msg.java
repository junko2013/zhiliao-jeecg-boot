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

/**
 * <p>
 * 聊天消息
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Data
@TableName("im_msg")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_msg对象", description="单聊消息")
public class Msg extends BaseModel<Msg> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Long id;
    /**源消息*/
    @Excel(name = "源消息", width = 15)
    @ApiModelProperty(value = "源消息")
    private java.lang.Long originId;
    /**发送人*/
    @Excel(name = "发送人", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "发送人")
    private java.lang.Integer userId;
    /**接收方*/
    @Excel(name = "接收方", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "接收方")
    private java.lang.Integer toUserId;
    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**消息ID*/
    @Excel(name = "消息ID", width = 15)
    @ApiModelProperty(value = "消息ID")
    private java.lang.String stanzaId;
    /**回复的消息id*/
    @Excel(name = "回复的消息id", width = 15)
    @ApiModelProperty(value = "回复的消息id")
    private java.lang.String replyStanzaId;
    /**类型*/
    @Excel(name = "类型", width = 15, dicCode = "msg_type")
    @Dict(dicCode = "msg_type")
    @ApiModelProperty(value = "类型")
    private java.lang.Integer type;
    /**加密*/
    @Excel(name = "加密", width = 15)
    @ApiModelProperty(value = "加密")
    private java.lang.Boolean isEncrypt;
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
    /**置顶时间*/
    @Excel(name = "置顶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "置顶时间")
    private java.util.Date tsPin;
    /**逻辑删除时间*/
    @Excel(name = "逻辑删除时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "逻辑删除时间")
    private java.util.Date tsDelete;
    /**已读时间*/
    @Excel(name = "已读时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "已读时间")
    private java.util.Date tsRead;
    /**撤回时间*/
    @Excel(name = "撤回时间", width = 20, format = "yyyy-MM-dd HH:mm:ss.SSS")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "撤回时间")
    private java.util.Date tsRevoke;
    /**撤回类型*/
    @Excel(name = "撤回类型", width = 15, dicCode = "msg_revoke_type")
    @Dict(dicCode = "msg_revoke_type")
    @ApiModelProperty(value = "撤回类型")
    private java.lang.Integer revokeType;
    /**是阅后即焚*/
    @Excel(name = "是阅后即焚", width = 15)
    @ApiModelProperty(value = "是阅后即焚")
    private java.lang.Boolean isReadDel;
    /**是垃圾消息*/
    @Excel(name = "是垃圾消息", width = 15)
    @ApiModelProperty(value = "是垃圾消息")
    private java.lang.Boolean isSpam;
    /**是发送的*/
    @Excel(name = "是发送的", width = 15)
    @ApiModelProperty(value = "是发送的")
    private java.lang.Boolean isSend;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

    /**
     * 过滤
     */
    @TableField(exist = false)
    private Integer filter;

    @TableField(exist = false)
    public User fromUser;
    @TableField(exist = false)
    public User toUser;

}
