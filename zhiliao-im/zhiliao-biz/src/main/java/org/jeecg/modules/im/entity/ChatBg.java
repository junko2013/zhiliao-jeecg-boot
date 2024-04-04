package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 聊天背景
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_chat_bg")
public class ChatBg extends Model<ChatBg> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    //缩略图
    private String thumb;
    //原图
    private String origin;
    //排序
    private Integer orderNo;
    //应用次数
    private Integer useTimes;

    @TableLogic
    private Integer delFlag;

    @Dict(dicCode = "status")
    private Integer status;

    private Long tsCreate;

    private Integer serverId;
}
