package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 自定义网页链接
 */
@RestController
@RequestMapping("/a/link")
public class LinkCtrl extends BaseApiCtrl {

    @Resource
    private ILinkService ILinkService;

    @RequestMapping("/all")
    public Result<Object> all() {
        ServerConfig config = getServerConfig();
        Kv kv = Kv.by("links", ILinkService.list()).set("enablePost",config.getEnablePost());
        return success(kv);
    }

}
