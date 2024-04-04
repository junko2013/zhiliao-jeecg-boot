package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.query_helper.QMucNotice;
import org.jeecg.modules.im.entity.query_helper.QPostComment;
import org.jeecg.modules.im.service.PostCommentService;
import org.jeecg.modules.im.service.PostService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 配置
 */
@RestController
@RequestMapping("/a/post/comment")
public class PostCommentCtrl extends BaseApiCtrl {

    @Resource
    private PostCommentService postCommentService;

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer commentId) {
        return success(postCommentService.getById(commentId));
    }

    /**
     * 添加或更新
     */
    @PostMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(QPostComment comment){
        comment.setUserId(getCurrentUserId());
        return postCommentService.createOrUpdate(comment);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return postCommentService.del(ids);
    }

}
