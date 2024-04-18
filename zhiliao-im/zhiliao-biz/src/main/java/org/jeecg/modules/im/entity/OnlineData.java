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
 * 在线数据
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("im_online_data")
public class OnlineData extends BaseModel<OnlineData> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String date;

    private Long tsCreate;
    //用户数
    private Integer user;
    //设备数
    private Integer device;
    //总用户
    private Integer totalUser;
    //总设备
    private Integer totalDevice;
    //服务器
    private Integer serverId;

}
