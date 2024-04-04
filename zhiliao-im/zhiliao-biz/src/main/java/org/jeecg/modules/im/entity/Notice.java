package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 公告
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Notice.TABLE_NAME)
public class Notice extends BaseModel<Notice> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_notice";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 置顶时间
     */
    private Long tsPin;

    @Dict(dicCode = "status")
    private Integer status;
    @TableLogic
    private Integer delFlag;

    /**
     * 浏览量
     */
    private Integer viewTimes;

    private Long tsCreate;

    private Integer serverId;

    /**
     * 为0表示所有用户都有，不为0表示具体某个用户的
     */
    private Integer userId;
    //类型
    @Dict(dicCode = "notice_type")
    private Integer type;
    //显示类型
    @Dict(dicCode = "notice_show_type")
    private Integer showType;
    //紧急的
    @Dict(dicCode = "yon")
    private Boolean isUrgent;
    //用户阅读时间
    private Long tsRead;
}
