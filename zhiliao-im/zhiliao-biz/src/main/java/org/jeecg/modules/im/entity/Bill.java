package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

import java.math.BigDecimal;

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
 * @Description: 账变
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_bill")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_bill对象", description="账变")
public class Bill implements Serializable {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**用户*/
    @Excel(name = "用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "用户")
    private java.lang.Integer userId;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
    /**金额*/
    @Excel(name = "金额", width = 15)
    @ApiModelProperty(value = "金额")
    private java.lang.Integer amount;
    /**账变前*/
    @Excel(name = "账变前", width = 15)
    @ApiModelProperty(value = "账变前")
    private java.lang.Integer balanceBefore;
    /**账变后*/
    @Excel(name = "账变后", width = 15)
    @ApiModelProperty(value = "账变后")
    private java.lang.Integer balanceAfter;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**增加*/
    @Excel(name = "增加", width = 15)
    @ApiModelProperty(value = "增加")
    private java.lang.Boolean isIncrease;
    /**标题*/
    @Excel(name = "标题", width = 15)
    @ApiModelProperty(value = "标题")
    private java.lang.String title;
    /**描述*/
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String body;
    /**业务类型*/
    @Excel(name = "业务类型", width = 15, dicCode = "bill_type")
    @Dict(dicCode = "bill_type")
    @ApiModelProperty(value = "业务类型")
    private java.lang.Integer type;
    /**支付方式*/
    @Excel(name = "支付方式", width = 15, dicCode = "bill_pay_type")
    @Dict(dicCode = "bill_pay_type")
    @ApiModelProperty(value = "支付方式")
    private java.lang.Integer payType;

    public enum Type {
        Withdraw("提现"),Recharge("充值"),RedPack("红包"),TransferOut("转账");
        String name;
        Type(String name){
            this.name = name;
        }
    }
    public enum PayType {
        Manual("后台充值"),Online("在线支付"),Apply("提交申请");
        String name;
        PayType(String name){
            this.name = name;
        }
    }
}
