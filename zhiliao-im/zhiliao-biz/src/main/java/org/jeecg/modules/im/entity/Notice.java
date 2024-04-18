package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * @Description: 系统公告
 * @Author: jeecg-boot
 * @Date:   2024-04-18
 * @Version: V1.0
 */
@Data
@TableName("im_notice")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_notice对象", description="系统公告")
public class Notice extends BaseModel<Notice> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**公告类型*/
    @Excel(name = "公告类型", width = 15, dicCode = "notice_type")
    @Dict(dicCode = "notice_type")
    @ApiModelProperty(value = "公告类型")
    private java.lang.String type;
    /**显示类型*/
    @Excel(name = "显示类型", width = 15, dicCode = "notice_show_type")
    @Dict(dicCode = "notice_show_type")
    @ApiModelProperty(value = "显示类型")
    private java.lang.Integer showType;
    /**置顶*/
    @Excel(name = "置顶", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "置顶")
    private java.util.Date tsPin;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**浏览次数*/
    @Excel(name = "浏览次数", width = 15)
    @ApiModelProperty(value = "浏览次数")
    private java.lang.Integer viewTimes;
    /**紧急的*/
    @Excel(name = "紧急的", width = 15)
    @ApiModelProperty(value = "紧急的")
    private java.lang.Boolean isUrgent;
    /** 为0表示所有用户都有，不为0表示具体某个用户的*/
    @Excel(name = " 为0表示所有用户都有，不为0表示具体某个用户的", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = " 为0表示所有用户都有，不为0表示具体某个用户的")
    private java.lang.Integer userId;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**用户阅读时间*/
    @Excel(name = "用户阅读时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "用户阅读时间")
    private java.util.Date tsRead;
    /**删除标识*/
    @Excel(name = "删除标识", width = 15)
    @ApiModelProperty(value = "删除标识")
    @TableLogic
    private java.lang.Integer delFlag;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
}
