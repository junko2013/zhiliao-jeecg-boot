package org.jeecg.modules.im.mq.receiver;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.jeecg.modules.im.base.tools.GsonUtil;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.mq.receiver.handler.StickerHandler;
import org.jeecg.modules.im.service.*;
import org.jxmpp.jid.impl.JidCreate;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;

/**
 * 新业务消息消费者
 * 如数据同步等
 */
@Slf4j
@RabbitComponent(value = "mqNewNormalMsgReceiver")
@RabbitListener(queues = ConstantMQ.NEW_NORMAL_MSG)
public class MQNewNormalMsgReceiver extends BaseRabbiMqHandler<String> {
    @Resource
    private FriendService friendService;
    @Resource
    private UserService userService;
    @Resource
    private MucService mucService;
    @Resource
    private MucConfigService mucConfigService;
    @Resource
    private MucMsgService mucMsgService;
    @Resource
    private MsgService msgService;
    @Resource
    private CallService callService;
    @Resource
    private GifService gifService;
    @Resource
    private MyGifService myGifService;
    @Resource
    private MyStickerService myStickerService;
    @Resource
    private CustomEmojiService customEmojiService;
    @Resource
    private MucMemberService mucMemberService;
    @Resource
    private MucMsgDeleteService mucMsgDeleteService;
    @Resource
    private TagService tagService;
    @Autowired
    private StickerHandler stickerHandler;
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
                    log.info("新业务消息,message= " + message);
                    Kv msg = GsonUtil.fromJson(message, Kv.class);
                    JSONObject json = JSON.parseObject(msg.getStr("body"));
                    if (json == null) {
                        return;
                    }
                    if (json.getString("module").equals("sticker")) {
                        stickerHandler.handle(json.getString("type"), JidCreate.from(msg.getStr("jid")));
                    }
                }catch (Exception e){
                    log.error("消费业务消息异常：", e);
//                    try {
//                        channel.basicNack(deliveryTag, false, true);
//                        log.info("重新返回队列");
//                    } catch (IOException ex) {
//                        ex.printStackTrace();
//                    }
                }
            }
        });
    }
}