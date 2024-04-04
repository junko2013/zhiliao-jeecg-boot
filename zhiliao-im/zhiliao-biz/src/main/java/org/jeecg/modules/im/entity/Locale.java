package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 语言包
 * </p>
 *
 * @author junko
 * @since 2023-12-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
@TableName("im_locale")
public class Locale extends Model<Locale> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 名称：如法语
     */
    private String name;

    /**
     * 语言代码
     */
    private String languageCode;
    /**
     * 国家代码
     */
    private String countryCode;
    /**
     * 唯一标示符
     */
    private String identity;

    /**
     * json内容
     */
    private String content;

    /**
     * 状态
     */
    @Dict(dicCode = "status")
    private Integer status;

    private Long tsCreate;

    /**
     * 用户id，为空则为后台发布
     */
    private Integer userId;

    /**
     * 0
     */
    private Integer orderNo;

    @TableLogic
    private Integer delFlag;
}
