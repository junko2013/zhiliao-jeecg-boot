package org.jeecg.modules.im.mq.receiver;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.boot.starter.rabbitmq.core.BaseRabbiMqHandler;
import org.jeecg.boot.starter.rabbitmq.listenter.MqListener;
import org.jeecg.common.annotation.RabbitComponent;
import org.jeecg.modules.im.base.constant.ConstantMQ;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.service.MsgService;
import org.jeecg.modules.im.service.MucMsgService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;

/**
 * 消息送达
 * （@RabbitListener声明类上，一个类只能监听一个队列）
 */
@Slf4j
@RabbitListener(queues = ConstantMQ.MSG_RECEIVE)
@RabbitComponent(value = "mqMsgReceiveReceiver")
public class MQMsgReceiveReceiver extends BaseRabbiMqHandler<String> {

    @Resource
    private MucMsgService mucMsgService;
    @Resource
    private MsgService msgService;


    @RabbitHandler
    public void onMessage(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
                          @Header(required = false, name = "retry-count") Integer retryCount) {
        super.onMessage(message, deliveryTag, channel, new MqListener<String>() {
            @Override
            public void handler(String message, Channel channel) {
                try {
                    //业务处理
                    log.info("新的消息送达, " + message);
                    Kv kv = JSONObject.parseObject(message, Kv.class);
                    String stanzaId = kv.getStr("stanzaId");
                    Long ts = kv.getLong("ts");
                    int count = 0;
                    if(kv.getBoolean("isMuc")){
                        count = (int) mucMsgService.updateTsReceive(stanzaId,ts).getResult();
                    }else{
                        count = (int) msgService.updateTsReceive(stanzaId,ts).getResult();
                    }
                    if(count==0){
                        throw new Exception("未更新");
                    }
                    log.info("消费消息送达：kv={}", kv.toJson());
                } catch (Exception e) {
                    try {
                        final int currentRetryCount = (retryCount == null) ? 0 : retryCount;
                        channel.basicReject(deliveryTag, false);
                        if (currentRetryCount >= 10) {
                            // 达到最大重试次数，拒绝消息
                            log.info("重试次数超过限制，丢弃消息: {}", message);
                        } else {
                            // 重试次数加 1 并重新入队
                            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder().contentEncoding("UTF-8").contentType("text/plain").headers(new HashMap<>()).priority(0).headers(Collections.singletonMap("retry-count", currentRetryCount + 1));
                            channel.basicPublish("", ConstantMQ.MSG_RECEIVE, builder.build(), message.getBytes());
                        }
                    } catch (IOException ioException) {
                        log.error("处理消息重试时发生异常: ", ioException);
                    }
                }
            }
        });
    }


}