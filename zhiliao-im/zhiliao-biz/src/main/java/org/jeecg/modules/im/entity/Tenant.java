package org.jeecg.modules.im.entity;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 * <p>
 * 多租户信息表
 * </p>
 *
 * @author junko
 * @since 2024-03-24
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("sys_tenant")
public class Tenant extends Model<Tenant> {

    private static final long serialVersionUID = 1L;

    /**
     * 租户编码
     */
    private Integer id;

    /**
     * 租户名称
     */
    private String name;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 开始时间
     */
    private LocalDateTime beginDate;

    /**
     * 结束时间
     */
    private LocalDateTime endDate;

    /**
     * 状态 1正常 0冻结
     */
    private Integer status;

    /**
     * 所属行业
     */
    private String trade;

    /**
     * 公司规模
     */
    private String companySize;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 公司logo
     */
    private String companyLogo;

    /**
     * 门牌号
     */
    private String houseNumber;

    /**
     * 工作地点
     */
    private String workPlace;

    /**
     * 二级域名
     */
    private String secondaryDomain;

    /**
     * 登录背景图片
     */
    private String loginBkgdImg;

    /**
     * 职级
     */
    private String position;

    /**
     * 部门
     */
    private String department;

    /**
     * 删除状态(0-正常,1-已删除)
     */
    private Integer delFlag;

    /**
     * 更新人
     */
    private String updateBy;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 允许申请管理员 1允许 0不允许
     */
    private Integer applyStatus;

}
