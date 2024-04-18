package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;

/**
 * <p>
 * 群组用户等级
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@TableName("im_muc_member_level")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_member_level对象", description="群聊成员等级")
public class MucMemberLevel extends BaseModel<MucMemberLevel> {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**分类*/
    @Excel(name = "分类", width = 15, dictTable = "im_muc_member_level_ctg", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc_member_level_ctg", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "分类")
    private java.lang.Integer ctgId;
    /**名称*/
    @Excel(name = "名称", width = 15)
    @ApiModelProperty(value = "名称")
    private java.lang.String name;
    /**等级*/
    @Excel(name = "等级", width = 15)
    @ApiModelProperty(value = "等级")
    private java.lang.Integer level;

}
