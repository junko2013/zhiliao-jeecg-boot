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
 * 金币账变
 * </p>
 *
 * @author junko
 * @since 2024-02-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_coin_bill")
public class CoinBill extends Model<CoinBill> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    /**
     * 类型
     */
    private String type;

    /**
     * 变动数额
     */
    private Integer amount;

    /**
     * 当前金币
     */
    private Integer balance;

    /**
     * 增加
     */
    private Boolean isIncrease;

    /**
     * 描述
     */
    private String reason;

    private Long tsCreate;

    /**
     * 备注
     */
    private String note;

    public enum Type {
        signIn("签到奖励"),
        signInContinue("连续签到奖励"),
        activity("活动奖励"),
        amountEx("金币兑换"),
        makeup("补签消耗"),
        ;
        String name;
        Type(String name){
            this.name = name;
        }
    }

}
