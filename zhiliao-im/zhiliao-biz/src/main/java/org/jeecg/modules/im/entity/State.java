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
 * 用户状态
 * </p>
 *
 * @author junko
 * @since 2023-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_state")
public class State extends Model<State> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 图标
     */
    private String icon;

    /**
     * 分类
     */
    private Integer ctgId;

    /**
     * 排序
     */
    private Integer orderNo;

    private Long tsCreate;

    @TableField(exist = false)
    private StateCtg ctg;

}
