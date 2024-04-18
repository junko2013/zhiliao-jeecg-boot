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
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;

/**
 * <p>
 * 注册邀请码
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
@Data
@TableName("im_invite_code")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_invite_code对象", description="注册邀请码")
public class InviteCode extends BaseModel<InviteCode> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**邀请码*/
    @Excel(name = "邀请码", width = 15)
    @ApiModelProperty(value = "邀请码")
    private java.lang.String code;
    /**上次使用*/
    @Excel(name = "上次使用", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "上次使用")
    private java.util.Date tsLast;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**最大使用次数，-1：不限制，0：不使用，>0：具体次数*/
    @Excel(name = "最大使用次数，-1：不限制，0：不使用，>0：具体次数", width = 15)
    @ApiModelProperty(value = "最大使用次数，-1：不限制，0：不使用，>0：具体次数")
    private java.lang.Integer maxTimes;
    /**自动添加的用户账号，多个换行或英文分号隔开*/
    @Excel(name = "自动添加的用户账号，多个换行或英文分号隔开", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "自动添加的用户账号，多个换行或英文分号隔开")
    private java.lang.String userToAdd;
    /**自动加入的群聊账号，多个换行或英文分号隔开*/
    @Excel(name = "自动加入的群聊账号，多个换行或英文分号隔开", width = 15, dictTable = "im_muc", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "自动加入的群聊账号，多个换行或英文分号隔开")
    private java.lang.String mucToJoin;
    /**使用次数*/
    @Excel(name = "使用次数", width = 15)
    @ApiModelProperty(value = "使用次数")
    private java.lang.Integer times;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;
    /**所属用户*/
    @Excel(name = "所属用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "所属用户")
    private java.lang.Integer userId;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

//    0:不启用，1：选填，2：必填

    public enum Type {
        disable(0,"不启用"),optional(1,"选填"),require(2,"必填");

        @Getter
        Integer code;
        @Getter
        String name;


        Type(int code,String name){
            this.code = code;
            this.name = name;
        }
        public static Type getByCode(int code) {
            for (Type value : Type.values()) {
                if (value.code==code) {
                    return value;
                }
            }
            return disable;
        }
    }
}
