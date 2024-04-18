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
 * 意见反馈
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Data
@TableName("im_feedback")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_feedback对象", description="问题反馈")
public class Feedback extends BaseModel<Feedback> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**类型*/
    @Excel(name = "类型", width = 15, dictTable = "im_feedback_type", dicText = "title", dicCode = "id")
    @Dict(dictTable = "im_feedback_type", dicText = "title", dicCode = "id")
    @ApiModelProperty(value = "类型")
    private java.lang.Integer typeId;
    /**所属用户*/
    @Excel(name = "所属用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "所属用户")
    private java.lang.Integer userId;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
    /**提交时间*/
    @Excel(name = "提交时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "提交时间")
    private java.util.Date tsCreate;
    /**图片*/
    @Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String imgUrl;
    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**回复*/
    @Excel(name = "回复", width = 15)
    @ApiModelProperty(value = "回复")
    private java.lang.String reply;
    /**联系方式*/
    @Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String contact;
    /**处理时间*/
    @Excel(name = "处理时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "处理时间")
    private java.util.Date tsDeal;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;

    @TableField(exist = false)
    private Boolean isDeal;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private FeedbackType type;

}
