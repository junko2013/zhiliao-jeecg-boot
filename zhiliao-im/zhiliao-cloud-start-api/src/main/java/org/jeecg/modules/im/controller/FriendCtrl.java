package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.configuration.ClientOperLog;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.service.FriendService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.XMPPService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 好友
 */
@RestController
@RequestMapping("/a/friend")
public class FriendCtrl extends BaseApiCtrl {
    @Resource
    private UserService userService;
    @Resource
    private FriendService friendService;
    @Resource
    private XMPPService xmppService;
    /**
     * 添加
     */
    @PostMapping("/")
    public Result<Object> add(@RequestParam int toUserId){
        return success();
    }
    /**
     * 删除某个好友
     */
    @PostMapping("/deleteOne")
    public Result<Object> deleteOne(@RequestParam int toUserId){
        return friendService.deleteOne(getCurrentUserId(),toUserId);
    }

    /**
     * 查询用户所有的好友
     */
    @ClientOperLog(module = "好友", type = "查询所有好友", desc = "")
    @PostMapping("/all")
    public Result<Object> allOfMy(){
        QFriend q = new QFriend();
        q.setUserId(getCurrentUserId());
        return success(friendService.findAll(q));
    }

    /**
     * 查询用户指定好友
     */
    @ClientOperLog(module = "好友", type = "查询某个好友", desc = "")
    @PostMapping("/one")
    public Result<Object> oneOfMy(@RequestParam Integer toUserId){
        return success(friendService.findOne(getCurrentUserId(),toUserId));
    }
}
