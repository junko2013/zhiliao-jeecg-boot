package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.entity.Call;
import org.jeecg.modules.im.service.ICallService;
import org.jeecg.modules.im.service.IXMPPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 音视频通话状态变更任务
 */
@Component
@Slf4j
public class CallJobHandler {

    @Resource
    private ICallService ICallService;
    @Lazy
    @Autowired
    private RedisUtil redisUtil;
    @Resource
    private IXMPPService IXMPPService;

    @XxlJob(value = "callStatusUpdate")
    public ReturnT<String> callStatusUpdate(String params) {
        //查询等待接听和接通中的通话
        List<Call> unFinishedCalls = ICallService.findUnFinished();
        for (Call call : unFinishedCalls) {
            //等待接听
            if(call.getStatus()==Call.Status.Waiting.getCode()){
                if(ToolDateTime.getDateSecondSpace(call.getTsCreate(),new Date()) >60){
                    call.setStatus(Call.Status.Timeout.getCode());
                }
            }
            //接通
            else if(call.getStatus()==Call.Status.Connected.getCode()){
                //获取双方最后发送的心跳包
                String fromKey = String.format(ConstantCache.CALL_HEARTBEAT,call.getId(),call.getFromId());
                String toKey = String.format(ConstantCache.CALL_HEARTBEAT,call.getId(),call.getFromId());
                long tsFrom = 0,tsTo = 0;
                if(redisUtil.get(fromKey)!=null){
                    //发起方的最后心跳
                    tsFrom = Integer.parseInt((String) redisUtil.get(fromKey));
                }
                if(redisUtil.get(toKey)!=null){
                    //接收方的最后心跳
                    tsTo = Integer.parseInt((String) redisUtil.get(toKey));
                }
                //发起方最后发送的心跳包是5秒前，那就是掉线了
                if(new Date().getTime()-tsFrom>5){
                    call.setStatus(Call.Status.Suspend.getCode());
                    call.setTsEnd(new Date());
                    call.setComment("end unexpected：launcher heartbeat duration exceed");
                    ICallService.updateById(call);
                    //通知接收方已断开
                    MessageBean messageBean = new MessageBean();
                    messageBean.setUserId(call.getFromId());
                    messageBean.setToUserId(call.getToId());
                    messageBean.setType(MsgType.callSuspend.getType());
                    messageBean.setContent(call.getId()+"");
                    IXMPPService.sendMsgToOne(messageBean);
                    continue;
                }
                //接收方最后发送的心跳包是5秒前，那就是掉线了
                if(new Date().getTime()-tsTo>5){
                    call.setStatus(Call.Status.Suspend.getCode());
                    call.setTsEnd(new Date());
                    call.setComment("end unexpected：receiver heartbeat duration exceed");
                    ICallService.updateById(call);
                    //通知发起方已断开
                    MessageBean messageBean = new MessageBean();
                    messageBean.setUserId(call.getToId());
                    messageBean.setToUserId(call.getFromId());
                    messageBean.setType(MsgType.callSuspend.getType());
                    messageBean.setContent(call.getId()+"");
                    IXMPPService.sendMsgToOne(messageBean);
                }
            }
        }
        return ReturnT.SUCCESS;
    }
}

