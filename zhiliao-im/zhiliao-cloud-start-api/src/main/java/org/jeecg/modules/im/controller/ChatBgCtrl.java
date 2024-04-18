package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ChatBg;
import org.jeecg.modules.im.entity.query_helper.QChatBg;
import org.jeecg.modules.im.service.IChatBgService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * 聊天背景
 */
@RestController
@RequestMapping("/a/chatBg")
public class ChatBgCtrl extends BaseApiCtrl{
    @Resource
    private IChatBgService IChatBgService;

    @RequestMapping("/pagination")
    public Result<Object> pagination(QChatBg q){
        return success(IChatBgService.paginationApi(new MyPage<>(getPage(),getPageSize()),q));
    }

}
