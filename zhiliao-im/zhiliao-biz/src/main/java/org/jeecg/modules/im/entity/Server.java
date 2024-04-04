package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jeecg.common.aspect.annotation.Dict;

/**
 * <p>
 * 服务器
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_server")
public class Server extends BaseModel<Server> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 服务器编号
     */
    private String no;

    /**
     * 名称
     */
    private String name;


    private Long tsCreate;

    /**
     * 1：正常 2：禁用
     */
    private Integer status;

    /**
     * 接口令牌
     */
    private String accessToken;

    //websocket链接
    private String wsUrl;

    /**
     * 停用时间
     */
    private Long tsStop;

    /**
     * 租户id
     */
    @Dict(dictTable = "sys_tenant", dicCode = "id", dicText = "name")
    private Integer tenantId;

    @TableLogic
    private Integer delFlag;

    @TableField(exist = false)
    private Tenant tenant;

}
