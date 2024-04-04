package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;

import java.io.Serializable;

/**
 * 部署系统
 */
@Data
@TableName("sys_system")
public class System implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    
    /**
     * 名称
     */
    private String name;
    //编号
    private String no;
    //所在区域
    private String zone;
    //接口地址
    private String apiUrl;
    //启用https
    @Dict(dicCode = "yon")
    private Boolean enableHttps;

    /**
     * 创建时间
     */
    private Long tsCreate;

    /**
     * 状态 1正常 0冻结
     */
    @Dict(dicCode = "status")
    private Integer status;
    //类型
    @Dict(dicCode = "system_type")
    private Integer type;

    @TableLogic
    private Integer delFlag;

}
