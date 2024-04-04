package org.jeecg.modules.im.controller;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.FriendService;
import org.jeecg.modules.im.service.UserService;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 搜索
 */
@Slf4j
@RestController
@RequestMapping("/a/search")
public class SearchCtrl extends BaseApiCtrl {

    @Resource
    private UserService userService;

    @Resource
    private FriendService friendService;

    /**
     * 查找用户
     */
    @RequestMapping("/user")
    public Result<Object> searchUser(@RequestParam String keyword){
        if(isEmpty(keyword)){
            return success();
        }
        Result<Object> result = userService.search(getCurrentUserId(),keyword, User.Type.business.getCode());
        List<User> userList = (List<User>) result.getResult();
        for (User user : userList) {
            user.setFriend(friendService.findOne(getCurrentUserId(),user.getId()));
        }
        return success(userList);
    }
    /**
     * 查找群组
     */
    @RequestMapping("/muc")
    public Result<Object> searchMuc(){
        return success();
    }
    /**
     * 查找频道
     */
    @RequestMapping("/channel")
    public Result<Object> searchChannel(){
        return success();
    }

}
