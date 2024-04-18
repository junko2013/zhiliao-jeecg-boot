package org.jeecg.modules.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
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
 * 好友
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Data
@TableName("im_friend")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_friend对象", description="好友")
public class Friend extends BaseModel<Friend> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**我方*/
    @Excel(name = "我方", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "我方")
    private java.lang.Integer userId;
    /**对方*/
    @Excel(name = "对方", width = 15, dictTable = "im_user", dicText = "account", dicCode = "id")
    @Dict(dictTable = "im_user", dicText = "account", dicCode = "id")
    @ApiModelProperty(value = "对方")
    private java.lang.Integer toUserId;
    /**所属服务器*/
    @Excel(name = "所属服务器", width = 15, dictTable = "im_server", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_server", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
    /**星标*/
    @Excel(name = "星标", width = 15)
    @ApiModelProperty(value = "星标")
    private java.lang.Boolean isStar;
    /**备注*/
    @Excel(name = "备注", width = 15)
    @ApiModelProperty(value = "备注")
    private java.lang.String remark;
    /**聊天背景*/
    @Excel(name = "聊天背景", width = 15)
    @ApiModelProperty(value = "聊天背景")
    private java.lang.String backImg;
    /**联系方式*/
    @Excel(name = "联系方式", width = 15)
    @ApiModelProperty(value = "联系方式")
    private java.lang.String phone;
    /**更多信息*/
    @Excel(name = "更多信息", width = 15)
    @ApiModelProperty(value = "更多信息")
    private java.lang.String info;
    /**标签ID*/
    @Excel(name = "标签ID", width = 15)
    @ApiModelProperty(value = "标签ID")
    private java.lang.String tagIds;
    /**标签列表*/
    @Excel(name = "标签列表", width = 15)
    @ApiModelProperty(value = "标签列表")
    private java.lang.String tagNames;
    /**消息归档*/
    @Excel(name = "消息归档", width = 15)
    @ApiModelProperty(value = "消息归档")
    private java.lang.Boolean isMsgArchive;
    /**隐藏对话*/
    @Excel(name = "隐藏对话", width = 15)
    @ApiModelProperty(value = "隐藏对话")
    private java.lang.Boolean isHide;
    /**消息免打扰*/
    @Excel(name = "消息免打扰", width = 15)
    @ApiModelProperty(value = "消息免打扰")
    private java.lang.Boolean isNoDisturb;
    /**阅后即焚*/
    @Excel(name = "阅后即焚", width = 15)
    @ApiModelProperty(value = "阅后即焚")
    private java.lang.Boolean isReadDel;
    /**创建时间*/
    @Excel(name = "创建时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间")
    private java.util.Date tsCreate;
    /**置顶时间*/
    @Excel(name = "置顶时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "置顶时间")
    private java.util.Date tsPin;
    /**成为好友时间*/
    @Excel(name = "成为好友时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "成为好友时间")
    private java.util.Date tsFriend;
    /**更新时间*/
    @Excel(name = "更新时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间")
    private java.util.Date tsUpdate;
    /**最后对话时间*/
    @Excel(name = "最后对话时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后对话时间")
    private java.util.Date tsLastTalk;
    /**指定时间后的消息可见*/
    @Excel(name = "指定时间后的消息可见", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "指定时间后的消息可见")
    private java.util.Date tsMsgVisible;
    /**最后一条阅读的消息时间*/
    @Excel(name = "最后一条阅读的消息时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "最后一条阅读的消息时间")
    private java.util.Date tsMsgRead;
    /**添加方式*/
    @Excel(name = "添加方式", width = 15, dicCode = "friend_add_type")
    @Dict(dicCode = "friend_add_type")
    @ApiModelProperty(value = "添加方式")
    private java.lang.Integer addType;
    /**好友状态*/
    @Excel(name = "好友状态", width = 15, dicCode = "friend_status")
    @Dict(dicCode = "friend_status")
    @ApiModelProperty(value = "好友状态")
    private java.lang.Integer status;
    /**权限*/
    @Excel(name = "权限", width = 15, dicCode = "friend_privilege")
    @Dict(dicCode = "friend_privilege")
    @ApiModelProperty(value = "权限")
    private java.lang.Integer privilege;
    /**标记未读*/
    @Excel(name = "标记未读", width = 15)
    @ApiModelProperty(value = "标记未读")
    private java.lang.Boolean isUnread;
    /**拉黑时间*/
    @Excel(name = "拉黑时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "拉黑时间")
    private java.util.Date tsBlack;
    /**被拉黑时间*/
    @Excel(name = "被拉黑时间", width = 20, format = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(timezone = "UTC",pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "被拉黑时间")
    private java.util.Date tsBeenBlack;
    /**翻译*/
    @Excel(name = "翻译", width = 15)
    @ApiModelProperty(value = "翻译")
    private java.lang.String tr;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private User toUser;

    //状态
    public enum Status{
        Follow(0,"关注"),
        Friend(1,"好友"),
        Stranger(2,"陌生人"),
        Delete(5,"双向删除"),
        Fans(6,"粉丝"),
        Ask(7,"等待验证");
        @Getter
        int code;
        @Getter
        String name;
        Status(int code,String name) {
            this.code = code;
            this.name = name;
        }

    }
    //添加方式
    public enum AddType{
        Account(0,"账号"),
        Mobile(1,"手机号"),
        Nickname(2,"昵称"),
        Username(3,"用户名"),
        Card(4,"名片"),
        Muc(5,"群聊"),
        Scan(6,"扫一扫"),
        System(7,"系统添加"),
        Not(8,"未添加");
        @Getter
        int code;
        @Getter
        String type;
        AddType(int code,String type) {
            this.code = code;
            this.type = type;
        }

    }
}