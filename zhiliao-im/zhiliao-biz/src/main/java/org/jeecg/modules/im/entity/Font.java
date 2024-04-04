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
 * 应用字体
 * </p>
 *
 * @author junko
 * @since 2024-02-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_font")
public class Font extends BaseModel<Font> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String family;

    private String name;

    /**
     * 字体下载地址
     */
    private String url;
    //大小
    private Integer size;

    /**
     * 字体效果预览图
     */
    private String preview;

    /**
     * 风格，0：normal，1：italic
     */
    @Dict(dicCode = "font_style")
    private Integer style;

    /**
     * 字重，0：normal，1：bold
     */
    @Dict(dicCode = "font_weight")
    private Integer weight;

    /**
     * 启用状态
     */
    @Dict(dicCode = "status")
    private Integer status;

    @Dict(dicCode = "hot")
    private Integer hot;
    @TableLogic
    private Integer delFlag;

    private Long tsCreate;

    /**
     * 使用次数
     */
    private Integer useTimes;

    /**
     * 评分
     */
    private Integer rating;

}
