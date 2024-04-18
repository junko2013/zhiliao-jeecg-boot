package org.jeecg.modules.im.entity;
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
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 金币账变
 * </p>
 *
 * @author junko
 * @since 2024-02-15
 */
@Data
@TableName("im_coin_bill")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_coin_bill对象", description="金币账变")
public class CoinBill extends BaseModel<CoinBill> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**所属用户*/
    @Excel(name = "所属用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "所属用户")
    private java.lang.Integer userId;
    /**类型*/
    @Excel(name = "类型", width = 15, dicCode = "coin_bill_type")
    @Dict(dicCode = "coin_bill_type")
    @ApiModelProperty(value = "类型")
    private java.lang.Integer type;
    /**变动数额*/
    @Excel(name = "变动数额", width = 15)
    @ApiModelProperty(value = "变动数额")
    private java.lang.Integer amount;
    /**入账前金币*/
    @Excel(name = "入账前金币", width = 15)
    @ApiModelProperty(value = "入账前金币")
    private java.lang.Integer balanceBefore;
    /**入账后金币*/
    @Excel(name = "入账后金币", width = 15)
    @ApiModelProperty(value = "入账后金币")
    private java.lang.Integer balanceAfter;
    /**增加*/
    @Excel(name = "增加", width = 15)
    @ApiModelProperty(value = "增加")
    private java.lang.Boolean isIncrease;
    /**描述*/
    @Excel(name = "描述", width = 15)
    @ApiModelProperty(value = "描述")
    private java.lang.String reason;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String note;

    public enum Type {
        signIn(1,"签到奖励"),
        signInContinue(2,"连续签到奖励"),
        activity(3,"活动奖励"),
        amountEx(4,"金币兑换"),
        makeup(5,"补签消耗"),
        ;
        @Getter
        int code;
        String name;
        Type(int code,String name){
            this.name = name;
        }
    }

}
