package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Locale;

/**
 * <p>
 * 用户设备
 * </p>
 *
 * @author junko
 * @since 2021-02-06
 */
@Data
@TableName("im_device")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_device对象", description="设备")
public class Device extends BaseModel<Device> {


    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**所属用户*/
    @Excel(name = "所属用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "所属用户")
    private java.lang.Integer userId;
    /**平台*/
    @Excel(name = "平台", width = 15, dicCode = "platform")
    @Dict(dicCode = "platform")
    @ApiModelProperty(value = "平台")
    private java.lang.String platform;
    /**唯一编号*/
    @Excel(name = "唯一编号", width = 15)
    @ApiModelProperty(value = "唯一编号")
    private java.lang.String no;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**详情信息*/
    @Excel(name = "详情信息", width = 15)
    @ApiModelProperty(value = "详情信息")
    private java.lang.String detail;
    /**客户端操作系统版本*/
    @Excel(name = "客户端操作系统版本", width = 15)
    @ApiModelProperty(value = "客户端操作系统版本")
    private java.lang.String sysVer;
    /**客户端应用版本*/
    @Excel(name = "客户端应用版本", width = 15)
    @ApiModelProperty(value = "客户端应用版本")
    private java.lang.String clientVer;
    /**令牌*/
    @Excel(name = "令牌", width = 15)
    @ApiModelProperty(value = "令牌")
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private java.lang.String token;
    /**极光推送注册id*/
    @Excel(name = "极光推送注册id", width = 15)
    @ApiModelProperty(value = "极光推送注册id")
    private java.lang.String jpushId;
    /**在线*/
    @Excel(name = "在线", width = 15)
    @ApiModelProperty(value = "在线")
    private java.lang.Boolean isOnline;
    /**真机*/
    @Excel(name = "真机", width = 15)
    @ApiModelProperty(value = "真机")
    private java.lang.Boolean isPhysic;
    /**最后上线*/
    @Excel(name = "最后上线", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后上线")
    private java.util.Date tsOnline;
    /**最后离线*/
    @Excel(name = "最后离线", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后离线")
    private java.util.Date tsOffline;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**禁用时间*/
    @Excel(name = "禁用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "禁用时间")
    private java.util.Date tsDisabled;
    /**最后心跳*/
    @Excel(name = "最后心跳", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后心跳")
    private java.util.Date tsPing;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

    @TableField(exist = false)
    private Integer loginTimes;
    @TableField(exist = false)
    private LoginLog loginLog;


    public enum Platform {
        android, ios, windows,macOS,linux,web,wap,unknown;

        public static Platform getByName(String name) {
            for (Platform value : Platform.values()) {
                if (value.toString().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return Platform.unknown;
        }
    }
}
