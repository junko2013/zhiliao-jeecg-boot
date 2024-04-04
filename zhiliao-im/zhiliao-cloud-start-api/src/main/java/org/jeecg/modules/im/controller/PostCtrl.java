package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QMucNotice;
import org.jeecg.modules.im.entity.query_helper.QPost;
import org.jeecg.modules.im.entity.query_helper.QRedPack;
import org.jeecg.modules.im.service.*;
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
@RequestMapping("/a/post")
public class PostCtrl extends BaseApiCtrl {

    @Resource
    private PostService postService;

    @RequestMapping("/pagination")
    public Result<Object> pagination(QPost q){
        return success(postService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer postId) {
        return success(postService.getById(postId));
    }

    /**
     * 发布
     */
    @PostMapping("/publish")
    public Result<Object> publish(QPost post){
        post.setUserId(getCurrentUserId());
        return postService.publish(post);
    }
    /**
     * 编辑
     */
    @PostMapping("/edit")
    public Result<Object> edit(QPost post){
        post.setUserId(getCurrentUserId());
        return postService.edit(post);
    }
    /**
     * 删除
     */
    @PostMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return postService.del(ids);
    }

}
