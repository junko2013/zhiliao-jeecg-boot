package org.jeecg.modules.im.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 手机通讯录联系人
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_contact")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_contact对象", description="手机通讯录联系人")
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**所属用户*/
    @Excel(name = "所属用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "所属用户")
    private java.lang.Integer userId;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**手机号*/
    @Excel(name = "手机号", width = 15)
    @ApiModelProperty(value = "手机号")
    private java.lang.String mobile;
    /**首写字母*/
    @Excel(name = "首写字母", width = 15)
    @ApiModelProperty(value = "首写字母")
    private java.lang.String capital;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

    /*
     * 未注册：邀请注册
     * 已注册：发送添加请求
     */
    @TableField(exist = false)
    private User user;
    /*
    与当前用户的好友
     */
    @TableField(exist = false)
    private Friend friend;

}
