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
 * @Description: 自定义表情
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_custom_emoji")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_custom_emoji对象", description="自定义表情")
public class CustomEmoji extends BaseModel<CustomEmoji> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**所属用户*/
    @Excel(name = "所属用户", width = 15)
    @ApiModelProperty(value = "所属用户")
    private java.lang.Integer userId;
    /**原图*/
    @Excel(name = "原图", width = 15)
    @ApiModelProperty(value = "原图")
    private java.lang.String origin;
    /**缩略图*/
    @Excel(name = "缩略图", width = 15)
    @ApiModelProperty(value = "缩略图")
    private java.lang.String thumb;
    /**高度*/
    @Excel(name = "高度", width = 15)
    @ApiModelProperty(value = "高度")
    private java.lang.Integer height;
    /**宽度*/
    @Excel(name = "宽度", width = 15)
    @ApiModelProperty(value = "宽度")
    private java.lang.Integer width;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**最后发送时间*/
    @Excel(name = "最后发送时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后发送时间")
    private java.util.Date tsSend;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;
    /**置顶时间*/
    @Excel(name = "置顶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "置顶时间")
    private java.util.Date tsPin;
    /**关联词*/
    @Excel(name = "关联词", width = 15)
    @ApiModelProperty(value = "关联词")
    private java.lang.String keyword;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15)
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

    @TableField(exist = false)
    private User user;
}
