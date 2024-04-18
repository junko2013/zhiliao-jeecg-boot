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
 * @Description: 群聊
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Data
@TableName("im_muc")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc对象", description="群聊")
public class Muc extends BaseModel<Muc> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**创建人*/
    @Excel(name = "创建人", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "创建人")
    private java.lang.Integer userId;
    /**管理员*/
    @Excel(name = "管理员", width = 15, dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @Dict(dictTable = "sys_user", dicText = "realname", dicCode = "username")
    @ApiModelProperty(value = "管理员")
    private java.lang.String adminId;
    /**群主*/
    @Excel(name = "群主", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "群主")
    private java.lang.Integer masterId;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**描述*/
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String info;
    /**头像原图*/
    @Excel(name = "头像原图", width = 15)
    @ApiModelProperty(value = "头像原图")
    private java.lang.String avatar;
    /**头像缩略图*/
    @Excel(name = "头像缩略图", width = 15)
    @ApiModelProperty(value = "头像缩略图")
    private java.lang.String smallAvatar;
    /**二维码*/
    @Excel(name = "二维码", width = 15)
    @ApiModelProperty(value = "二维码")
    private java.lang.String qrCode;
    /**账号*/
    @Excel(name = "账号", width = 15)
    @ApiModelProperty(value = "账号")
    private java.lang.String account;
    /**主题*/
    @Excel(name = "主题", width = 15)
    @ApiModelProperty(value = "主题")
    private java.lang.String subject;
    /**群成员数量*/
    @Excel(name = "群成员数量", width = 15)
    @ApiModelProperty(value = "群成员数量")
    private java.lang.Integer memberCount;
    /**虚假成员数*/
    @Excel(name = "虚假成员数", width = 15)
    @ApiModelProperty(value = "虚假成员数")
    private java.lang.Integer fakeMember;
    /**虚假在线数*/
    @Excel(name = "虚假在线数", width = 15)
    @ApiModelProperty(value = "虚假在线数")
    private java.lang.Integer fakeOnline;
    /**封禁截止*/
    @Excel(name = "封禁截止", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "封禁截止")
    private java.util.Date tsLocked;
    /**禁言截止*/
    @Excel(name = "禁言截止", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "禁言截止")
    private java.util.Date tsMute;
    /**最后发言*/
    @Excel(name = "最后发言", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后发言")
    private java.util.Date tsLastTalk;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**删除时间*/
    @Excel(name = "删除时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "删除时间")
    private java.util.Date tsDelete;
    /**更新时间*/
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date tsUpdate;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

    /**
     * 群组配置
     */
    @TableField(exist = false)
    private MucConfig mucConfig;
    //创建人
    @TableField(exist = false)
    private User user;
    //当前用户成员
    @TableField(exist = false)
    private MucMember member;
    //群主
    @TableField(exist = false)
    private MucMember master;
    //当前用户在群里的角色
    @TableField(exist = false)
    private Integer role;
    //群成员
    @TableField(exist = false)
    private List<MucMember> members;
    //群组默认权限
    @TableField(exist = false)
    private MucPermission mucPermission;
    //进群邀请
    @TableField(exist = false)
    private List<MucInvite> invites;

}
