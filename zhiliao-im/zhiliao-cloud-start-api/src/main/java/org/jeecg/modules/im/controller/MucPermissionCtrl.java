package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.query_helper.QMucPermission;
import org.jeecg.modules.im.service.IMucPermissionService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 群管理权限
 */
@RestController
@RequestMapping("/a/mucPermission")
public class MucPermissionCtrl extends BaseApiCtrl {
    @Resource
    private IMucPermissionService IMucPermissionService;

    @RequestMapping("/getOne")
    public Result<Object> send(@RequestParam Integer mucId,@RequestParam Integer userId){
        return IMucPermissionService.findByUserOfMuc(userId,mucId);
    }
    @RequestMapping("/update")
    public Result<Object> updateByCondition(QMucPermission q){
        return IMucPermissionService.updateByCondition(q);
    }
}
