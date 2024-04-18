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
 * @Description: 用户操作日志
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_user_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_user_log对象", description="用户操作日志")
public class UserLog extends BaseModel<UserLog> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**用户*/
    @Excel(name = "用户", width = 15)
    @ApiModelProperty(value = "用户")
    private java.lang.Integer userId;
    /**ip*/
    @Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private java.lang.String ip;
    /**ip信息*/
    @Excel(name = "ip信息", width = 15)
    @ApiModelProperty(value = "ip信息")
    private java.lang.String ipInfo;
    /**设备*/
    @Excel(name = "设备", width = 15)
    @ApiModelProperty(value = "设备")
    private java.lang.Integer deviceId;
    /**设备平台*/
    @Excel(name = "设备平台", width = 15)
    @ApiModelProperty(value = "设备平台")
    private java.lang.String devicePlatform;
    /**设备编号*/
    @Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String deviceNo;
    /**设备名称*/
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String deviceName;
    /**设备系统版本*/
    @Excel(name = "设备系统版本", width = 15)
    @ApiModelProperty(value = "设备系统版本")
    private java.lang.String deviceSysVer;
    /**设备是物理机*/
    @Excel(name = "设备是物理机", width = 15)
    @ApiModelProperty(value = "设备是物理机")
    private java.lang.Boolean deviceIsPhysic;
    /**设备客户端版本*/
    @Excel(name = "设备客户端版本", width = 15)
    @ApiModelProperty(value = "设备客户端版本")
    private java.lang.String deviceClientVer;
    /**类型*/
    @Excel(name = "类型", width = 15)
    @ApiModelProperty(value = "类型")
    private java.lang.String type;
    /**请求方法*/
    @Excel(name = "请求方法", width = 15)
    @ApiModelProperty(value = "请求方法")
    private java.lang.String method;
    /**请求参数*/
    @Excel(name = "请求参数", width = 15)
    @ApiModelProperty(value = "请求参数")
    private java.lang.String reqParam;
    /**相应数据*/
    @Excel(name = "相应数据", width = 15)
    @ApiModelProperty(value = "相应数据")
    private java.lang.String resData;
    /**请求链接*/
    @Excel(name = "请求链接", width = 15)
    @ApiModelProperty(value = "请求链接")
    private java.lang.String uri;
    /**详情*/
    @Excel(name = "详情", width = 15)
    @ApiModelProperty(value = "详情")
    private java.lang.String detail;
    /**模块*/
    @Excel(name = "模块", width = 15)
    @ApiModelProperty(value = "模块")
    private java.lang.String module;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15)
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
}
