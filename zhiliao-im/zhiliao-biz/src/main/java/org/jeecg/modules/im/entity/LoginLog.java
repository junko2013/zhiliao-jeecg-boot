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
 *
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@TableName("im_login_log")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_login_log对象", description="登录/注册日志")
public class LoginLog extends BaseModel<LoginLog> {


    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**用户*/
    @Excel(name = "用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
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
    /**关联设备*/
    @Excel(name = "关联设备", width = 15)
    @ApiModelProperty(value = "关联设备")
    private java.lang.Integer deviceId;
    /**设备平台*/
    @Excel(name = "设备平台", width = 15)
    @ApiModelProperty(value = "设备平台")
    private java.lang.String devicePlatform;
    /**设备序号*/
    @Excel(name = "设备序号", width = 15)
    @ApiModelProperty(value = "设备序号")
    private java.lang.String deviceNo;
    /**设备名称*/
    @Excel(name = "设备名称", width = 15)
    @ApiModelProperty(value = "设备名称")
    private java.lang.String deviceName;
    /**设备系统版本*/
    @Excel(name = "设备系统版本", width = 15)
    @ApiModelProperty(value = "设备系统版本")
    private java.lang.String deviceSysVer;
    /**设备客户端版本*/
    @Excel(name = "设备客户端版本", width = 15)
    @ApiModelProperty(value = "设备客户端版本")
    private java.lang.String deviceClientVer;
    /**维度*/
    @Excel(name = "维度", width = 15)
    @ApiModelProperty(value = "维度")
    private java.lang.String latitude;
    /**经度*/
    @Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String longitude;
    /**位置信息*/
    @Excel(name = "位置信息", width = 15)
    @ApiModelProperty(value = "位置信息")
    private java.lang.String locationInfo;
    /**是注册*/
    @Excel(name = "是注册", width = 15)
    @ApiModelProperty(value = "是注册")
    private java.lang.Boolean isRegister;
    /**方式*/
    @Excel(name = "方式", width = 15, dicCode = "login_way")
    @Dict(dicCode = "login_way")
    @ApiModelProperty(value = "方式")
    private java.lang.String way;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

    @TableField(exist = false)
    private User user;

    public enum Way{
        Mobile("手机号"),Account("账号"),SmsCode("短信验证码"),AutoLogin("自动登录"),Username("用户名"),Email("邮箱"),Scan("扫码");
        String info;
        Way(String info){
            this.info = info;
        }
    }

}
