package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 群组管理员权限
 * </p>
 *
 * @author junko
 * @since 2023-03-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_muc_permission")
public class MucPermission extends BaseModel<MucPermission> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 群组id
     */
    private Integer mucId;
    /**
     * 0表示群默认，其他代表管理员权限
     */
    private Integer managerId;

    /**
     * 编辑群资料
     */
    @Dict(dicCode = "yon")
    private Boolean modifyInfo;

    /**
     * 编辑公告
     */
    @Dict(dicCode = "yon")
    private Boolean modifyNotice;

    /**
     * 置顶消息
     */
    @Dict(dicCode = "yon")
    private Boolean msgPin;

    /**
     * 添加成员
     */
    @Dict(dicCode = "yon")
    private Boolean addMember;

    /**
     * 移除成员
     */
    @Dict(dicCode = "yon")
    private Boolean delMember;

    /**
     * 禁言成员
     */
    @Dict(dicCode = "yon")
    private Boolean muteMember;

    /**
     * 添加管理员
     */
    @Dict(dicCode = "yon")
    private Boolean addManager;

    /**
     * 撤销管理员
     */
    @Dict(dicCode = "yon")
    private Boolean revokeManager;

    /**
     * 提示入群验证
     */
    @Dict(dicCode = "yon")
    private Boolean isValidationTip;
    //匿名管理员
    @Dict(dicCode = "yon")
    private Boolean isAnonymous;
}
