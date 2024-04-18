package org.jeecg.modules.im.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.*;
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
 * @Description: ip黑名单
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@TableName("im_block_ip")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_block_ip对象", description="ip黑名单")
public class BlockIp extends BaseModel<BlockIp> {
    private static final long serialVersionUID = 1L;

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**ip*/
    @Excel(name = "ip", width = 15)
    @ApiModelProperty(value = "ip")
    private java.lang.String ip;
    @TableField(exist = false)
    private String ip1;
    @TableField(exist = false)
    private String ip2;
    /**起始ip转long的值*/
    @Excel(name = "起始ip转long的值", width = 15)
    @ApiModelProperty(value = "起始ip转long的值")
    private java.lang.Long num1;
    /**截止ip转long的值*/
    @Excel(name = "截止ip转long的值", width = 15)
    @ApiModelProperty(value = "截止ip转long的值")
    private java.lang.Long num2;
    /**ip信息*/
    @Excel(name = "ip信息", width = 15)
    @ApiModelProperty(value = "ip信息")
    private java.lang.String info;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String comment;
    /**类型*/
    @Excel(name = "类型", width = 15, dicCode = "block_ip_type")
    @Dict(dicCode = "block_ip_type")
    @ApiModelProperty(value = "类型")
    private java.lang.String type;
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

    public enum Type{
        单个,区间,地址段,国家
    }
}
