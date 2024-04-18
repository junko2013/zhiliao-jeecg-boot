package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import com.xxl.job.core.log.XxlJobLogger;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.entity.MucMsgRead;
import org.jeecg.modules.im.service.IMucMsgReadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 同步群聊消息已读记录
 */
@Component
@Slf4j
public class SyncMucMsgReadJobHandler {
    @Lazy
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private IMucMsgReadService mucMsgReadService;

    @XxlJob(value = "syncMucMsgRead")
    public ReturnT<String> syncMucMsgRead(String params) {
        try {

            String prefix = ConstantCache.MUC_MSG_READ.replace("%s", "");
            List<MucMsgRead> records = null;
            Set<String> keys = redisUtil.keys(prefix);
            if (!keys.isEmpty()) {
                records = new ArrayList<>();
                for (String key : keys) {
                    Map map = redisUtil.hmget(key);
                    for (Object k : map.keySet()) {
                        Long msgId = Long.valueOf(key.replace(prefix, ""));
                        int userId = Integer.parseInt((String) k);
                        Long tsRead = (Long) map.get(k);
                        MucMsgRead read = mucMsgReadService.getOne(msgId, userId);
                        if (read == null) {
                            read = new MucMsgRead();
                            read.setMsgId(msgId);
                            read.setUserId(userId);
                            read.setTsRead(ToolDateTime.getDate(tsRead));
                            records.add(read);
                        }else {
                            redisUtil.del(key);
                        }
                    }
                }
            }
            if (records != null && !records.isEmpty()) {
                if(mucMsgReadService.saveBatch(records)){
                    for (String key : keys) {
                        redisUtil.del(key);
                    }
                }
            }
            return ReturnT.SUCCESS;
        }catch (Exception e){
            XxlJobLogger.log("同步已读消息异常：{}",e);
            return ReturnT.FAIL;
        }
    }
}

