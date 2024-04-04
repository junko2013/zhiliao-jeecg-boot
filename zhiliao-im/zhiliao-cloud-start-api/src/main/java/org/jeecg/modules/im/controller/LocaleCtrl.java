package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 语言包
 */
@RestController
@RequestMapping("/a/locale")
public class LocaleCtrl extends BaseApiCtrl {

    @Resource
    private LocaleService localeService;

    @NoNeedUserToken
    @RequestMapping("/all")
    public Result<Object> all() {
        return success(localeService.findAll());
    }

    @NoNeedUserToken
    @RequestMapping("/getContent")
    public Result<Object> getContent(@RequestParam int id) {
        return success(localeService.getContent(id));
    }
}
