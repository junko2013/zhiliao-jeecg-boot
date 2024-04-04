package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 广告
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(Adver.TABLE_NAME)
public class Adver extends BaseModel<Adver> {

    private static final long serialVersionUID = 1L;
    public static final String TABLE_NAME = "im_adver";

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String title;

    private String cover;

    @Dict(dicCode = "adver_location_type")
    private Integer type;

    @Dict(dicCode = "adver_jump_type")
    private Integer jumpType;

    private String jumpUrl;

    private Integer timeout;

    @Dict(dicCode = "status")
    private Integer status;

    private Long tsCreate;

    @TableLogic
    private Integer delFlag;

    //服务器
    private Integer serverId;
}
