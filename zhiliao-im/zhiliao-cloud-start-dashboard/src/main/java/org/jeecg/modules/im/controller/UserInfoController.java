package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.UserInfo;
import org.jeecg.modules.im.service.IUserInfoService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/userInfo")
public class UserInfoController extends BaseBackController<UserInfo, IUserInfoService> {

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer userId) {
        return success(service.findByUserId(userId));
    }
    @RequestMapping("/update")
    public Result<Object> updateSetting(@RequestBody UserInfo userInfo) {
        return success(userInfo.updateById());
    }
}
