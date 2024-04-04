package org.jeecg.modules.im.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.SayHelloReply;
import org.jeecg.modules.im.entity.query_helper.QSayHelloReply;
import org.jeecg.modules.im.service.SayHelloReplyService;
import org.jeecg.modules.im.service.SayHelloService;
import org.jeecg.modules.im.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/sayHelloReply")
public class SayHelloReplyController extends BaseBackController {
    @Resource
    private UserService userService;
    @Resource
    private SayHelloReplyService sayHelloReplyService;
    @Resource
    private SayHelloService sayHelloService;

    @RequestMapping("/pagination")
    public Result<Object> list(QSayHelloReply q){
        IPage<SayHelloReply> replyPage = sayHelloReplyService.pagination(new MyPage<>(getPage(),getPageSize()),q);
        return success(replyPage);
    }
}
