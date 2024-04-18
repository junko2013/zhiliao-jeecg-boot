package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

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
 * 群组邀请链接
 * </p>
 *
 * @author junko
 * @since 2023-07-22
 */
@Data
@TableName("im_muc_invite_link")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_invite_link对象", description="群聊邀请链接")
public class MucInviteLink extends BaseModel<MucInviteLink> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**群组*/
    @Excel(name = "群组", width = 15, dictTable = "im_muc", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "群组")
    private java.lang.Integer mucId;
    /**邀请码*/
    @Excel(name = "邀请码", width = 15)
    @ApiModelProperty(value = "邀请码")
    private java.lang.String code;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**需要审核*/
    @Excel(name = "需要审核", width = 15)
    @ApiModelProperty(value = "需要审核")
    private java.lang.Boolean needVerify;
    /**有效时长*/
    @Excel(name = "有效时长", width = 15)
    @ApiModelProperty(value = "有效时长")
    private java.lang.Integer duration;
    /**次数限制*/
    @Excel(name = "次数限制", width = 15)
    @ApiModelProperty(value = "次数限制")
    private java.lang.Integer times;
    /**使用次数*/
    @Excel(name = "使用次数", width = 15)
    @ApiModelProperty(value = "使用次数")
    private java.lang.Integer usedTimes;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**失效时间*/
    @Excel(name = "失效时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "失效时间")
    private java.util.Date tsValid;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**创建人*/
    @Excel(name = "创建人", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "创建人")
    private java.lang.Integer creatorId;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;

    @TableField(exist = false)
    private Muc muc;
    @TableField(exist = false)
    private MucMember creator;
}
