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
 * 系统异常日志
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@TableName("im_exception_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_exception_log对象", description="系统异常日志")
public class ExceptionLog extends BaseModel<ExceptionLog> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**ip*/
    @Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private java.lang.String ip;
    /**ip信息*/
    @Excel(name = "ip信息", width = 15)
    @ApiModelProperty(value = "ip信息")
    private java.lang.String ipInfo;
    /**用户*/
    @Excel(name = "用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "用户")
    private java.lang.Integer userId;
    /**访问资源*/
    @Excel(name = "访问资源", width = 15)
    @ApiModelProperty(value = "访问资源")
    private java.lang.String uri;
    /**方法*/
    @Excel(name = "方法", width = 15)
    @ApiModelProperty(value = "方法")
    private java.lang.String method;
    /**请求参数*/
    @Excel(name = "请求参数", width = 15)
    @ApiModelProperty(value = "请求参数")
    private java.lang.String reqParam;
    /**异常名称*/
    @Excel(name = "异常名称", width = 15)
    @ApiModelProperty(value = "异常名称")
    private java.lang.String excName;
    /**异常信息*/
    @Excel(name = "异常信息", width = 15)
    @ApiModelProperty(value = "异常信息")
    private java.lang.String excMessage;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;

    @TableField(exist = false)
    private User user;
}
