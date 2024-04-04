package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.service.FontService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 应用字体
 */
@RestController
@RequestMapping("/a/font")
public class FontCtrl extends BaseApiCtrl {
    @Resource
    private FontService fontService;

    @NoNeedUserToken
    @RequestMapping("/all")
    public Result<Object> all(){
        return fontService.findAll();
    }
}
