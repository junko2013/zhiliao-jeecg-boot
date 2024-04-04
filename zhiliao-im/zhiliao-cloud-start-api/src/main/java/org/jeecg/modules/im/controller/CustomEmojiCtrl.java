package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.service.CustomEmojiService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 自定义表情
 */
@RestController
@RequestMapping("/a/customEmoji")
public class CustomEmojiCtrl extends BaseApiCtrl {
    @Resource
    private CustomEmojiService customEmojiService;

    @RequestMapping("/all")
    public Result<Object> all(){
        return success(customEmojiService.findAll(getCurrentUserId()));
    }

}
