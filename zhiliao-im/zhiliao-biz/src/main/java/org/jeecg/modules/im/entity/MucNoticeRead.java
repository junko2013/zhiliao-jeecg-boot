package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 群公告已读
 * </p>
 *
 * @author junko
 * @since 2024-02-05
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_muc_notice_read")
public class MucNoticeRead extends BaseModel<MucNoticeRead> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 公告id
     */
    private Integer noticeId;

    /**
     * 阅读人
     */
    private Integer memberId;

    /**
     * 已读时间
     */
    private Long tsRead;

    /**
     * 确认时间
     */
    private Long tsConfirm;

    @TableField(exist = false)
    private MucMember member;
    @TableField(exist = false)
    private MucNotice notice;

}
