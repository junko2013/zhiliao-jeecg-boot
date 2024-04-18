package org.jeecg.modules.im.entity;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.jeecg.modules.im.entity.User;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 音视频通话
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_call")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_call对象", description="音视频通话")
public class Call implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**发起人*/
    @Excel(name = "发起人", width = 15)
    @ApiModelProperty(value = "发起人")
    private java.lang.Integer fromId;
    /**接收方*/
    @Excel(name = "接收方", width = 15)
    @ApiModelProperty(value = "接收方")
    private java.lang.Integer toId;
    /**通话频道*/
    @Excel(name = "通话频道", width = 15)
    @ApiModelProperty(value = "通话频道")
    private java.lang.String channelId;
    /**备注，如失败原因*/
    @Excel(name = "备注，如失败原因", width = 15)
    @ApiModelProperty(value = "备注，如失败原因")
    private java.lang.String comment;
    /**状态*/
    @Excel(name = "状态", width = 15, dicCode = "call_status")
    @Dict(dicCode = "call_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**接通时间*/
    @Excel(name = "接通时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "接通时间")
    private java.util.Date tsAccept;
    /**取消时间*/
    @Excel(name = "取消时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "取消时间")
    private java.util.Date tsCancel;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**超时时间*/
    @Excel(name = "超时时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "超时时间")
    private java.util.Date tsTimeout;
    /**拒接时间*/
    @Excel(name = "拒接时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "拒接时间")
    private java.util.Date tsReject;
    /**中断时间*/
    @Excel(name = "中断时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "中断时间")
    private java.util.Date tsSuspend;
    /**结束时间*/
    @Excel(name = "结束时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "结束时间")
    private java.util.Date tsEnd;
    /**接通时间*/
    @Excel(name = "接通时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "接通时间")
    private java.util.Date tsConnected;
    /**失败时间*/
    @Excel(name = "失败时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "失败时间")
    private java.util.Date tsNotConnected;
    /**通话时长*/
    @Excel(name = "通话时长", width = 15)
    @ApiModelProperty(value = "通话时长")
    private java.lang.Integer seconds;
    /**是视频通话*/
    @Excel(name = "是视频通话", width = 15)
    @ApiModelProperty(value = "是视频通话")
    private java.lang.Boolean isVideo;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;

    @TableField(exist = false)
    private User fromUser;
    @TableField(exist = false)
    private User toUser;

    public enum Status{
        Waiting(0,"等待接听"),
        Cancel(1,"取消"),
        Timeout(2,"超时未接"),
        Reject(3,"拒绝"),
        Suspend(4,"中断"),//非正常结束
        End(5,"结束"),//正常结束
        Accept(6,"接受"),
        Connected(7,"接通"),
        ConnectFail(8,"接受未接通");
        @Getter
        int code;
        @Getter
        String name;
        Status(int code,String name){
            this.code = code;
            this.name = name;
        }
    }
}
