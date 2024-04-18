package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * <p>
 * 群组管理员权限
 * </p>
 *
 * @author junko
 * @since 2023-03-27
 */
@Data
@TableName("im_muc_permission")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_permission对象", description="群管理员权限")
public class MucPermission extends BaseModel<MucPermission> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**群聊*/
    @Excel(name = "群聊", width = 15, dictTable = "im_muc", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "群聊")
    private java.lang.Integer mucId;
    /**为0表示默认，否则代表具体管理员*/
    @Excel(name = "为0表示默认，否则代表具体管理员", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "为0表示默认，否则代表具体管理员")
    private java.lang.Integer managerId;
    /**编辑群资料*/
    @Excel(name = "编辑群资料", width = 15)
    @ApiModelProperty(value = "编辑群资料")
    private java.lang.Boolean modifyInfo;
    /**编辑公告*/
    @Excel(name = "编辑公告", width = 15)
    @ApiModelProperty(value = "编辑公告")
    private java.lang.Boolean modifyNotice;
    /**置顶消息*/
    @Excel(name = "置顶消息", width = 15)
    @ApiModelProperty(value = "置顶消息")
    private java.lang.Boolean msgPin;
    /**添加成员*/
    @Excel(name = "添加成员", width = 15)
    @ApiModelProperty(value = "添加成员")
    private java.lang.Boolean addMember;
    /**移除成员*/
    @Excel(name = "移除成员", width = 15)
    @ApiModelProperty(value = "移除成员")
    private java.lang.Boolean delMember;
    /**禁言成员*/
    @Excel(name = "禁言成员", width = 15)
    @ApiModelProperty(value = "禁言成员")
    private java.lang.Boolean muteMember;
    /**添加管理员*/
    @Excel(name = "添加管理员", width = 15)
    @ApiModelProperty(value = "添加管理员")
    private java.lang.Boolean addManager;
    /**撤销管理员*/
    @Excel(name = "撤销管理员", width = 15)
    @ApiModelProperty(value = "撤销管理员")
    private java.lang.Boolean revokeManager;
    /**提示入群验证*/
    @Excel(name = "提示入群验证", width = 15)
    @ApiModelProperty(value = "提示入群验证")
    private java.lang.Boolean isValidationTip;
    /**匿名管理员*/
    @Excel(name = "匿名管理员", width = 15)
    @ApiModelProperty(value = "匿名管理员")
    private java.lang.Boolean isAnonymous;
}
