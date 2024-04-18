package org.jeecg.modules.im.xxljob;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.IUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 关注系统号
 */
@Component
@Slf4j
public class FollowSysJobHandler {

    @Resource
    private IUserService IUserService;

    @XxlJob(value = "followSys")
    public ReturnT<String> followSys(String params) {
        List<User> sysUsers = IUserService.findSysUser();
        List<Integer> types = new ArrayList<>();
        types.add(User.Type.common.getCode());
        types.add(User.Type.business.getCode());
        //普通用户和业务号
        List<User> users = IUserService.findByTypes(types);
        for (User user : users) {
            IUserService.batchFollowSys(user,sysUsers);
        }
        return ReturnT.SUCCESS;
    }
}

