package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * <p>
 * 语言包
 * </p>
 *
 * @author junko
 * @since 2023-12-18
 */
@Data
@TableName("im_locale")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_locale对象", description="语言包")
public class Locale extends Model<Locale> {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**语言代码*/
    @Excel(name = "语言代码", width = 15)
    @ApiModelProperty(value = "语言代码")
    private java.lang.String languageCode;
    /**国家代码*/
    @Excel(name = "国家代码", width = 15)
    @ApiModelProperty(value = "国家代码")
    private java.lang.String countryCode;
    /**唯一标识符*/
    @Excel(name = "唯一标识符", width = 15)
    @ApiModelProperty(value = "唯一标识符")
    private java.lang.String identity;
    /**json内容*/
    @Excel(name = "json内容", width = 15)
    @ApiModelProperty(value = "json内容")
    private java.lang.String content;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**删除标记*/
    @Excel(name = "删除标记", width = 15)
    @ApiModelProperty(value = "删除标记")
    @TableLogic
    private java.lang.Integer delFlag;
    /**创建时间*/
    @Excel(name = "创建时间", width = 15)
    @ApiModelProperty(value = "创建时间")
    private java.lang.Integer tsCreate;
    /**排序*/
    @Excel(name = "排序", width = 15)
    @ApiModelProperty(value = "排序")
    private java.lang.Integer orderNo;
    /**用户，为空则为系统发布*/
    @Excel(name = "用户，为空则为系统发布", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "用户，为空则为系统发布")
    private java.lang.Integer userId;
}
