package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * 我收藏的贴纸项
 * </p>
 *
 * @author junko
 * @since 2021-12-12
 */
@Data
@TableName("im_my_sticker_item")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_my_sticker_item对象", description="我的贴纸项")
public class MyStickerItem extends BaseModel<MyStickerItem> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**贴纸项*/
    @Excel(name = "贴纸项", width = 15, dictTable = "im_sticker_item", dicText = "id", dicCode = "id")
    @Dict(dictTable = "im_sticker_item", dicText = "id", dicCode = "id")
    @ApiModelProperty(value = "贴纸项")
    private java.lang.Integer stickerItemId;
    /**贴纸包*/
    @Excel(name = "贴纸包", width = 15, dictTable = "im_sticker", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_sticker", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "贴纸包")
    private java.lang.Integer stickerId;
    /**用户*/
    @Excel(name = "用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "用户")
    private java.lang.Integer userId;
    /**原图*/
    @Excel(name = "原图", width = 15)
    @ApiModelProperty(value = "原图")
    private java.lang.String origin;
    /**缩略图*/
    @Excel(name = "缩略图", width = 15)
    @ApiModelProperty(value = "缩略图")
    private java.lang.String thumb;
    /**lottie*/
    @Excel(name = "lottie", width = 15)
    @ApiModelProperty(value = "lottie")
    private java.lang.String lottie;
    /**添加时间*/
    @Excel(name = "添加时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
    private java.util.Date tsCreate;
}
