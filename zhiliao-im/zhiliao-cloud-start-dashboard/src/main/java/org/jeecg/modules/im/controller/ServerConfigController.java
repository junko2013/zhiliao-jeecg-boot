package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.service.ServerConfigService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/im/serverConfig")
public class ServerConfigController extends BaseBackController {
    @Resource
    private ServerConfigService serverConfigService;

    @RequestMapping("/detail")
    public Result<Object> detail(Integer id){
        return success(serverConfigService.get(id));
    }
    /**
     * 更新
     */
    @RequestMapping("/update")
    public Result<Object> update(@RequestBody @Validated ServerConfig config, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return success(serverConfigService.updateOne(config));
    }
}
