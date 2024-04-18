package org.jeecg.modules.im.entity;

import java.io.Serializable;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.UnsupportedEncodingException;

/**
 * @Description: 服务器配置
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@ApiModel(value="im_server_config对象", description="服务器配置")
@Data
@TableName("im_server_config")
public class ServerConfig extends BaseModel<ServerConfig> {

    /**id*/
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "id")
    private java.lang.Integer id;
    /**允许普通用户创建群聊*/
    @Excel(name = "允许普通用户创建群聊", width = 15)
    @ApiModelProperty(value = "允许普通用户创建群聊")
    private java.lang.Boolean allowCommonUserCreateMuc;
    /**允许普通用户创建频道*/
    @Excel(name = "允许普通用户创建频道", width = 15)
    @ApiModelProperty(value = "允许普通用户创建频道")
    private java.lang.Boolean allowCommonUserCreateChannel;
    /**群聊名称唯一*/
    @Excel(name = "群聊名称唯一", width = 15)
    @ApiModelProperty(value = "群聊名称唯一")
    private java.lang.Boolean mucNameUnique;
    /**频道名称唯一*/
    @Excel(name = "频道名称唯一", width = 15)
    @ApiModelProperty(value = "频道名称唯一")
    private java.lang.Boolean channelNameUnique;
    /**创建群聊上限*/
    @Excel(name = "创建群聊上限", width = 15)
    @ApiModelProperty(value = "创建群聊上限")
    private java.lang.Integer maxMucCreate;
    /**加入群聊上限*/
    @Excel(name = "加入群聊上限", width = 15)
    @ApiModelProperty(value = "加入群聊上限")
    private java.lang.Integer maxMucJoin;
    /**管理群聊上限*/
    @Excel(name = "管理群聊上限", width = 15)
    @ApiModelProperty(value = "管理群聊上限")
    private java.lang.Integer maxMucManage;
    /**好友上限*/
    @Excel(name = "好友上限", width = 15)
    @ApiModelProperty(value = "好友上限")
    private java.lang.Integer maxFriend;
    /**每日添加好友上限*/
    @Excel(name = "每日添加好友上限", width = 15)
    @ApiModelProperty(value = "每日添加好友上限")
    private java.lang.Integer maxFriendPerDay;
    /**用户默认密码，用于非注册用户或后台重置密码*/
    @Excel(name = "用户默认密码，用于非注册用户或后台重置密码", width = 15)
    @ApiModelProperty(value = "用户默认密码，用于非注册用户或后台重置密码")
    private java.lang.String defaultPassword;
    /**邀请码类型：0:不启用，1：选填，2：必填*/
    @Excel(name = "邀请码类型：0:不启用，1：选填，2：必填", width = 15, dicCode = "invite_code_type")
    @ApiModelProperty(value = "邀请码类型：0:不启用，1：选填，2：必填")
    private java.lang.Integer inviteCodeType;
    /**自动添加邀请人为好友*/
    @Excel(name = "自动添加邀请人为好友", width = 15)
    @ApiModelProperty(value = "自动添加邀请人为好友")
    private java.lang.Boolean autoAddInviter;
    /**启用普通用户的邀请码*/
    @Excel(name = "启用普通用户的邀请码", width = 15)
    @ApiModelProperty(value = "启用普通用户的邀请码")
    private java.lang.Boolean enableCommonUserInvite;
    /**昵称查找用户*/
    @Excel(name = "昵称查找用户", width = 15)
    @ApiModelProperty(value = "昵称查找用户")
    private java.lang.Boolean nicknameSearch;
    /**昵称精确查找用户*/
    @Excel(name = "昵称精确查找用户", width = 15)
    @ApiModelProperty(value = "昵称精确查找用户")
    private java.lang.Boolean nicknameSearchExact;
    /**手机号查找用户*/
    @Excel(name = "手机号查找用户", width = 15)
    @ApiModelProperty(value = "手机号查找用户")
    private java.lang.Boolean mobileSearch;
    /**账号查找用户*/
    @Excel(name = "账号查找用户", width = 15)
    @ApiModelProperty(value = "账号查找用户")
    private java.lang.Boolean accountSearch;
    /**用户名查找用户*/
    @Excel(name = "用户名查找用户", width = 15)
    @ApiModelProperty(value = "用户名查找用户")
    private java.lang.Boolean usernameSearch;
    /**通过昵称添加*/
    @Excel(name = "通过昵称添加", width = 15)
    @ApiModelProperty(value = "通过昵称添加")
    private java.lang.Boolean nicknameAdd;
    /**通过手机号添加*/
    @Excel(name = "通过手机号添加", width = 15)
    @ApiModelProperty(value = "通过手机号添加")
    private java.lang.Boolean mobileAdd;
    /**通过账号添加*/
    @Excel(name = "通过账号添加", width = 15)
    @ApiModelProperty(value = "通过账号添加")
    private java.lang.Boolean accountAdd;
    /**通过用户名添加*/
    @Excel(name = "通过用户名添加", width = 15)
    @ApiModelProperty(value = "通过用户名添加")
    private java.lang.Boolean usernameAdd;
    /**通过名片添加*/
    @Excel(name = "通过名片添加", width = 15)
    @ApiModelProperty(value = "通过名片添加")
    private java.lang.Boolean cardAdd;
    /**通过群聊添加*/
    @Excel(name = "通过群聊添加", width = 15)
    @ApiModelProperty(value = "通过群聊添加")
    private java.lang.Boolean mucAdd;
    /**通过扫码添加*/
    @Excel(name = "通过扫码添加", width = 15)
    @ApiModelProperty(value = "通过扫码添加")
    private java.lang.Boolean scanAdd;
    /**启用钱包*/
    @Excel(name = "启用钱包", width = 15)
    @ApiModelProperty(value = "启用钱包")
    private java.lang.Boolean enableWallet;
    /**提现费率，%*/
    @Excel(name = "提现费率，%", width = 15)
    @ApiModelProperty(value = "提现费率，%")
    private java.lang.Integer withdrawFee;
    /**最低提现金额*/
    @Excel(name = "最低提现金额", width = 15)
    @ApiModelProperty(value = "最低提现金额")
    private java.lang.Integer minWithdrawAmount;
    /**最高提现金额*/
    @Excel(name = "最高提现金额", width = 15)
    @ApiModelProperty(value = "最高提现金额")
    private java.lang.Integer maxWithdrawAmount;
    /**最低充值金额*/
    @Excel(name = "最低充值金额", width = 15)
    @ApiModelProperty(value = "最低充值金额")
    private java.lang.Integer minRechargeAmount;
    /**最高充值金额*/
    @Excel(name = "最高充值金额", width = 15)
    @ApiModelProperty(value = "最高充值金额")
    private java.lang.Integer maxRechargeAmount;
    /**启用签到*/
    @Excel(name = "启用签到", width = 15)
    @ApiModelProperty(value = "启用签到")
    private java.lang.Boolean enableSignIn;
    /**单位：金币。连续签到获得额外奖励，则格式如：1,3,5,7,10,15...，表示第一天1，第2天3，第3天5，以此类推，若当前连续签到天数超过设置的个数，则取最右值。固定奖励请输入固定值，如：5，表示签到奖励5*/
    @Excel(name = "单位：金币。连续签到获得额外奖励，则格式如：1,3,5,7,10,15...，表示第一天1，第2天3，第3天5，以此类推，若当前连续签到天数超过设置的个数，则取最右值。固定奖励请输入固定值，如：5，表示签到奖励5", width = 15)
    @ApiModelProperty(value = "单位：金币。连续签到获得额外奖励，则格式如：1,3,5,7,10,15...，表示第一天1，第2天3，第3天5，以此类推，若当前连续签到天数超过设置的个数，则取最右值。固定奖励请输入固定值，如：5，表示签到奖励5")
    private java.lang.String signInRewardContinue;
    /**单位：金币。每次签到获得的奖励*/
    @Excel(name = "单位：金币。每次签到获得的奖励", width = 15)
    @ApiModelProperty(value = "单位：金币。每次签到获得的奖励")
    private java.lang.Integer signInReward;
    /**达到后重置天数，中断将重置天数。0为不开启连续签到*/
    @Excel(name = "达到后重置天数，中断将重置天数。0为不开启连续签到", width = 15)
    @ApiModelProperty(value = "达到后重置天数，中断将重置天数。0为不开启连续签到")
    private java.lang.Integer signInContinueDay;
    /**最大签到天数，达到后不可继续签到，需要申请提现并通过后才能继续签到，通过后重置天数，0为不限制*/
    @Excel(name = "最大签到天数，达到后不可继续签到，需要申请提现并通过后才能继续签到，通过后重置天数，0为不限制", width = 15)
    @ApiModelProperty(value = "最大签到天数，达到后不可继续签到，需要申请提现并通过后才能继续签到，通过后重置天数，0为不限制")
    private java.lang.Integer signInMaxDay;
    /**最多可补签天数，超过该天数则无法补签*/
    @Excel(name = "最多可补签天数，超过该天数则无法补签", width = 15)
    @ApiModelProperty(value = "最多可补签天数，超过该天数则无法补签")
    private java.lang.Integer signInMakeupDays;
    /**补签一次所扣除的金币。开启阶梯规则如：1,3,5...，表示往前推第一天扣1金币，第二天扣3金币，第三天扣5金币，以此类推。不开启阶梯则直接输入固定值，如：5，表示每次补签均扣除5金币*/
    @Excel(name = "补签一次所扣除的金币。开启阶梯规则如：1,3,5...，表示往前推第一天扣1金币，第二天扣3金币，第三天扣5金币，以此类推。不开启阶梯则直接输入固定值，如：5，表示每次补签均扣除5金币", width = 15)
    @ApiModelProperty(value = "补签一次所扣除的金币。开启阶梯规则如：1,3,5...，表示往前推第一天扣1金币，第二天扣3金币，第三天扣5金币，以此类推。不开启阶梯则直接输入固定值，如：5，表示每次补签均扣除5金币")
    private java.lang.String signInMakeupCost;
    /**补签可获得奖励*/
    @Excel(name = "补签可获得奖励", width = 15)
    @ApiModelProperty(value = "补签可获得奖励")
    private java.lang.Boolean signInMakeupReward;
    /**签到说明*/
    @Excel(name = "签到说明", width = 15)
    @ApiModelProperty(value = "签到说明")
    private java.lang.String signInNote;
    /**启用短信*/
    @Excel(name = "启用短信", width = 15)
    @ApiModelProperty(value = "启用短信")
    private java.lang.Boolean enableSms;
    /**允许注册*/
    @Excel(name = "允许注册", width = 15)
    @ApiModelProperty(value = "允许注册")
    private java.lang.Boolean allowRegister;
    /**启用手机号注册*/
    @Excel(name = "启用手机号注册", width = 15)
    @ApiModelProperty(value = "启用手机号注册")
    private java.lang.Boolean mobileRegister;
    /**启用用户名和密码注册*/
    @Excel(name = "启用用户名和密码注册", width = 15)
    @ApiModelProperty(value = "启用用户名和密码注册")
    private java.lang.Boolean usernameRegister;
    /**昵称唯一*/
    @Excel(name = "昵称唯一", width = 15)
    @ApiModelProperty(value = "昵称唯一")
    private java.lang.Boolean nicknameUnique;
    /**启用邮箱注册*/
    @Excel(name = "启用邮箱注册", width = 15)
    @ApiModelProperty(value = "启用邮箱注册")
    private java.lang.Boolean emailRegister;
    /**注册奖励*/
    @Excel(name = "注册奖励", width = 15)
    @ApiModelProperty(value = "注册奖励")
    private java.lang.Integer registerPresent;
    /**允许发红包*/
    @Excel(name = "允许发红包", width = 15)
    @ApiModelProperty(value = "允许发红包")
    private java.lang.Boolean allowRedPack;
    /**单个红包金额上限*/
    @Excel(name = "单个红包金额上限", width = 15)
    @ApiModelProperty(value = "单个红包金额上限")
    private java.lang.Integer redPackMax;
    /**允许转账*/
    @Excel(name = "允许转账", width = 15)
    @ApiModelProperty(value = "允许转账")
    private java.lang.Boolean allowTransfer;
    /**单次转账金额上限*/
    @Excel(name = "单次转账金额上限", width = 15)
    @ApiModelProperty(value = "单次转账金额上限")
    private java.lang.String transferMax;
    /**手机验证码登录*/
    @Excel(name = "手机验证码登录", width = 15)
    @ApiModelProperty(value = "手机验证码登录")
    private java.lang.Boolean mobileSmsLogin;
    /**邮箱验证码登录*/
    @Excel(name = "邮箱验证码登录", width = 15)
    @ApiModelProperty(value = "邮箱验证码登录")
    private java.lang.Boolean emailCodeLogin;
    /**QQ登录*/
    @Excel(name = "QQ登录", width = 15)
    @ApiModelProperty(value = "QQ登录")
    private java.lang.Boolean qqLogin;
    /**微信登录*/
    @Excel(name = "微信登录", width = 15)
    @ApiModelProperty(value = "微信登录")
    private java.lang.Boolean wechatLogin;
    /**qq AppId*/
    @Excel(name = "qq AppId", width = 15)
    @ApiModelProperty(value = "qq AppId")
    private java.lang.String qqAppId;
    /**wechat AppId*/
    @Excel(name = "wechat AppId", width = 15)
    @ApiModelProperty(value = "wechat AppId")
    private java.lang.String wechatAppId;
    /**wechat AppKey*/
    @Excel(name = "wechat AppKey", width = 15)
    @ApiModelProperty(value = "wechat AppKey")
    private java.lang.String wechatAppKey;
    /**当连续登录失败达到该次数时锁定账号*/
    @Excel(name = "当连续登录失败达到该次数时锁定账号", width = 15)
    @ApiModelProperty(value = "当连续登录失败达到该次数时锁定账号")
    private java.lang.Integer loginFailedLockedTimes;
    /**当连续登录失败的锁定时长*/
    @Excel(name = "当连续登录失败的锁定时长", width = 15)
    @ApiModelProperty(value = "当连续登录失败的锁定时长")
    private java.lang.Integer loginFailedLockedDuration;
    /**登录有效时长，默认：7天*/
    @Excel(name = "登录有效时长，默认：7天", width = 15)
    @ApiModelProperty(value = "登录有效时长，默认：7天")
    private java.lang.Integer loginValidDay;
    /**用户头像需要审核*/
    @Excel(name = "用户头像需要审核", width = 15)
    @ApiModelProperty(value = "用户头像需要审核")
    private java.lang.String userAvatarNeedAudit;
    /**限制ip单位时间,秒*/
    @Excel(name = "限制ip单位时间,秒", width = 15)
    @ApiModelProperty(value = "限制ip单位时间,秒")
    private java.lang.Integer limitIpDuration;
    /**限制ip单位时间内注册用户数*/
    @Excel(name = "限制ip单位时间内注册用户数", width = 15)
    @ApiModelProperty(value = "限制ip单位时间内注册用户数")
    private java.lang.Integer limitIpCount;
    /**限制设备单位时间，秒*/
    @Excel(name = "限制设备单位时间，秒", width = 15)
    @ApiModelProperty(value = "限制设备单位时间，秒")
    private java.lang.Integer limitDeviceDuration;
    /**限制同一设备单位时间内注册用户数*/
    @Excel(name = "限制同一设备单位时间内注册用户数", width = 15)
    @ApiModelProperty(value = "限制同一设备单位时间内注册用户数")
    private java.lang.Integer limitDeviceCount;
    /**限制消息发送单位时间，秒*/
    @Excel(name = "限制消息发送单位时间，秒", width = 15)
    @ApiModelProperty(value = "限制消息发送单位时间，秒")
    private java.lang.Integer limitMsgDuration;
    /**限制消息发送单位时间内发送条数*/
    @Excel(name = "限制消息发送单位时间内发送条数", width = 15)
    @ApiModelProperty(value = "限制消息发送单位时间内发送条数")
    private java.lang.Integer limitMsgCount;
    /**支持的国家区号代码*/
    @Excel(name = "支持的国家区号代码", width = 15)
    @ApiModelProperty(value = "支持的国家区号代码")
    private java.lang.String dialCodes;
    /**启用定位*/
    @Excel(name = "启用定位", width = 15)
    @ApiModelProperty(value = "启用定位")
    private java.lang.Boolean location;
    /**高德安卓key*/
    @Excel(name = "高德安卓key", width = 15)
    @ApiModelProperty(value = "高德安卓key")
    private java.lang.String gdAndroidKey;
    /**高德苹果key*/
    @Excel(name = "高德苹果key", width = 15)
    @ApiModelProperty(value = "高德苹果key")
    private java.lang.String gdIosKey;
    /**高德网页key*/
    @Excel(name = "高德网页key", width = 15)
    @ApiModelProperty(value = "高德网页key")
    private java.lang.String gdWebKey;
    /**显示通话记录*/
    @Excel(name = "显示通话记录", width = 15)
    @ApiModelProperty(value = "显示通话记录")
    private java.lang.Boolean dialHistory;
    /**启用动态*/
    @Excel(name = "启用动态", width = 15)
    @ApiModelProperty(value = "启用动态")
    private java.lang.Boolean enablePost;
    /**敏感词过滤*/
    @Excel(name = "敏感词过滤", width = 15)
    @ApiModelProperty(value = "敏感词过滤")
    private java.lang.Boolean keywordFilter;
    /**显示好友删除*/
    @Excel(name = "显示好友删除", width = 15, dicCode = "show_friend_delete")
    @ApiModelProperty(value = "显示好友删除")
    private java.lang.Integer showFriendDelete;
    /**所属服务器*/
    @ApiModelProperty(value = "所属服务器")
    private java.lang.Integer serverId;
}
