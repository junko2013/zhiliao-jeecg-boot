package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * <p>
 * 群组用户等级配置
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@TableName("im_muc_member_level_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_member_level_config对象", description="群聊成员等级配置")
public class MucMemberLevelConfig extends BaseModel<MucMemberLevelConfig> {

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**等级*/
    @Excel(name = "等级", width = 15)
    @ApiModelProperty(value = "等级")
    private java.lang.Integer level;
    /**发言字数*/
    @Excel(name = "发言字数", width = 15)
    @ApiModelProperty(value = "发言字数")
    private java.lang.Integer wordCount;
    /**发言次数*/
    @Excel(name = "发言次数", width = 15)
    @ApiModelProperty(value = "发言次数")
    private java.lang.Integer chatCount;
    /**在线天数*/
    @Excel(name = "在线天数", width = 15)
    @ApiModelProperty(value = "在线天数")
    private java.lang.Integer onlineDays;

}
