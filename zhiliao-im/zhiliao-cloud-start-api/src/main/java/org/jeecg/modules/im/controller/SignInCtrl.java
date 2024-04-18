package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.ISignInService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 签到
 */
@RestController
@RequestMapping("/a/signIn")
@Slf4j
public class SignInCtrl extends BaseApiCtrl {

    @Resource
    private ISignInService signInService;

    @PostMapping("/info")
    public Result<Object> info(){
        return signInService.info(getCurrentUserId());
    }

    @PostMapping("/sign")
    public Result<Object> sign(){
        return signInService.sign(getCurrentUserId());
    }
    //补签
    @PostMapping("/makeup")
    public Result<Object> makeup(@RequestParam String date){
        return signInService.makeup(date,getCurrentUserId());
    }

}
