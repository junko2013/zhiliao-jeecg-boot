package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.List;

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
 * 群公告
 * </p>
 *
 * @author junko
 * @since 2024-02-05
 */
@Data
@TableName("im_muc_notice")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_notice对象", description="群公告")
public class MucNotice extends BaseModel<MucNotice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**群聊*/
    @Excel(name = "群聊", width = 15, dictTable = "im_muc", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "群聊")
    private java.lang.Integer mucId;
    /**内容*/
    @Excel(name = "内容", width = 15)
    @ApiModelProperty(value = "内容")
    private java.lang.String content;
    /**使用弹窗展示公告*/
    @Excel(name = "使用弹窗展示公告", width = 15)
    @ApiModelProperty(value = "使用弹窗展示公告")
    private java.lang.Boolean alertShow;
    /**需群成员确认收到*/
    @Excel(name = "需群成员确认收到", width = 15)
    @ApiModelProperty(value = "需群成员确认收到")
    private java.lang.Boolean needConfirm;
    /**封面图*/
    @Excel(name = "封面图", width = 15)
    @ApiModelProperty(value = "封面图")
    private java.lang.String cover;
    /**置顶时间*/
    @Excel(name = "置顶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "置顶时间")
    private java.util.Date tsPin;
    /**引导群成员修改群昵称*/
    @Excel(name = "引导群成员修改群昵称", width = 15)
    @ApiModelProperty(value = "引导群成员修改群昵称")
    private java.lang.Boolean showModifyNickname;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**创建人*/
    @Excel(name = "创建人", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "创建人")
    private java.lang.Integer creatorId;
    /**逻辑删除*/
    @Excel(name = "逻辑删除", width = 15)
    @ApiModelProperty(value = "逻辑删除")
    @TableLogic
    private java.lang.Integer delFlag;
    /**启用*/
    @Excel(name = "启用", width = 15)
    @ApiModelProperty(value = "启用")
    private java.lang.Boolean enable;

    @TableField(exist = false)
    private Muc muc;
    @TableField(exist = false)
    private MucMember creator;
    @TableField(exist = false)
    private List<MucNoticeRead> readList;

}
