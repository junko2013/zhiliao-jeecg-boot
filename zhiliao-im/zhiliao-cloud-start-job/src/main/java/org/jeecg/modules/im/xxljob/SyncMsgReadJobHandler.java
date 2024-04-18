package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.entity.Msg;
import org.jeecg.modules.im.service.IMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 同步单聊消息已读
 */
@Component
@Slf4j
public class SyncMsgReadJobHandler {
    @Lazy
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private IMsgService IMsgService;

    @XxlJob(value = "syncMsgRead")
    public ReturnT<String> syncMsgRead(String params) {
        try {
            List<Msg> msgs = null;
            String prefix = ConstantCache.MSG_READ.replace("%s", "");
            Set<String> keys = redisUtil.keys(prefix);
            if (!keys.isEmpty()) {
                msgs = new ArrayList<>();
                Msg msg;
                for (String key : keys) {
                    String stanzaId = key.replace(prefix, "");
                    Long tsRead = Long.parseLong (redisUtil.get(key).toString());
                    msg = IMsgService.findByStanzaIdOfSend(stanzaId,true);
                    if(msg!=null){
                        msg.setTsRead(ToolDateTime.getDate(tsRead));
                        msgs.add(msg);
                    }
                    redisUtil.del(key);
                }
            }
            if(msgs!=null&&!msgs.isEmpty()){
                IMsgService.updateBatchById(msgs);
            }
            return ReturnT.SUCCESS;
        }catch (Exception e){
            XxlJobLogger.log("同步已读消息异常：{}",e);
            return ReturnT.FAIL;
        }
    }
}

