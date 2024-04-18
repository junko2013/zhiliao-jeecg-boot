package org.jeecg.modules.im.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * @Description: 广告图
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_adver")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_adver对象", description="广告图")
public class Adver implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**图片*/
    @Excel(name = "图片", width = 15)
    @ApiModelProperty(value = "图片")
    private java.lang.String cover;
    /**类型*/
    @Excel(name = "类型", width = 15, dicCode = "adver_location_type")
    @Dict(dicCode = "adver_location_type")
    @ApiModelProperty(value = "类型")
    private java.lang.Integer type;
    /**跳转类型*/
    @Excel(name = "跳转类型", width = 15, dicCode = "adver_jump_type")
    @Dict(dicCode = "adver_jump_type")
    @ApiModelProperty(value = "跳转类型")
    private java.lang.Integer jumpType;
    /**跳转链接*/
    @Excel(name = "跳转链接", width = 15)
    @ApiModelProperty(value = "跳转链接")
    private java.lang.String jumpUrl;
    /**倒计时*/
    @Excel(name = "倒计时", width = 15)
    @ApiModelProperty(value = "倒计时")
    private java.lang.Integer timeout;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**逻辑删除标识*/
    @Excel(name = "逻辑删除标识", width = 15)
    @ApiModelProperty(value = "逻辑删除标识")
    @TableLogic
    private java.lang.Integer delFlag;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
}
