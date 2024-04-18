package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.service.IDeviceService;
import org.jeecg.modules.im.service.IOnlineDataService;
import org.jeecg.modules.im.service.IStatisticService;
import org.jeecg.modules.im.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 请求加好友过期
 */
@Component
@Slf4j
public class SayHelloJobHandler {

    @Resource
    private IUserService IUserService;
    @Resource
    private IDeviceService IDeviceService;
    @Resource
    private IStatisticService IStatisticService;
    @Resource
    private IOnlineDataService IOnlineDataService;

    //超时失效
    @XxlJob(value = "sayHelloInvalid")
    public ReturnT<String> sayHelloInvalid(String params) {
        String date = ToolDateTime.getDate(ToolDateTime.pattern_ymd);
        return ReturnT.SUCCESS;
    }
}

