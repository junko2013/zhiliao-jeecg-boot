package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.entity.Call;
import org.jeecg.modules.im.entity.SysConfig;
import org.jeecg.modules.im.io.agora.media.RtcTokenBuilder2;
import org.jeecg.modules.im.service.ICallService;
import org.jeecg.modules.im.service.ISysConfigService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;


/**
 * 通话记录
 */
@RestController
@RequestMapping("/a/call")
public class CallCtrl extends BaseApiCtrl {
    @Resource
    private ICallService ICallService;

    @Resource
    private ISysConfigService ISysConfigService;

    static int tokenExpirationInSeconds = 3600;
    static int privilegeExpirationInSeconds = 3600;

    @RequestMapping("/all")
    public Result<Object> all(){
        return success(ICallService.findAll(getCurrentUserId()));
    }
    @RequestMapping("/getOne")
    public Result<Object> getOne(@RequestParam String recordId){
        Integer userId = getCurrentUserId();
        Call record = ICallService.getById(Integer.parseInt(recordId));
        //既不是发起者也不是接收者
        if(record==null||(!record.getFromId().equals(userId) &&!record.getToId().equals(userId))){
            return fail();
        }
        return success(record);
    }
    /**
     * 发起通话
     */
    @PostMapping("/launch")
    public Result<Object> launch(@RequestParam int toId,@RequestParam boolean isVideo){
        SysConfig sysConfig = ISysConfigService.get();

        int uid = getCurrentUserId();
        String channelId = uid+"-"+toId;

        Call call = new Call();
        call.setToId(toId);
        call.setIsVideo(isVideo);
        call.setFromId(uid);
        call.setChannelId(channelId);
        call.setStatus(Call.Status.Waiting.getCode());
        call.setTsCreate(new Date());
        call.setServerId(getServerId());

        Kv data = Kv.create();
        Result<Object> result = ICallService.launch(call);
        if(result.isSuccess()){
            data.set("record",result.getResult());
            data.set("channel", channelId);
            data.set("token", new RtcTokenBuilder2().buildTokenWithUid(
                    sysConfig.getAgoraAppId(),
                    sysConfig.getAgoraAppCertificate(),
                    channelId,
                    uid,
                    RtcTokenBuilder2.Role.ROLE_PUBLISHER,
                    tokenExpirationInSeconds,
                    privilegeExpirationInSeconds
            ));
            return success(data);
        }
        return result;
    }

    /**
     * 受邀方获取通话token
     * @param callId
     * @return
     */
    @PostMapping("/getToken")
    public Result<Object> getToken(@RequestParam int callId){
        SysConfig sysConfig = ISysConfigService.get();

        int uid = getCurrentUserId();

        Call call = ICallService.getById(callId);
        if(call ==null|| call.getToId()!=uid){
            return fail("通话记录不存在");
        }

        return success(new RtcTokenBuilder2().buildTokenWithUid(
                sysConfig.getAgoraAppId(),
                sysConfig.getAgoraAppCertificate(),
                call.getChannelId(),
                uid,
                RtcTokenBuilder2.Role.ROLE_SUBSCRIBER,
                tokenExpirationInSeconds,
                privilegeExpirationInSeconds
        ));
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return ICallService.del(ids);
    }
}
