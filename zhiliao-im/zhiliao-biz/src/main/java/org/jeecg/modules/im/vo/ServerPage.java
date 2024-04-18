package org.jeecg.modules.im.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelCollection;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Description: 租户服务器
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Data
@ApiModel(value="im_serverPage对象", description="租户服务器")
public class ServerPage {

	/**id*/
	@ApiModelProperty(value = "id")
    private Integer id;
	/**服务器编号*/
	@Excel(name = "服务器编号", width = 15)
	@ApiModelProperty(value = "服务器编号")
    private String no;
	/**名称*/
	@Excel(name = "名称", width = 15)
	@ApiModelProperty(value = "名称")
    private String name;
	/**创建时间*/
	@Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "创建时间")
    private Date tsCreate;
	/**启用*/
	@Excel(name = "启用", width = 15)
	@ApiModelProperty(value = "启用")
    private String enable;
	/**接口令牌*/
	@Excel(name = "接口令牌", width = 15)
	@ApiModelProperty(value = "接口令牌")
    private String accessToken;
	/**ws链接*/
	@Excel(name = "ws链接", width = 15)
	@ApiModelProperty(value = "ws链接")
    private String wsUrl;
	/**停用时间*/
	@Excel(name = "停用时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@ApiModelProperty(value = "停用时间")
    private Date tsStop;
	/**删除标记*/
	@Excel(name = "删除标记", width = 15)
	@ApiModelProperty(value = "删除标记")
    private Integer delFlag;
	/**所属租户*/
	@Excel(name = "所属租户", width = 15, dictTable = "sys_tenant", dicText = "name", dicCode = "id")
    @Dict(dictTable = "sys_tenant", dicText = "name", dicCode = "id")
	@ApiModelProperty(value = "所属租户")
    private Integer tenantId;

	@ExcelCollection(name="服务器配置")
	@ApiModelProperty(value = "服务器配置")
	private List<ServerConfig> serverConfigList;

}
