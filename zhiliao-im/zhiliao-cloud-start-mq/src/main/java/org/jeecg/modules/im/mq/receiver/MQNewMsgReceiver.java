package org.jeecg.modules.im.mq.receiver;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.*;
import org.jeecg.modules.im.base.tools.GsonUtil;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.service.*;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * 新消息消费者
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 */
@Slf4j
@RabbitComponent(value = "mqNewMsgReceiver")
@RabbitListener(queues = ConstantMQ.NEW_MSG)
public class MQNewMsgReceiver extends BaseRabbiMqHandler<String> {
    @Resource
    private IFriendService friendService;
    @Resource
    private IUserService userService;
    @Resource
    private IMucService mucService;
    @Resource
    private IMucConfigService mucConfigService;
    @Resource
    private IMucMsgService mucMsgService;
    @Resource
    private IMsgService msgService;
    @Resource
    private ICallService callService;
    @Resource
    private IGifService gifService;
    @Resource
    private IMyGifService myGifService;
    @Resource
    private IMyStickerService myStickerService;
    @Resource
    private ICustomEmojiService customEmojiService;
    @Resource
    private IMucMemberService mucMemberService;
    @Resource
    private IMucMsgDeleteService mucMsgDeleteService;
    @Resource
    private ITagService tagService;
    @Resource
    private ISayHelloService sayHelloService;
    @Resource
    private ISayHelloReplyService sayHelloReplyService;
    @Lazy
    @Resource
    private RedisUtil redisUtil;

