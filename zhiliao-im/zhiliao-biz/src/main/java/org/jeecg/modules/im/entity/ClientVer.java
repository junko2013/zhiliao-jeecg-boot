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
 * @Description: 客户端版本
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_client_ver")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_client_ver对象", description="客户端版本")
public class ClientVer implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**所属平台*/
    @Excel(name = "所属平台", width = 15, dicCode = "platform")
    @Dict(dicCode = "platform")
    @ApiModelProperty(value = "所属平台")
    private java.lang.Integer platform;
    /**版本号*/
    @Excel(name = "版本号", width = 15)
    @ApiModelProperty(value = "版本号")
    private java.lang.String version;
    /**下载链接*/
    @Excel(name = "下载链接", width = 15)
    @ApiModelProperty(value = "下载链接")
    private java.lang.String downloadUrl;
    /**强制更新*/
    @Excel(name = "强制更新", width = 15)
    @ApiModelProperty(value = "强制更新")
    private java.lang.Boolean isForce;
    /**描述信息*/
    @Excel(name = "描述信息", width = 15)
    @ApiModelProperty(value = "描述信息")
    private java.lang.String info;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**最低兼容版本号*/
    @Excel(name = "最低兼容版本号", width = 15)
    @ApiModelProperty(value = "最低兼容版本号")
    private java.lang.String minCompatibleVer;
    /**发布时间*/
    @Excel(name = "发布时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "发布时间")
    private java.util.Date tsCreate;
    /**测试版*/
    @Excel(name = "测试版", width = 15)
    @ApiModelProperty(value = "测试版")
    private java.lang.Boolean isBeta;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;
}

