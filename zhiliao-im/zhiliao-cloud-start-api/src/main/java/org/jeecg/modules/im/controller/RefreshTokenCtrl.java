package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.constant.ConstantZhiLiao;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.DeviceService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 刷新 refresh_token
 */
@Slf4j
@RestController
@RequestMapping("/a/auth")
public class RefreshTokenCtrl extends BaseApiCtrl {
    @Resource
    private UserService userService;
    @Resource
    private DeviceService deviceService;
    @NoNeedUserToken
    @RequestMapping("/refresh_token")
    public Result<Object> refreshToken(@RequestParam String refreshToken){
        // 解密获得userId，用于和数据库进行对比
        Integer userId = JwtUtilApp.verify(refreshToken);
        if (userId == null) {
//            throw new AuthenticationException("invalid");
            return fail("invalid");
        }
        // 查询用户信息
        log.debug("———校验token是否有效————checkUserTokenIsEffect——————— "+ refreshToken);
        User user = userService.findById(userId);
        if (user == null) {
            return fail("用户不存在");
        }
        // 判断用户状态
        if (user.getTsLocked()>0) {
            return fail("用户已被禁用");
        }
        Device device = deviceService.findByPlatform(getDeviceNo(), getDevicePlatform(), getDeviceDetail(),user);
        if(device==null){
            return fail("设备未登录过");
        }
        if(device.getTsDisabled()>0){
            return fail(ConstantZhiLiao.ACCOUNT_LOCKED,"当前设备已被禁用");
        }
        String accessToken = JwtUtilApp.getAccessToken(user.getId(), user.getPassword(),user.getAccount());
        Kv data = Kv.create();
        data.put("access_token",accessToken);
        data.put("refresh_token",JwtUtilApp.getRefreshToken(user.getId(), user.getPassword()));
        //更新设备token
        device.setToken(accessToken);
        deviceService.updateById(device);
        //刷新设备token
        return success(data);
    }
}
