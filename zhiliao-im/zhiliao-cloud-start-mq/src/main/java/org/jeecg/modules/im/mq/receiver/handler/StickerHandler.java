package org.jeecg.modules.im.mq.receiver.handler;

import com.alibaba.fastjson.JSONObject;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.service.IStickerService;
import org.jeecg.modules.im.xmpp.XMPPManager;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.MessageBuilder;
import org.jxmpp.jid.Jid;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class StickerHandler {
    @Resource
    private IStickerService IStickerService;
    @Resource
    private XMPPManager xmppManager;

    public void handle(String type,Jid from) throws SmackException.NotConnectedException, InterruptedException {
        switch (type){
            case "getEmojis":{
                Kv data = Kv.by("data", JSONObject.toJSONString(IStickerService.getEmojis()));
                Message message = MessageBuilder.buildMessage()
                        .setBody(data.toJson())
                        .from(xmppManager.getAdminConnection().getUser())
                        .to(from)
                        .ofType(Message.Type.normal)
                        .build();
                xmppManager.getAdminConnection().sendStanza(message);
                break;
            }
            case "getAll":{

                break;
            }
        }
    }
}
