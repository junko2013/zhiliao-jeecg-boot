package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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

/**
 * <p>
 * gif收藏
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Data
@TableName("im_gif")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_gif对象", description="im_gif")
public class Gif extends BaseModel<Gif> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**排序*/
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer orderNo;
    /**原图*/
    @Excel(name = "原图", width = 15)
    @ApiModelProperty(value = "原图")
    private java.lang.String origin;
    /**缩略图*/
    @Excel(name = "缩略图", width = 15)
    @ApiModelProperty(value = "缩略图")
    private java.lang.String thumb;
    /**md5*/
    @Excel(name = "md5", width = 15)
    @ApiModelProperty(value = "md5")
    private java.lang.String md5;
    /**关键词*/
    @Excel(name = "关键词", width = 15)
    @ApiModelProperty(value = "关键词")
    private java.lang.String keyword;
    /**emoji*/
    @Excel(name = "emoji", width = 15)
    @ApiModelProperty(value = "emoji")
    private java.lang.String emoji;
    /**编码*/
    @Excel(name = "编码", width = 15)
    @ApiModelProperty(value = "编码")
    private java.lang.String emojiCode;
    /**图集*/
    @Excel(name = "图集", width = 15, dictTable = "im_gif_album", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_gif_album", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "图集")
    private java.lang.Integer albumId;
    /**添加次数*/
    @Excel(name = "添加次数", width = 15)
    @ApiModelProperty(value = "添加次数")
    private java.lang.Integer addTimes;
    /**发送次数*/
    @Excel(name = "发送次数", width = 15)
    @ApiModelProperty(value = "发送次数")
    private java.lang.Integer sendTimes;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**宽度*/
    @Excel(name = "宽度", width = 15)
    @ApiModelProperty(value = "宽度")
    private java.lang.Integer width;
    /**高度*/
    @Excel(name = "高度", width = 15)
    @ApiModelProperty(value = "高度")
    private java.lang.Integer height;
}
