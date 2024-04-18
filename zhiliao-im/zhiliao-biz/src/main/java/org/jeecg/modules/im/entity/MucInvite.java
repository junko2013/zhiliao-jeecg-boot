package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
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
 * 加群邀请
 * </p>
 *
 * @author junko
 * @since 2021-03-06
 */
@Data
@TableName("im_muc_invite")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_invite对象", description="群聊邀请记录")
public class MucInvite extends BaseModel<MucInvite> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**群聊*/
    @Excel(name = "群聊", width = 15, dictTable = "im_muc", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "群聊")
    private java.lang.Integer mucId;
    /**邀请者*/
    @Excel(name = "邀请者", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "邀请者")
    private java.lang.Integer inviter;
    /**受邀者*/
    @Excel(name = "受邀者", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "受邀者")
    private java.lang.Integer invitee;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String comment;
    /**处理者*/
    @Excel(name = "处理者", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "处理者")
    private java.lang.Integer handler;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**状态*/
    @Excel(name = "状态", width = 15, dicCode = "muc_invite_status")
    @Dict(dicCode = "muc_invite_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**有效*/
    @Excel(name = "有效", width = 15)
    @ApiModelProperty(value = "有效")
    private java.lang.Boolean isValid;
    /**需管理员验证*/
    @Excel(name = "需管理员验证", width = 15)
    @ApiModelProperty(value = "需管理员验证")
    private java.lang.Boolean isNeedVerify;
    /**处理时间*/
    @Excel(name = "处理时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "处理时间")
    private java.util.Date tsDeal;
    /**方式*/
    @Excel(name = "方式", width = 15, dicCode = "muc_invite_way")
    @Dict(dicCode = "muc_invite_way")
    @ApiModelProperty(value = "方式")
    private java.lang.Integer way;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

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
