package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

/**
 * <p>
 * 签到
 * </p>
 *
 * @author junko
 * @since 2021-02-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(SignIn.TABLE_NAME)
public class SignIn extends BaseModel<SignIn> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_sign_in";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;
    //签到日期
    private String date;
    //奖励
    private Integer reward;
    //连续签到奖励
    private Integer rewardContinue;
    //连续签到天数
    private Integer days;
    //是补签
    private Boolean isMakeup;
    //补签花费
    private Integer makeupCost;

    @TableField(exist = false)
    private User user;

    private Long tsCreate;

    private Integer serverId;


}
