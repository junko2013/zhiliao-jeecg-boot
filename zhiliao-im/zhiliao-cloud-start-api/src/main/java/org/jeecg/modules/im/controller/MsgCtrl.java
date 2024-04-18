package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.Msg;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.entity.query_helper.QMsg;
import org.jeecg.modules.im.service.IFriendService;
import org.jeecg.modules.im.service.IMsgService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 消息
 */
@RestController
@RequestMapping("/a/msg")
public class MsgCtrl extends BaseApiCtrl {
    @Resource
    private IFriendService IFriendService;
    @Resource
    private IMsgService IMsgService;

    /**
     * 分页查询
     */
    @RequestMapping("/pagination")
    public Result<Object> paginationApi(QMsg q){
        q.setUserId(getCurrentUserId());
        if(q.getAfter()){
            q.setPageSize(Integer.MAX_VALUE);
        }
        return success(IMsgService.paginationApi(q));
    }
    /**
     * 分页查询
     */
    @RequestMapping("/syncAll")
    public Result<Object> syncAll(QMsg q){
        q.setUserId(getCurrentUserId());
        //查询我的好友
        QFriend qf = new QFriend();
        qf.setUserId(getCurrentUserId());
        List<Friend> friends = IFriendService.findAll(qf);
        List<Msg> msgs = new ArrayList<>();
        for (Friend friend : friends) {
            q.setToUserId(friend.getToUser().getId());
            msgs.addAll(IMsgService.paginationApi(q));
        }
        return success(msgs);
    }
}
