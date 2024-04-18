package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 设备离线
 */
@Component
@Slf4j
public class DeviceOfflineJobHandler {

    @Resource
    private DeviceService deviceService;
    @Resource
    private UserService userService;

    /**
     * 客户端每5秒发送一次心跳，任务5秒执行一次，这里将最后发送心跳在当前时间之前15秒（增加容错率）的设备置为离线
     * 如果用户在线但在线设备列表为空，则标记为离线
     * @param params
     * @return
     */

    @XxlJob(value = "deviceOffline")
    public ReturnT<String> offline(String params) {
        deviceService.updateOffline(ToolDateTime.getDateByDatePlusSeconds(new Date(),-10).getTime());
        userService.updateOffline(new Date().getTime());
        //用户离线是否需要广播？
        return ReturnT.SUCCESS;
    }
}
