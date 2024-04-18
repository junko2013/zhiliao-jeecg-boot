package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.PostComment;
import org.jeecg.modules.im.entity.query_helper.QPostComment;
import org.jeecg.modules.im.service.IPostCommentService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 动态评论
 */
@RestController
@RequestMapping("/im/post/comment")
public class PostCommentController extends BaseBackController<PostComment, IPostCommentService> {

    @RequestMapping("/pagination")
    public Result<Object> list(QPostComment q){
        return success(service.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    /**
     * 添加或更新
     */
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated PostComment item, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return service.createOrUpdate(item);
    }

    /**
     * 批量删除
     */
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return service.del(ids);
    }
}
