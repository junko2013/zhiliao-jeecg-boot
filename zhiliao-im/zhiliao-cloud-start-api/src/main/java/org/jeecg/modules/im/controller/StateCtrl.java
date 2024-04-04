package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.StateService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 用户状态
 */
@RestController
@RequestMapping("/a/state")
public class StateCtrl extends BaseApiCtrl {
    @Resource
    private StateService stateService;

    @RequestMapping("/all")
    public Result<Object> all(){
        return stateService.findAll();
    }
}
