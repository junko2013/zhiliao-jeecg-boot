package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.IHelpService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 帮助
 */
@RestController
@RequestMapping("/a/help")
public class HelpCtrl extends BaseApiCtrl {
    @Resource
    private IHelpService helpService;

    @NoNeedUserToken
    @RequestMapping("/all")
    public Result<Object> all(){
        return helpService.findAll();
    }
}
