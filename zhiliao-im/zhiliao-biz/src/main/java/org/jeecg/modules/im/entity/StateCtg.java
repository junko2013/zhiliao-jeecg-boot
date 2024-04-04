package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 用户状态分类
 * </p>
 *
 * @author junko
 * @since 2023-07-30
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_state_ctg")
public class StateCtg extends Model<StateCtg> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String name;

    private Integer orderNo;

    private Long tsCreate;

}
