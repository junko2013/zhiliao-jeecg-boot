
package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.entity.OnlineData;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.query_helper.QServer;
import org.jeecg.modules.im.service.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;


/**
 * 统计任务
 */
@Component
@Slf4j
public class StatisticJobHandler {

    @Resource
    private UserService userService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private StatisticService statisticService;
    @Resource
    private OnlineDataService onlineDataService;
    @Resource
    private ServerService serverService;

    @XxlJob(value = "online")
    public ReturnT<String> online(String params) {
        String date = ToolDateTime.getDate(ToolDateTime.pattern_ymd);
        List<Server> servers = serverService.findAll(new QServer());
        OnlineData onlineData;
        for (Server server : servers) {
            onlineData = new OnlineData();
            onlineData.setDate(date);
            onlineData.setTsCreate(new Date().getTime());
            onlineData.setServerId(server.getId());
            onlineData.setUser(userService.getOnlineCount());
            onlineData.setDevice(deviceService.getCount(null,true));
            onlineData.setTotalUser(userService.getTotalUser());
            onlineData.setTotalDevice(deviceService.getCount(null,null));
            onlineDataService.save(onlineData);
        }
        return ReturnT.SUCCESS;
    }
    @XxlJob(value = "daily")
    public ReturnT<String> daily(String params) {
        log.info("我是定时任务,我执行了...............................");
        return ReturnT.SUCCESS;
    }
}

