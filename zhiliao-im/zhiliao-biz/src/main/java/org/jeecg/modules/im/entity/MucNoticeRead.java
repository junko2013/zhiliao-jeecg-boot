package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
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
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 群公告已读
 * </p>
 *
 * @author junko
 * @since 2024-02-05
 */
@Data
@TableName("im_muc_notice_read")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_notice_read对象", description="群公告已读")
public class MucNoticeRead extends BaseModel<MucNoticeRead> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**公告*/
    @Excel(name = "公告", width = 15, dictTable = "im_muc_notice", dicText = "id", dicCode = "id")
    @Dict(dictTable = "im_muc_notice", dicText = "id", dicCode = "id")
    @ApiModelProperty(value = "公告")
    private java.lang.Integer noticeId;
    /**阅读人*/
    @Excel(name = "阅读人", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "阅读人")
    private java.lang.Integer memberId;
    /**已读时间*/
    @Excel(name = "已读时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "已读时间")
    private java.util.Date tsRead;
    /**确认时间*/
    @Excel(name = "确认时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "确认时间")
    private java.util.Date tsConfirm;

    @TableField(exist = false)
    private MucMember member;
    @TableField(exist = false)
    private MucNotice notice;

}
