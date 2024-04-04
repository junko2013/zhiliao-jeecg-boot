package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 群公告
 * </p>
 *
 * @author junko
 * @since 2024-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_muc_notice")
public class MucNotice extends BaseModel<MucNotice> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer mucId;

    /**
     * 内容
     */
    private String content;

    /**
     * 使用弹窗展示公告
     */
    @Dict(dicCode = "yon")
    private Boolean alertShow;

    /**
     * 需群成员确认收到
     */
    @Dict(dicCode = "yon")
    private Boolean needConfirm;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 置顶时间
     */
    private Long tsPin;

    /**
     * 引导群成员修改群昵称
     */
    @Dict(dicCode = "yon")
    private Boolean showModifyNickname;

    /**
     * 创建时间
     */
    private Long tsCreate;

    /**
     * 创建人
     */
    private Integer creatorId;

    @TableLogic
    private Integer delFlag;

    @Dict(dicCode = "status")
    private Integer status;

    @TableField(exist = false)
    private Muc muc;
    @TableField(exist = false)
    private MucMember creator;
    @TableField(exist = false)
    private List<MucNoticeRead> readList;

}
