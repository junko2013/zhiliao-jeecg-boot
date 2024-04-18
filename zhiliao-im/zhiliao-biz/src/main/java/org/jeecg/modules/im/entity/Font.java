package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableLogic;
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
 * 应用字体
 * </p>
 *
 * @author junko
 * @since 2024-02-12
 */
@Data
@TableName("im_font")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_font对象", description="字体")
public class Font extends BaseModel<Font> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**family*/
    @Excel(name = "family", width = 15)
    @ApiModelProperty(value = "family")
    private java.lang.String family;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**字体下载地址*/
    @Excel(name = "字体下载地址", width = 15)
    @ApiModelProperty(value = "字体下载地址")
    private java.lang.String url;
    /**字体效果预览图*/
    @Excel(name = "字体效果预览图", width = 15)
    @ApiModelProperty(value = "字体效果预览图")
    private java.lang.String preview;
    /**风格*/
    @Excel(name = "风格", width = 15, dicCode = "font_style")
    @Dict(dicCode = "font_style")
    @ApiModelProperty(value = "风格")
    private java.lang.Integer style;
    /**文件大小*/
    @Excel(name = "文件大小", width = 15)
    @ApiModelProperty(value = "文件大小")
    private java.lang.Integer size;
    /**字重*/
    @Excel(name = "字重", width = 15, dicCode = "font_weight")
    @Dict(dicCode = "font_weight")
    @ApiModelProperty(value = "字重")
    private java.lang.Integer weight;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**热门*/
    @Excel(name = "热门", width = 15)
    @ApiModelProperty(value = "热门")
    private java.lang.Integer hot;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**使用次数*/
    @Excel(name = "使用次数", width = 15)
    @ApiModelProperty(value = "使用次数")
    private java.lang.Integer useTimes;
    /**评分*/
    @Excel(name = "评分", width = 15)
    @ApiModelProperty(value = "评分")
    private java.lang.Double rating;

}
