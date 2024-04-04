package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucInviteLink;
import org.jeecg.modules.im.service.MucInviteLinkService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 群组邀请链接
 */
@RestController
@RequestMapping("/a/mucInviteLink")
public class MucInviteLinkCtrl extends BaseApiCtrl {
    @Resource
    private MucInviteLinkService mucInviteLinkService;

    @RequestMapping("/findByMuc")
    public Result<Object> findByMuc(@RequestParam Integer mucId){
        return success(mucInviteLinkService.findByMuc(mucId));
    }

    /**
     * 添加或更新
     */
    @PostMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(MucInviteLink link){
        return mucInviteLinkService.createOrUpdate(link);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return mucInviteLinkService.del(ids);
    }
}
