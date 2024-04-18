package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.entity.query_helper.QFeedback;
import org.jeecg.modules.im.service.IFeedbackService;
import org.jeecg.modules.im.service.IFeedbackTypeService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 意见反馈
 */
@RestController
@RequestMapping("/a/feedback")
public class FeedbackCtrl extends BaseApiCtrl {
    @Resource
    private IFeedbackTypeService IFeedbackTypeService;
    @Resource
    private IFeedbackService IFeedbackService;

    @RequestMapping("/allType")
    public Result<Object> allType(){
        return IFeedbackTypeService.findAll();
    }


    @RequestMapping("/pagination")
    public Result<Object> pagination(QFeedback q){
        q.setUserId(getCurrentUserId());
        return success(IFeedbackService.paginationApi(new MyPage<>(getPage(),getPageSize()),q));
    }

    @RequestMapping("/submit")
    public Result<Object> submit(Feedback feedback){
        feedback.setUserId(getCurrentUserId());
        feedback.setTsCreate(getDate());
        feedback.setServerId(getServerId());
        return success(IFeedbackService.save(feedback));
    }
}
