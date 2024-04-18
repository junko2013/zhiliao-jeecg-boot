package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Accessors;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 群组成员
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Data
@TableName("im_muc_member")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_member对象", description="群聊成员")
public class MucMember extends BaseModel<MucMember> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**用户*/
    @Excel(name = "用户", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "用户")
    private java.lang.Integer userId;
    /**群聊*/
    @Excel(name = "群聊", width = 15, dictTable = "im_muc", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "群聊")
    private java.lang.Integer mucId;
    /**群里的昵称*/
    @Excel(name = "群里的昵称", width = 15)
    @ApiModelProperty(value = "群里的昵称")
    private java.lang.String nickname;
    /**聊天背景图*/
    @Excel(name = "聊天背景图", width = 15)
    @ApiModelProperty(value = "聊天背景图")
    private java.lang.String backImg;
    /**加入时间*/
    @Excel(name = "加入时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "加入时间")
    private java.util.Date tsJoin;
    /**加入方式*/
    @Excel(name = "加入方式", width = 15, dicCode = "muc_member_join_type")
    @Dict(dicCode = "muc_member_join_type")
    @ApiModelProperty(value = "加入方式")
    private java.lang.Integer joinType;
    /**置顶时间*/
    @Excel(name = "置顶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "置顶时间")
    private java.util.Date tsPin;
    /**免打扰*/
    @Excel(name = "免打扰", width = 15)
    @ApiModelProperty(value = "免打扰")
    private java.lang.Boolean isNoDisturb;
    /**未读*/
    @Excel(name = "未读", width = 15)
    @ApiModelProperty(value = "未读")
    private java.lang.Boolean isUnread;
    /**角色*/
    @Excel(name = "角色", width = 15, dicCode = "muc_member_role")
    @Dict(dicCode = "muc_member_role")
    @ApiModelProperty(value = "角色")
    private java.lang.Integer role;
    /**头衔*/
    @Excel(name = "头衔", width = 15)
    @ApiModelProperty(value = "头衔")
    private java.lang.String title;
    /**消息归档*/
    @Excel(name = "消息归档", width = 15)
    @ApiModelProperty(value = "消息归档")
    private java.lang.Boolean isMsgArchive;
    /**隐藏对话*/
    @Excel(name = "隐藏对话", width = 15)
    @ApiModelProperty(value = "隐藏对话")
    private java.lang.Boolean isHide;
    /**阅后即焚*/
    @Excel(name = "阅后即焚", width = 15)
    @ApiModelProperty(value = "阅后即焚")
    private java.lang.Boolean isReadDel;
    /**消息可见时间*/
    @Excel(name = "消息可见时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "消息可见时间")
    private java.util.Date tsMsgVisible;
    /**最后一条阅读的消息时间*/
    @Excel(name = "最后一条阅读的消息时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后一条阅读的消息时间")
    private java.util.Date tsMsgRead;
    /**群聊等级*/
    @Excel(name = "群聊等级", width = 15)
    @ApiModelProperty(value = "群聊等级")
    private java.lang.Integer levelId;
    /**消息未读数*/
    @Excel(name = "消息未读数", width = 15)
    @ApiModelProperty(value = "消息未读数")
    private java.lang.Integer unreadCount;
    /**金币*/
    @Excel(name = "金币", width = 15)
    @ApiModelProperty(value = "金币")
    private java.lang.Integer coin;
    /**更新时间*/
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date tsUpdate;
    /**禁言截止*/
    @Excel(name = "禁言截止", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "禁言截止")
    private java.util.Date tsMute;
    /**禁言类型*/
    @Excel(name = "禁言类型", width = 15)
    @ApiModelProperty(value = "禁言类型")
    private java.lang.Integer muteType;
    /**状态*/
    @Excel(name = "状态", width = 15, dicCode = "muc_member_status")
    @Dict(dicCode = "muc_member_status")
    @ApiModelProperty(value = "状态")
    private java.lang.Integer status;
    /**被谁踢的*/
    @Excel(name = "被谁踢的", width = 15, dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @Dict(dictTable = "im_muc_member", dicText = "nickname", dicCode = "id")
    @ApiModelProperty(value = "被谁踢的")
    private java.lang.Integer kicker;
    /**退出时间*/
    @Excel(name = "退出时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "退出时间")
    private java.util.Date tsQuit;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private MucPermission permission;

    public enum Role{
        Zombie(0,"僵尸号"),
        Member(1,"普通成员"),
        Manager(2,"管理员"),
        Master(3,"群主");
        @Getter
        private final int code;
        @Getter
        private String msg;
        Role(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

    }
    public enum Status{
        normal(0,"正常"),
        kicked(1,"被踢"),
        quit(2,"主动退出"),
        dismiss(3,"群组解散");
        @Getter
        private final int code;
        @Getter
        private String msg;
        Status(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

    }
    public enum JoinType{
        invite(0,"邀请进群"),
        create(1,"建群加入"),
        consoleAdd(2,"系统添加"),
        qrCode(3,"扫码进群"),
        inviteLink(4,"邀请链接"),
        ;
        @Getter
        private int code;
        @Getter
        private String msg;
        JoinType(int code,String msg){
            this.code = code;
            this.msg = msg;
        }

    }
}
