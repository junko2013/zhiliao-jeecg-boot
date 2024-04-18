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
 * 群聊设置
 * </p>
 *
 * @author junko
 * @since 2023-02-09
 */
@Data
@TableName("im_muc_config")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="im_muc_config对象", description="群聊配置")
public class MucConfig extends BaseModel<MucConfig> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**最大群人数*/
    @Excel(name = "最大群人数", width = 15)
    @ApiModelProperty(value = "最大群人数")
    private java.lang.Integer maxMemberCount;
    /**踢人通知 0：不通知，1：全部通知，2：通知群主管理员及本人*/
    @Excel(name = "踢人通知 0：不通知，1：全部通知，2：通知群主管理员及本人", width = 15)
    @ApiModelProperty(value = "踢人通知 0：不通知，1：全部通知，2：通知群主管理员及本人")
    private java.lang.Integer kickNotice;
    /**退出通知 0：不通知 1：全部通知 2：通知群主管理员*/
    @Excel(name = "退出通知 0：不通知 1：全部通知 2：通知群主管理员", width = 15)
    @ApiModelProperty(value = "退出通知 0：不通知 1：全部通知 2：通知群主管理员")
    private java.lang.Integer quitNotice;
    /**成员修改昵称通知 0：不通知 1：全部通知 2：通知群主管理员*/
    @Excel(name = "成员修改昵称通知 0：不通知 1：全部通知 2：通知群主管理员", width = 15)
    @ApiModelProperty(value = "成员修改昵称通知 0：不通知 1：全部通知 2：通知群主管理员")
    private java.lang.Integer modifyNicknameNotice;
    /**撤回通知 0：不通知 1：全部通知 2：通知群主管理员及本人*/
    @Excel(name = "撤回通知 0：不通知 1：全部通知 2：通知群主管理员及本人", width = 15)
    @ApiModelProperty(value = "撤回通知 0：不通知 1：全部通知 2：通知群主管理员及本人")
    private java.lang.Integer revokeNotice;
    /**消息撤回时限*/
    @Excel(name = "消息撤回时限", width = 15)
    @ApiModelProperty(value = "消息撤回时限")
    private java.lang.Integer revokeDuration;
    /**公开*/
    @Excel(name = "公开", width = 15)
    @ApiModelProperty(value = "公开")
    private java.lang.Boolean isPublic;
    /**显示消息已读人数*/
    @Excel(name = "显示消息已读人数", width = 15)
    @ApiModelProperty(value = "显示消息已读人数")
    private java.lang.Boolean isShowRead;
    /**显示群昵称*/
    @Excel(name = "显示群昵称", width = 15)
    @ApiModelProperty(value = "显示群昵称")
    private java.lang.Boolean isShowNickname;
    /**禁言通知 0：不通知 1：全部通知 2：通知群主管理员及本人*/
    @Excel(name = "禁言通知 0：不通知 1：全部通知 2：通知群主管理员及本人", width = 15)
    @ApiModelProperty(value = "禁言通知 0：不通知 1：全部通知 2：通知群主管理员及本人")
    private java.lang.Integer muteNotice;
    /**新成员进群后允许发言*/
    @Excel(name = "新成员进群后允许发言", width = 15)
    @ApiModelProperty(value = "新成员进群后允许发言")
    private java.lang.Boolean isAllowTalkAfterJoin;
    /**入群之前的消息可见*/
    @Excel(name = "入群之前的消息可见", width = 15)
    @ApiModelProperty(value = "入群之前的消息可见")
    private java.lang.Boolean isShowMsgBeforeJoin;
    /**新用户注册后加入该群*/
    @Excel(name = "新用户注册后加入该群", width = 15)
    @ApiModelProperty(value = "新用户注册后加入该群")
    private java.lang.Boolean isDefaultJoin;
    /**移除成员时撤回他的历史发言*/
    @Excel(name = "移除成员时撤回他的历史发言", width = 15)
    @ApiModelProperty(value = "移除成员时撤回他的历史发言")
    private java.lang.Boolean isRevokeAllWhenKicked;
    /**加群确认*/
    @Excel(name = "加群确认", width = 15)
    @ApiModelProperty(value = "加群确认")
    private java.lang.Boolean isJoinVerify;
    /**显示群成员 列表*/
    @Excel(name = "显示群成员 列表", width = 15)
    @ApiModelProperty(value = "显示群成员 列表")
    private java.lang.Boolean isShowMemberList;
    /**群成员修改昵称通知所有人*/
    @Excel(name = "群成员修改昵称通知所有人", width = 15)
    @ApiModelProperty(value = "群成员修改昵称通知所有人")
    private java.lang.Boolean isUpdateNicknameNotify;
    /**欢迎语*/
    @Excel(name = "欢迎语", width = 15)
    @ApiModelProperty(value = "欢迎语")
    private java.lang.String welcomes;
    /**查看成员信息*/
    @Excel(name = "查看成员信息", width = 15)
    @ApiModelProperty(value = "查看成员信息")
    private java.lang.Boolean viewMember;
    /**显示群人员数量*/
    @Excel(name = "显示群人员数量", width = 15)
    @ApiModelProperty(value = "显示群人员数量")
    private java.lang.Boolean showMemberCount;
    /**显示群在线人数*/
    @Excel(name = "显示群在线人数", width = 15)
    @ApiModelProperty(value = "显示群在线人数")
    private java.lang.Boolean showOnlineCount;
    /**邀请新成员*/
    @Excel(name = "邀请新成员", width = 15)
    @ApiModelProperty(value = "邀请新成员")
    private java.lang.Boolean invite;
    /**允许修改群聊昵称*/
    @Excel(name = "允许修改群聊昵称", width = 15)
    @ApiModelProperty(value = "允许修改群聊昵称")
    private java.lang.Boolean modifyNickname;
    /**置顶消息*/
    @Excel(name = "置顶消息", width = 15)
    @ApiModelProperty(value = "置顶消息")
    private java.lang.Boolean pin;
    /**禁止聊天页、历史消息截图*/
    @Excel(name = "禁止聊天页、历史消息截图", width = 15)
    @ApiModelProperty(value = "禁止聊天页、历史消息截图")
    private java.lang.Boolean capture;
    /**消息频率，单位：秒*/
    @Excel(name = "消息频率，单位：秒", width = 15)
    @ApiModelProperty(value = "消息频率，单位：秒")
    private java.lang.Integer msgRate;
    /**消息数*/
    @Excel(name = "消息数", width = 15)
    @ApiModelProperty(value = "消息数")
    private java.lang.Integer msgCount;
    /**发送图片*/
    @Excel(name = "发送图片", width = 15)
    @ApiModelProperty(value = "发送图片")
    private java.lang.Boolean sendImage;
    /**发送视频*/
    @Excel(name = "发送视频", width = 15)
    @ApiModelProperty(value = "发送视频")
    private java.lang.Boolean sendVideo;
    /**发送动图*/
    @Excel(name = "发送动图", width = 15)
    @ApiModelProperty(value = "发送动图")
    private java.lang.Boolean sendGif;
    /**发送贴纸*/
    @Excel(name = "发送贴纸", width = 15)
    @ApiModelProperty(value = "发送贴纸")
    private java.lang.Boolean sendSticker;
    /**发送语音*/
    @Excel(name = "发送语音", width = 15)
    @ApiModelProperty(value = "发送语音")
    private java.lang.Boolean sendVoice;
    /**发送位置*/
    @Excel(name = "发送位置", width = 15)
    @ApiModelProperty(value = "发送位置")
    private java.lang.Boolean sendLocation;
    /**允许发送红包*/
    @Excel(name = "允许发送红包", width = 15)
    @ApiModelProperty(value = "允许发送红包")
    private java.lang.Boolean sendRedPack;
    /**发送链接*/
    @Excel(name = "发送链接", width = 15)
    @ApiModelProperty(value = "发送链接")
    private java.lang.Boolean sendLink;
    /**发送名片*/
    @Excel(name = "发送名片", width = 15)
    @ApiModelProperty(value = "发送名片")
    private java.lang.Boolean sendCard;
    /**发送文件*/
    @Excel(name = "发送文件", width = 15)
    @ApiModelProperty(value = "发送文件")
    private java.lang.Boolean sendFile;
    /**群聊*/
    @Excel(name = "群聊", width = 15, dictTable = "im_muc", dicText = "name", dicCode = "id")
    @Dict(dictTable = "im_muc", dicText = "name", dicCode = "id")
    @ApiModelProperty(value = "群聊")
    private java.lang.Integer mucId;

}
