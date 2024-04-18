package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;

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
 * 群消息已读记录
 * </p>
 *
 * @author junko
 * @since 2023-12-03
 */
@Data
@TableName("im_muc_msg_read")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_msg_read对象", description="群消息已读记录")
public class MucMsgRead extends BaseModel<MucMsgRead> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**消息ID*/
    @Excel(name = "消息ID", width = 15, dictTable = "im_muc_msg", dicText = "id", dicCode = "id")
    @Dict(dictTable = "im_muc_msg", dicText = "id", dicCode = "id")
    @ApiModelProperty(value = "消息ID")
    private java.lang.Long msgId;
    /**用户*/
    @Excel(name = "用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "用户")
    private java.lang.Integer userId;
    /**阅读时间*/
    @Excel(name = "阅读时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "阅读时间")
    private java.util.Date tsRead;

}
