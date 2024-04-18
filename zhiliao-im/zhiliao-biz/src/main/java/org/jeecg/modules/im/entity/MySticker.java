package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
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
 * 我添加的贴纸
 * </p>
 *
 * @author junko
 * @since 2021-03-24
 */
@Data
@TableName("im_my_sticker")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_my_sticker对象", description="我的贴纸包")
public class MySticker extends BaseModel<MySticker> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**用户*/
    @Excel(name = "用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "用户")
    private java.lang.Integer userId;
    /**贴纸包*/
    @Excel(name = "贴纸包", width = 15, dictTable = "im_sticker", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_sticker", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "贴纸包")
    private java.lang.Integer stickerId;
    /**添加时间*/
    @Excel(name = "添加时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "添加时间")
    private java.util.Date tsCreate;
    @TableField(exist = false)
    private Sticker sticker;
}
