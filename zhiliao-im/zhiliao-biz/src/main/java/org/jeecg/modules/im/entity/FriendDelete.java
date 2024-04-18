package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 解除好友关系
 * </p>
 *
 * @author junko
 * @since 2024-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_friend_delete")
public class FriendDelete extends BaseModel<FriendDelete> {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**
     * 发起人
     */
    @Excel(name = "发起人", width = 15)
    @ApiModelProperty(value = "发起人")
    private java.lang.Integer fromId;
    /**
     * 对方
     */
    @Excel(name = "对方", width = 15)
    @ApiModelProperty(value = "对方")
    private java.lang.Integer toId;
    /**
     * 所属服务器
     */
    @Excel(name = "所属服务器", width = 15)
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
    /**
     * 创建时间
     */
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    //发送人
    @TableField(exist = false)
    private User fromUser;
    //接收人
    @TableField(exist = false)
    private User toUser;

}
