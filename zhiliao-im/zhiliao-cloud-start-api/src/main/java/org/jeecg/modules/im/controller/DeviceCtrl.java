package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.service.IDeviceService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 设备
 */
@RestController
@RequestMapping("/a/device")
@Slf4j
public class DeviceCtrl extends BaseApiCtrl {

    @Resource
    private IDeviceService IDeviceService;

    @RequestMapping("/all")
    public Result<Object> all() {
        return success(IDeviceService.findAll(getCurrentUserId()));
    }
    //终止特定会话
    @PostMapping("/terminate")
    public Result<Object> terminate(@RequestParam Integer id,@RequestParam(defaultValue = "0",required = false) String except) {
        return success(IDeviceService.terminate(getCurrentUserId(),id,except.equals("1")));
    }
}