    @RabbitHandler
    public void onMessage(String message, com.rabbitmq.client.Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        super.onMessage(message, deliveryTag, channel, new MqListener<String>() {
            @Override
            public void handler(String message, com.rabbitmq.client.Channel channel) {
                try {
                    //业务处理
                    log.info("新消息,message= " + message);
                    Msg msg = null;
                    MucMsg mucMsg = null;
                    try {
                        msg = GsonUtil.fromJson(message, Msg.class);
                    } catch (Exception e) {
                        log.error("parse sg msg error:{0}", e);
                    }
                    if (msg != null && msg.getToUserId() != null) {
                        processMsg(msg);
                        return;
                    }

                    try {
                        mucMsg = GsonUtil.fromJson(message, MucMsg.class);
                    } catch (Exception e2) {
                        log.error("parse muc msg error:{0}", e2);
                    }
                    if (mucMsg != null && mucMsg.getMucId() != null) {
                        processMucMsg(mucMsg);
                    }
                }catch (Exception e){
                    log.error("消费新单聊/群聊消息异常：", e);
                    try {
                        channel.basicNack(deliveryTag, false, true);
                        log.info("重新返回队列");
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
    }
    //保存群消息
    private void processMucMsg(MucMsg mucMsg) {
        MsgType msgType = MsgType.getByType(mucMsg.getType());
        boolean needStore = msgType.isStore();
        Muc muc;
        try {
            if(mucMsgService.getById(mucMsg.getId())!=null){
                return;
            }
            JSONObject contentJson = null;
            try{
                contentJson = JSONObject.parseObject(mucMsg.getContent());
            }catch (Exception e){}
            muc = mucService.getById(mucMsg.getMucId());
            MucConfig mucConfig = mucConfigService.findByMuc(mucMsg.getMucId());
            //判断是否是撤回消息
            if (msgType.getType() == MsgType.revokeMsg.getType()) {
                String stanzaId = mucMsg.getContent();
                List<Msg> revokeMsgs = msgService.findByStanzaId(stanzaId);
                //撤销者
                User revoker = userService.findById(mucMsg.getUserId());
                for (Msg revokeMsg : revokeMsgs) {
                    revokeMsg.setTsRevoke(mucMsg.getTsSend());
                    //是系统号撤销的
                    if(revoker.getType()==User.Type.sysAccount.getCode()){
                        revokeMsg.setRevokeType(MsgRevokeType.System.getCode());
                    }else {
                        //撤销人和要被撤销的消息发送人是同一个，且消息为发送，则为发送人撤销，否则为接收方撤销
                        if (mucMsg.getUserId().intValue() == revokeMsg.getUserId()) {
                            revokeMsg.setRevokeType(MsgRevokeType.Sender.getCode());
                        } else {
                            revokeMsg.setRevokeType(MsgRevokeType.Receiver.getCode());
                        }
                    }
                }
                msgService.updateBatchById(revokeMsgs);
            }
            //消息已读
            else if(msgType.getType()==MsgType.read.getType()){
                // [{"userId":156,"stanzaId":"FUYUPTHCF","tsRead":1701702320813}]
                String content = mucMsg.getContent();
                JSONArray array = JSONArray.parseArray(content);
                for (Object o : array) {
                    JSONObject obj = (JSONObject)o;
                    //被读消息msgId
                    String msgId = obj.getString("msgId");
                    //阅读人
                    Integer readerId = obj.getInteger("userId");
                    Long readTs = obj.getLong("tsRead");
                    //将该消息的已读记录存储到redis
                    String cacheKey = String.format(ConstantCache.MUC_MSG_READ, msgId);
                    Map map = redisUtil.hmget(cacheKey);
                    map.put(readerId+"",readTs);
                    redisUtil.hmset(cacheKey,map);
                }
            }
            //消息置顶
            else if(msgType.getType()==MsgType.pin.getType()){
                assert contentJson!=null;
                int flag = contentJson.getInteger("flag");
                if(flag==0){
                    needStore = false;//取消置顶不保存
                }
                String stanzaId = contentJson.getString("stanzaId");
                List<Msg> msgs = msgService.findByStanzaId(stanzaId);
                for (Msg tempMsg : msgs) {
                    tempMsg.setTsPin(flag==0?null: mucMsg.getTsSend());
                }
                msgService.updateBatchById(msgs);
            }
            //全体禁言
            else if(msgType.getType()==MsgType.mucMute.getType()){
                assert contentJson!=null;
                int flag = contentJson.getInteger("flag");
                muc.setTsMute(flag==1? ToolDateTime.forever() :null);
                mucService.updateById(muc);
            }
            //单独禁言
            else if(msgType.getType()==MsgType.muteOne.getType()){
                assert contentJson!=null;
                int flag = contentJson.getInteger("flag");
                int userId = contentJson.getInteger("userId");
                int duration = contentJson.getInteger("duration");
                int plus = contentJson.getInteger("plus");
                MucMember member = mucMemberService.findByMucIdOfUser(muc.getId(),userId);
                if(flag==1){
                    //永久
                    if(duration==-1){
                        member.setTsMute(ToolDateTime.forever());
                    }
                    member.setTsMute(ToolDateTime.getDate((plus==1?member.getTsMute().getTime():getTs())+duration* 1000L));
                    member.setMuteType(User.MuteType.admin.getCode());
                }else{
                    member.setMuteType(User.MuteType.normal.getCode());
                    member.setTsMute(null);
                }
                mucMemberService.updateById(member);
            }
            //通知类
            else if(msgType.getType()==MsgType.setMucNotice.getType()){
                assert contentJson!=null;
                int type = contentJson.getInteger("type");
                int notice = contentJson.getInteger("notice");
                if(type== NoticeType.kickNotice.getCode()){
                    mucConfig.setKickNotice(notice);
                }else if(type== NoticeType.muteNotice.getCode()){
                    mucConfig.setMuteNotice(notice);
                }else if(type== NoticeType.quitNotice.getCode()){
                    mucConfig.setQuitNotice(notice);
                }else if(type== NoticeType.revokeNotice.getCode()){
                    mucConfig.setRevokeNotice(notice);
                }else if(type== NoticeType.modifyNicknameNotice.getCode()){
                    mucConfig.setModifyNicknameNotice(notice);
                }
                mucConfigService.updateById(mucConfig);
            }
            //消息撤回时限
            else if(msgType.getType()==MsgType.msgRevokeDuration.getType()){
                mucConfig.setRevokeDuration(Integer.parseInt(mucMsg.getContent()));
                mucConfigService.updateById(mucConfig);
            }
            //群聊邀请确认
            else if(msgType.getType()==MsgType.mucJoinVerify.getType()){
                mucConfig.setIsJoinVerify(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //显示群成员列表
            else if(msgType.getType()==MsgType.mucShowMemberList.getType()){
                mucConfig.setIsShowMemberList(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //进群后允许发言
            else if(msgType.getType()==MsgType.mucAllowTalkAfterJoin.getType()){
                mucConfig.setIsAllowTalkAfterJoin(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //显示成员群聊名称
            else if(msgType.getType()==MsgType.mucShowNickname.getType()){
                mucConfig.setIsShowNickname(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //移除成员时撤回其历史发言
            else if(msgType.getType()==MsgType.mucRevokeWhenKicked.getType()){
                mucConfig.setIsRevokeAllWhenKicked(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //入群前的消息可见
            else if(msgType.getType()==MsgType.mucShowMsgBeforeJoin.getType()){
                mucConfig.setIsShowMsgBeforeJoin(Integer.parseInt(mucMsg.getContent())==1);
                mucConfigService.updateById(mucConfig);
            }
            //gif消息
            //{"id":1,"origin":"","thumb":""}
            else if(msgType.getType()==MsgType.gif.getType()){
                assert contentJson!=null;
                int gifId = contentJson.getInteger("id");
                //gif发送次数+1
                redisUtil.incr(String.format(ConstantCache.GIF_SEND_TIMES,gifId),1);
                myGifService.addGif(mucMsg.getUserId(),gifId);
            }
            else if(msgType.getType()==MsgType.customEmoji.getType()){
                assert contentJson!=null;
                int id = contentJson.getInteger("id");
                CustomEmoji emoji = customEmojiService.getById(id);
                if(emoji!=null){
                    emoji.setTsSend(new Date());
                    customEmojiService.updateById(emoji);
                }
            }
            //贴纸消息
            else if(msgType.getType()==MsgType.sticker.getType()){
                assert contentJson!=null;
                int stickerId = contentJson.getInteger("id");
                //发送次数+1
                redisUtil.incr(String.format(ConstantCache.STICKER_ITEM_SEND_TIMES,stickerId),1);
                myStickerService.add(mucMsg.getUserId(),stickerId);
            }
            //修改成员权限
            else if(msgType.getType()==MsgType.changeMemberPermission.getType()){
                assert contentJson!=null;
                Integer viewMember = contentJson.getInteger("viewMember");
                Integer invite = contentJson.getInteger("invite");
                Integer pin = contentJson.getInteger("pin");
                Integer capture = contentJson.getInteger("capture");
                Integer msgRate = contentJson.getInteger("msgRate");
                Integer msgCount = contentJson.getInteger("msgCount");
                Integer sendImage = contentJson.getInteger("sendImage");
                Integer sendVoice = contentJson.getInteger("sendVoice");
                Integer sendLocation = contentJson.getInteger("sendLocation");
                Integer sendGif = contentJson.getInteger("sendGif");
                Integer sendSticker = contentJson.getInteger("sendSticker");
                Integer sendVideo = contentJson.getInteger("sendVideo");
                Integer sendRedPack = contentJson.getInteger("sendRedPack");
                Integer sendLink = contentJson.getInteger("sendLink");
                Integer sendCard = contentJson.getInteger("sendCard");
                Integer sendFile = contentJson.getInteger("sendFile");
                if(viewMember!=null){
                    mucConfig.setViewMember(viewMember==1);
                }
                if(invite!=null){
                    mucConfig.setInvite(invite==1);
                }
                if(pin!=null){
                    mucConfig.setPin(pin==1);
                }
                if(capture!=null){
                    mucConfig.setCapture(capture==1);
                }
                if(msgRate!=null){
                    mucConfig.setMsgRate(msgRate);
                }
                if(msgCount!=null){
                    mucConfig.setMsgCount(msgCount);
                }
                if(sendImage!=null){
                    mucConfig.setSendImage(sendImage==1);
                }
                if(sendVoice!=null){
                    mucConfig.setSendVoice(sendVoice==1);
                }
                if(sendLocation!=null){
                    mucConfig.setSendLocation(sendLocation==1);
                }
                if(sendGif!=null){
                    mucConfig.setSendGif(sendGif==1);
                }
                if(sendSticker!=null){
                    mucConfig.setSendSticker(sendSticker==1);
                }
                if(sendVideo!=null){
                    mucConfig.setSendVideo(sendVideo==1);
                }
                if(sendRedPack!=null){
                    mucConfig.setSendRedPack(sendRedPack==1);
                }
                if(sendLink!=null){
                    mucConfig.setSendLink(sendLink==1);
                }
                if(sendCard!=null){
                    mucConfig.setSendCard(sendCard==1);
                }
                if(sendFile!=null){
                    mucConfig.setSendFile(sendFile==1);
                }
                mucConfigService.updateById(mucConfig);
            }
        }catch (Exception e){
            log.error("处理新的群消息异常：",e);
            return;
        }
        //判断是否需要存储
        if(needStore){
            mucMsg.setServerId(muc.getServerId());
            if (mucMsgService.save(mucMsg)) {
                log.info("save new mucMsg success");
            } else {
                log.info("save new mucMsg fail");
            }
        }
    }
    //处理单聊消息
    private void processMsg(Msg msg) {
        MsgType msgType = MsgType.getByType(msg.getType());
        boolean needStore = msgType.isStore();
        User user = userService.findById(msg.getUserId());
        try{
            if(msgService.getById(msg.getId())!=null){
                return;
            }
            if(msg.getId()==null){
                msg.setId(getTs());
            }
            JSONObject contentJson = null;
            try{
                contentJson = JSONObject.parseObject(msg.getContent());
            }catch (Exception e){}
            //如果发送者是系统账号
//            User toUser = userService.findById(msg.getToUserId());
            //判断是否是好友
            Friend friend = friendService.findOne(msg.getUserId(), msg.getToUserId());
            if(friend!=null){
                friend.setTsLastTalk(new Date());
                //普通用户或者业务号
                if (user.getType() <= User.Type.business.getCode()) {
                    Friend friend2 = friendService.findOne(msg.getToUserId(), msg.getUserId());
                    friend2.setTsLastTalk(new Date());
                    friendService.updateById(friend);
                    friendService.updateById(friend2);
                }
            }

            //删除消息 单聊msgList [{"stanzaId":"","isSend":0}] 群聊mucMsgIds:1,2,3
            if(msgType.getType()==MsgType.delMsg.getType()) {
                assert contentJson!=null;
                String mucMsgIds = contentJson.getString("mucMsgIds");
                if(StringUtils.isBlank(mucMsgIds)){
                    JSONArray msgList = contentJson.getJSONArray("msgList");
                    //该消息是我发送的
                    for (Object o : msgList) {
                        JSONObject obj = (JSONObject)o;
                        boolean isSend = obj.getInteger("isSend")==1;
                        Msg msgDel = msgService.findByStanzaIdOfSend(obj.getString("stanzaId"),isSend);
                        if(msgDel==null||msgDel.getTsDelete()!=null){
                            return;
                        }
                        msgDel.setTsDelete(new Date());
                        msgService.updateById(msgDel);
                    }
                }else{
                    List<MucMsgDelete> deletes = new ArrayList<>();
                    for (String msgId : StringUtils.split(mucMsgIds, ",")) {
                        MucMsgDelete delete = new MucMsgDelete();
                        delete.setMsgId(Long.parseLong(msgId));
                        delete.setUserId(msg.getUserId());
                        deletes.add(delete);
                    }
                    mucMsgDeleteService.saveBatch(deletes);
                }

                return;
            }
            //清空记录
            if(msgType.getType()==MsgType.clearHistory.getType()) {
                assert contentJson!=null;
                //单聊
                Integer toUserId = contentJson.getInteger("toUserId");
                Integer both = contentJson.getInteger("both");
                //群聊
                Integer mucId = contentJson.getInteger("mucId");
                Integer all = contentJson.getInteger("all");

                //只处理自己发给自己
                if(Objects.equals(msg.getToUserId(), msg.getUserId())){
                    if(toUserId!=null) {
                        msgService.deleteLogic(msg.getUserId(), toUserId);
                        if (both == 1) {
                            msgService.deleteLogic(toUserId, msg.getUserId());
                        }
                    }else{
                        if(all==1){
                            mucMsgService.deleteLogic(mucId);
                        }else{
                            //修改成员最后可见消息时间
                            mucMemberService.updateTsVisible(mucId,msg.getUserId());
                        }
                    }
                }
                return;
            }
            //判断是否是撤回消息
            if(msgType.getType()==MsgType.revokeMsg.getType()){
                List<Msg> revokeMsgs = msgService.findByStanzaId(msg.getContent());
                //撤销者
                User revoker = userService.findById(msg.getUserId());
                for (Msg revokeMsg : revokeMsgs) {
                    revokeMsg.setTsRevoke(msg.getTsSend());
                    //是系统号撤销的
                    if(revoker.getType()==User.Type.sysAccount.getCode()){
                        revokeMsg.setRevokeType(MsgRevokeType.System.getCode());
                    }else {
                        //撤销人和要被撤销的消息发送人是同一个，且消息为发送，则为发送人撤销，否则为接收方撤销
                        if (msg.getUserId().intValue() == revokeMsg.getUserId()) {
                            revokeMsg.setRevokeType(MsgRevokeType.Sender.getCode());
                        } else {
                            revokeMsg.setRevokeType(MsgRevokeType.Receiver.getCode());
                        }
                    }
                }
                msgService.updateBatchById(revokeMsgs);
                return;
            }
            //控制台禁言
            if(msgType.getType()==MsgType.mute.getType()){
                return;
            }
            //控制台锁定
            if(msgType.getType()==MsgType.lock.getType()){
                return;
            }
            //不允许连接xmpp
            if(msgType.getType()==MsgType.noConnectXmpp.getType()){
                return;
            }
            //发起通话
            if(msgType.getType()==MsgType.call.getType()){
                int callId = Integer.parseInt(msg.getContent());
                return;
            }
            //接受通话
            if(msgType.getType()==MsgType.callAccept.getType()){
                int callId = Integer.parseInt(msg.getContent());
                Call call = callService.getById(callId);
                call.setTsAccept(msg.getTsSend());
                call.setStatus(Call.Status.Accept.getCode());
                callService.updateById(call);
                return;
            }
            //拒绝通话
            if(msgType.getType()==MsgType.callReject.getType()){
                int callId = Integer.parseInt(msg.getContent());
                Call call = callService.getById(callId);
                call.setTsReject(msg.getTsSend());
                call.setStatus(Call.Status.Reject.getCode());
                callService.updateById(call);
                return;
            }
            //挂断
            if(msgType.getType()==MsgType.callEnd.getType()){
                int callId = Integer.parseInt(msg.getContent());
                Call call = callService.getById(callId);
                call.setTsEnd(msg.getTsSend());
                call.setStatus(Call.Status.Reject.getCode());
                callService.updateById(call);
                return;
            }
            //接通
            if(msgType.getType()==MsgType.callConnected.getType()){
                int callId = Integer.parseInt(msg.getContent());
                Call call = callService.getById(callId);
                call.setTsConnected(msg.getTsSend());
                call.setStatus(Call.Status.Connected.getCode());
                callService.updateById(call);
                return;
            }
            //中断 已在定时任务中处理 此处不处理
            if(msgType.getType()==MsgType.callSuspend.getType()){
                return;
            }
            //取消
            if(msgType.getType()==MsgType.callCancel.getType()){
                int callId = Integer.parseInt(msg.getContent());
                Call call = callService.getById(callId);
                call.setTsCancel(msg.getTsSend());
                call.setStatus(Call.Status.Cancel.getCode());
                callService.updateById(call);
                return;
            }
            //消息置顶
            if(msgType.getType()==MsgType.pin.getType()){
                assert contentJson!=null;
                int flag = contentJson.getInteger("flag");
                if(flag==0){
                    needStore = false;//取消置顶不保存
                }
                String stanzaId = contentJson.getString("stanzaId");
                List<Msg> msgs = msgService.findByStanzaId(stanzaId);
                for (Msg tempMsg : msgs) {
                    tempMsg.setTsPin(flag==0?null:msg.getTsSend());
                }
                msgService.updateBatchById(msgs);
            }
            //更新好友资料
            else if(msgType.getType()==MsgType.updateFriend.getType()){
                assert contentJson!=null;
                int toUserId = contentJson.getInteger("toUserId");
                String remark = contentJson.getString("remark");
                String info = contentJson.getString("info");
                String phone = contentJson.getString("phone");
                String backImg = contentJson.getString("backImg");
                Friend f = friendService.findOne(msg.getUserId(), toUserId);
                if(f!=null){
                    if(!f.getRemark().equals(remark)){
                        f.setRemark(remark);
                    }
                    if(!f.getInfo().equals(info)){
                        f.setInfo(info);
                    }
                    if(!f.getPhone().equals(phone)){
                        f.setPhone(phone);
                    }
                    if(!f.getBackImg().equals(backImg)){
                        f.setBackImg(backImg);
                    }
                    friendService.updateById(f);
                }
            }
            //成员修改资料
            else if(msgType.getType()==MsgType.memberChangeInfo.getType()){
                assert contentJson!=null;
                int memberId = contentJson.getInteger("id");
                String remark = contentJson.getString("remark");
                String nickname = contentJson.getString("nickname");
                String backImg = contentJson.getString("backImg");
                MucMember member = mucMemberService.findById(memberId);
                if(member!=null){
                    member.setRemark(remark);
                    member.setNickname(nickname);
                    member.setBackImg(backImg);
                    mucMemberService.updateById(member);
                }
            }
            //接受但未接通
            else if(msgType.getType()==MsgType.callNotConnected.getType()){
                assert contentJson!=null;
                int callId = contentJson.getInteger("id");
                String reason = contentJson.getString("reason");
                Call call = callService.getById(callId);
                call.setComment(reason);
                call.setTsNotConnected(msg.getTsSend());
                call.setStatus(Call.Status.ConnectFail.getCode());
                callService.updateById(call);
            }
            //gif消息
            //{"id":1,"origin":"","thumb":""}
            else if(msgType.getType()==MsgType.gif.getType()){
                assert contentJson!=null;
                int gifId = contentJson.getInteger("id");
                //gif发送次数+1
                redisUtil.incr(String.format(ConstantCache.GIF_SEND_TIMES,gifId),1);
                myGifService.addGif(msg.getUserId(),gifId);
            }
            //添加gif
            else if(msgType.getType()==MsgType.addGif.getType()){
                assert contentJson!=null;
                int gifId = contentJson.getInteger("id");
                int flag = contentJson.getInteger("flag");
                if(flag==1){
                    myGifService.addGif(msg.getUserId(),gifId);
                }else{
                    myGifService.delBatch(msg.getUserId(),gifId+"");
                }
            }
            //置顶gif
            else if(msgType.getType()==MsgType.pinGif.getType()){
                assert contentJson!=null;
                int id = contentJson.getInteger("id");
                long ts = contentJson.getLong("ts");
                myGifService.pin(id,ts);
            }
            //添加贴纸
            else if(msgType.getType()==MsgType.addSticker.getType()){
                assert contentJson!=null;
                int stickerId = contentJson.getInteger("id");
                int flag = contentJson.getInteger("flag");
                if(flag==1){
                    myStickerService.add(msg.getUserId(),stickerId);
                }else{
                    myStickerService.del(msg.getUserId(),stickerId+"");
                }
            }
            //添加自定义表情
            else if(msgType.getType()==MsgType.addCustom.getType()){
                assert contentJson!=null;
                Integer flag = contentJson.getInteger("flag");
                //只处理删除
                if(flag!=null&&flag==0){
                    customEmojiService.del(contentJson.getInteger("id")+"");
//                }else{
//                    //以图片链接为判断依据
//                    CustomEmoji emoji = new CustomEmoji();
//                    emoji.setOrigin(contentJson.getString("origin"));
//                    emoji.setThumb(contentJson.getString("thumb"));
//                    emoji.setUserId(user.getId());
//                    emoji.setTsCreate(getTs());
//                    emoji.setHeight(contentJson.getInteger("height"));
//                    emoji.setWidth(contentJson.getInteger("width"));
//                    emoji.setServerId(user.getServerId());
//                    customEmojiService.save(emoji);
                }
            }
            //置顶自定义emoji
            else if(msgType.getType()==MsgType.pinCustom.getType()){
                assert contentJson!=null;
                int id = contentJson.getInteger("id");
                Long ts = contentJson.getLong("ts");
                customEmojiService.pin(id,ts);
            }
            //自定义表情消息
            else if(msgType.getType()==MsgType.customEmoji.getType()){
                assert contentJson!=null;
                int id = contentJson.getInteger("id");
                CustomEmoji emoji = customEmojiService.getById(id);
                if(emoji!=null){
                    emoji.setTsSend(new Date());
                    customEmojiService.updateById(emoji);
                }
            }
            //贴纸消息
            else if(msgType.getType()==MsgType.sticker.getType()){
                assert contentJson!=null;
                int stickerId = contentJson.getInteger("id");
                //发送次数+1
                redisUtil.incr(String.format(ConstantCache.STICKER_ITEM_SEND_TIMES,stickerId),1);
            }
            //标签消息
            else if(msgType.getType()==MsgType.tag.getType()){
                assert contentJson!=null;
                int type = contentJson.getInteger("type");
                //更新
                if(type==0){
                    JSONArray tags = contentJson.getJSONArray("tags");
                    for (Object tag : tags) {
                        tagService.update(GsonUtil.fromJson(String.valueOf(tag),Tag.class));
                    }
                }else if(type==-1){
                    int id = contentJson.getInteger("id");
                }
            }
            //打招呼回复已读
            else if(msgType.getType()==MsgType.sayHelloReplyRead.getType()){
                assert contentJson!=null;
                int id = contentJson.getInteger("id");
                Long ts = contentJson.getLong("ts");
                sayHelloReplyService.read(id,ts,user.getId());
            }
            //打招呼回复已读
            else if(msgType.getType()==MsgType.sayHelloRead.getType()){
                assert contentJson!=null;
                int id = contentJson.getInteger("id");
                Long ts = contentJson.getLong("ts");
                SayHello sayHello = sayHelloService.getById(id);
                sayHello.setTsRead(ts);
                sayHelloService.updateById(sayHello);
            }
            //拉黑/取消拉黑
            else if(msgType.getType()==MsgType.black.getType()){
                assert contentJson!=null;
                int toUserId = contentJson.getInteger("toUserId");
                long ts = contentJson.getLong("ts");
                Friend f1 = friendService.findOne(msg.getUserId(), toUserId);
                Friend f2 = friendService.findOne(toUserId,msg.getUserId());
                //拉黑
                if(ts>0){
                    if(f1.getTsBlack()!=null){
                     log.error("对方已在黑名单中");
                        return;
                    }
                    f1.setTsBlack(new Date());
                    f2.setTsBeenBlack(new Date());
                }else{
                    //取消拉黑
                    if(f1.getTsBlack()==null){
                        log.error("对方未在黑名单中");
                        return;
                    }
                    f1.setTsBlack(null);
                    f2.setTsBeenBlack(null);
                }
                friendService.updateById(f1);
                friendService.updateById(f2);
            }
            //对话消息
            else if(MsgType.dialoguePin.getType()<=msgType.getType()&&msgType.getType()<=MsgType.dialogueHide.getType()){
                assert contentJson!=null;
                int toId = contentJson.getInteger("toId");
                boolean isMuc = contentJson.getInteger("isMuc")==1;
                boolean isCancel = contentJson.getBoolean("isCancel");
                if (isMuc) {
                    MucMember mucMember = mucMemberService.findByMucIdOfUser(toId, msg.getUserId());
                    //归档
                    if(MsgType.dialogueArchive.getType()==msgType.getType()){
                        mucMember.setIsMsgArchive(!isCancel);
                    }
                    //置顶
                    else if(MsgType.dialoguePin.getType()==msgType.getType()){
                        mucMember.setTsPin(isCancel?null:new Date());
                    }
                    //已读
                    else if(MsgType.dialogueRead.getType()==msgType.getType()){
                        mucMember.setIsUnread(!isCancel);
                    }
                    //免打扰
                    else if(MsgType.dialogueNoDisturb.getType()==msgType.getType()){
                        mucMember.setIsNoDisturb(!isCancel);
                    }
                    //隐藏
                    else if(MsgType.dialogueHide.getType()==msgType.getType()){
                        mucMember.setIsHide(!isCancel);
                    }
                    mucMemberService.updateById(mucMember);
                } else {
                    Friend to = friendService.findOne(msg.getUserId(), toId);
                    //归档
                    if(MsgType.dialogueArchive.getType()==msgType.getType()){
                        to.setIsMsgArchive(!isCancel);
                    }
                    //置顶
                    else if(MsgType.dialoguePin.getType()==msgType.getType()){
                        to.setTsPin(isCancel?null:new Date());
                    }
                    //已读
                    else if(MsgType.dialogueRead.getType()==msgType.getType()){
                        to.setIsUnread(!isCancel);
                        //已读对方发给我的消息
                        msgService.readAllReceive(to.getUserId(),to.getToUserId());
                    }
                    //免打扰
                    else if(MsgType.dialogueNoDisturb.getType()==msgType.getType()){
                        to.setIsNoDisturb(!isCancel);
                    }
                    //隐藏
                    else if(MsgType.dialogueHide.getType()==msgType.getType()){
                        to.setIsHide(!isCancel);
                    }
                    friendService.updateById(to);
                }
            }
        }catch (Exception e){
            log.error("处理新的群消息异常：",e);
            return;
        }
        //判断是否需要存储
        if(needStore){
            msg.setServerId(user.getServerId());
            Msg receiveMsg = new Msg();
            BeanUtils.copyProperties(msg, receiveMsg);
            receiveMsg.setId(msg.getId()+1);
            receiveMsg.setIsSend(false);
            if (msgService.save(msg)) {
                receiveMsg.setOriginId(msg.getId());
                if(msgService.save(receiveMsg)){
                    log.info("save new msg success");
                }
            } else {
                log.info("save new msg fail");
            }
        }
    }
    private long getTs(){
        return new Date().getTime();
    }
}