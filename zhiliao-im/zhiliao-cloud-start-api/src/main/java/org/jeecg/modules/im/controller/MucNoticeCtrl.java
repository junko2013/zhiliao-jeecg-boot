package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucNotice;
import org.jeecg.modules.im.entity.query_helper.QMucNotice;
import org.jeecg.modules.im.service.MucNoticeService;
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
@RequestMapping("/a/mucNotice")
public class MucNoticeCtrl extends BaseApiCtrl {
    @Resource
    private MucNoticeService mucNoticeService;

    @RequestMapping("/findByMuc")
    public Result<Object> findByMuc(@RequestParam Integer mucId){
        return success(mucNoticeService.findByMuc(mucId));
    }

    /**
     * 添加或更新
     */
    @PostMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(QMucNotice notice){
        notice.setUserId(getCurrentUserId());
        return mucNoticeService.createOrUpdate(notice);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return mucNoticeService.del(ids);
    }
}
