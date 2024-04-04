package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.query_helper.QNotice;
import org.jeecg.modules.im.service.NoticeService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 系统公告
 */
@RestController
@RequestMapping("/a/notice")
public class NoticeCtrl extends BaseApiCtrl {

    @Resource
    private NoticeService noticeService;

    //查询用户所有的公告
    @RequestMapping("/all")
    public Result<Object> all(QNotice q) {
        q.setUserId(getCurrentUserId());
        q.setServerId(getServerId());
        return success(noticeService.findAll(q));
    }

}
