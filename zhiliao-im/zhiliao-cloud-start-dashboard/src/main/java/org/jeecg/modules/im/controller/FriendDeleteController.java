package org.jeecg.modules.im.controller;


import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.query_helper.QFriendDelete;
import org.jeecg.modules.im.service.FriendDeleteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@RequestMapping("/im/friendDelete")
public class FriendDeleteController extends BaseBackController {
    @Resource
    private FriendDeleteService friendDeleteService;

    @RequestMapping("/pagination")
    public Result<Object> list(QFriendDelete q){
        q.setServerId(getServerId());
        return success(friendDeleteService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }
}
