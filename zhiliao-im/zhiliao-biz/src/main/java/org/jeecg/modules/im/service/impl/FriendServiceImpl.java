package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.entity.Friend;
import org.jeecg.modules.im.entity.FriendDelete;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.entity.query_helper.QFriend;
import org.jeecg.modules.im.mapper.FriendMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 好友 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
@Slf4j
public class FriendServiceImpl extends BaseServiceImpl<FriendMapper, Friend> implements FriendService {

    @Autowired
    private FriendMapper friendMapper;
    @Resource
    private UserService userService;
    @Resource
    private ServerConfigService serverConfigService;
    @Resource
    private SayHelloService sayHelloService;
    @Resource
    private MsgService msgService;
    @Resource
    private FriendDeleteService friendDeleteService;
    @Resource
    private XMPPService xmppService;
    @Override
    public IPage<Friend> pagination(MyPage<Friend> page, QFriend q) {
        return friendMapper.pagination(page,q);
    }
    @Override
    public List<Friend> findAll(QFriend q) {
        return friendMapper.findAll(q);
    }

    @Override
    public Friend findByIdOfUser(Integer id, Integer userId) {
        Friend friend = new Friend();
        friend.setId(id);
        friend.setUserId(userId);
        return friendMapper.findByIdOfUser(friend);
    }
    @Override
    public Friend findOne(Integer userId, Integer toUserId) {
        if(Objects.equals(userId, toUserId)){
            return null;
        }
        User user = userService.findById(userId);
        Friend friend = friendMapper.findOne(userId,toUserId,user.getServerId());
        if(friend==null){
            friend = new Friend();
            friend.setUserId(userId);
            friend.setToUserId(toUserId);
            friend.setTsCreate(getTs());
            friend.setStatus(Friend.Status.Stranger.getCode());
            friend.setAddType(Friend.AddType.Not.getCode());
            friend.setRemark(userService.findById(toUserId).getNickname());
            friend.setServerId(user.getServerId());
            save(friend);
        }
        return friendMapper.findOne(userId,toUserId,user.getServerId());
    }

    @Override
    public int getCountOfUser(Integer userId) {
        LambdaQueryWrapper<Friend> wrapper = new LambdaQueryWrapper<Friend>();
        wrapper.eq(Friend::getUserId,userId).eq(Friend::getStatus,Friend.Status.Friend.getCode());
        return (int)count(wrapper);
    }
    //主动方添加的需要保存加好友请求记录
    @Override
    public Result<Object> addFriend(User user, User toUser,boolean saveSayHello) {
        Friend friend = this.findOne(user.getId(),toUser.getId());
        if(friend.getStatus()==Friend.Status.Friend.getCode()){
            return  fail("好友已存在");
        }
        friend.setIsStar(false);
        friend.setTsLastTalk(0L);
        friend.setIsUnread(false);
        friend.setAddType(Friend.AddType.System.getCode());
        friend.setIsMsgArchive(false);
        friend.setIsNoDisturb(false);
        friend.setIsHide(false);
        friend.setTsPin(0L);
        friend.setStatus(Friend.Status.Friend.getCode());
        friend.setTsFriend(getTs());
        updateById(friend);

        if(saveSayHello){
            //保存添加记录
            SayHello hello = new SayHello();
            hello.setStatus(SayHello.Status.Accept.getCode());
            hello.setFromId(user.getId());
            hello.setToId(toUser.getId());
            hello.setMsg("你好");
            hello.setTsDeal(getTs());
            hello.setIsValid(true);
            hello.setResource(Friend.AddType.System.getCode());
            hello.setTsCreate(getTs());
            hello.setServerId(getServerId());
            sayHelloService.save(hello);
            //xmpp添加好友
            xmppService.addFriend(user.getId(),toUser.getId());
            //被添加方发送添加好友通知
            MessageBean messageBean = new MessageBean();
            messageBean.setUserId(user.getId());
            messageBean.setToUserId(toUser.getId());
            messageBean.setType(MsgType.makeFriendDirectly.getType());
            xmppService.sendMsgToOne(messageBean);
        }
        return success();
    }
    //关注用户
    @Override
    public Result<Object> followUser(User user, User toUser,boolean saveSayHello) {
        Friend friend = this.findOne(user.getId(),toUser.getId());
        if(friend.getStatus()==Friend.Status.Follow.getCode()){
            return fail("已关注，请勿重复关注");
        }
        friend.setIsStar(false);
        friend.setTsLastTalk(0L);
        friend.setIsUnread(false);
        friend.setAddType(Friend.AddType.System.getCode());
        friend.setIsMsgArchive(false);
        friend.setIsNoDisturb(false);
        friend.setIsHide(false);
        friend.setTsPin(0L);
        friend.setStatus(Friend.Status.Follow.getCode());
        friend.setTsFriend(getTs());
        updateById(friend);

        if(saveSayHello){
            //保存添加记录
            SayHello hello = new SayHello();
            hello.setStatus(SayHello.Status.Accept.getCode());
            hello.setFromId(toUser.getId());
            hello.setToId(user.getId());
            hello.setMsg("欢迎关注~");
            hello.setTsDeal(getTs());
            hello.setIsValid(true);
            hello.setResource(Friend.AddType.System.getCode());
            hello.setType(SayHello.Type.FollowUser.getCode());
            hello.setTsCreate(getTs());
            sayHelloService.save(hello);
            //xmpp关注用户
            xmppService.followUser(user.getId(),toUser.getId());
        }
        return success();
    }
    //主动方添加的需要保存加好友请求记录
    @Override
    public Result<Object> consoleAddFriend(User user, User toUser,boolean saveSayHello) {
        Friend friend = this.findOne(user.getId(),toUser.getId());
        if(friend.getStatus()==Friend.Status.Friend.getCode()){
            return  fail("好友已存在");
        }
        friend.setIsStar(false);
        friend.setTsLastTalk(0L);
        friend.setIsUnread(false);
        friend.setAddType(Friend.AddType.System.getCode());
        friend.setIsMsgArchive(false);
        friend.setIsNoDisturb(false);
        friend.setIsHide(false);
        friend.setTsPin(0L);
        friend.setStatus(Friend.Status.Friend.getCode());
        friend.setTsFriend(getTs());
        updateById(friend);

        if(saveSayHello) {
            //保存添加记录
            SayHello hello = new SayHello();
            hello.setStatus(SayHello.Status.Accept.getCode());
            hello.setFromId(user.getId());
            hello.setToId(toUser.getId());
            hello.setMsg("系统添加好友");
            hello.setTsDeal(getTs());
            hello.setIsValid(true);
            hello.setServerId(user.getServerId());
            hello.setResource(Friend.AddType.System.getCode());
            sayHelloService.save(hello);
            //xmpp添加好友
            xmppService.addFriend(user.getId(), toUser.getId());
        }
        //系统通知双方
        MessageBean messageBean = new MessageBean();
        messageBean.setToUserId(toUser.getId());
        messageBean.setType(MsgType.makeFriendDirectly.getType());
        xmppService.sendMsgBySys(messageBean);
        return success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<Object> deleteOne(Integer userId, Integer toUserId) {
        Friend friend = this.findOne(userId,toUserId);
        if(friend==null||friend.getStatus()!=Friend.Status.Friend.getCode()){
            return  fail("好友不存在");
        }
        try {
            //双向删除
            //将好友关系更改为已删除
            friend.setStatus(Friend.Status.Delete.getCode());
            friend.setIsStar(false);
            friend.setTsFriend(0L);
            friend.setTsLastTalk(0L);
            friend.setIsUnread(false);
            friend.setAddType(Friend.AddType.Not.getCode());
            friend.setIsMsgArchive(false);
            friend.setIsNoDisturb(false);
            friend.setIsHide(false);
            friend.setTsPin(0L);
            updateById(friend);
            Friend toFriend = this.findOne(toUserId, userId);
            toFriend.setStatus(Friend.Status.Delete.getCode());
            toFriend.setIsStar(false);
            toFriend.setTsFriend(0L);
            toFriend.setTsLastTalk(0L);
            toFriend.setIsUnread(false);
            toFriend.setAddType(Friend.AddType.Not.getCode());
            toFriend.setIsMsgArchive(false);
            toFriend.setIsNoDisturb(false);
            toFriend.setIsHide(false);
            toFriend.setTsPin(0L);
            updateById(toFriend);
            //删除单聊消息记录
            msgService.deleteLogic(userId,toUserId);
            msgService.deleteLogic(toUserId,userId);
//            //系统通知对方已被好友删除
//            User toUser = userService.findById(toUserId);
//            MessageBean messageBean = new MessageBean();
//            messageBean.setToUserId(toUser.getId());
//            messageBean.setToUserName(toUser.getNickname());
//            messageBean.setType(MsgType.del.getType());
//            xmppService.sendMsgBySys(messageBean);
            //保存删除记录
            FriendDelete delete = new FriendDelete();
            delete.setFromId(userId);
            delete.setToId(toUserId);
            delete.setTsCreate(getTs());
            delete.setServerId(friend.getServerId());
            friendDeleteService.save(delete);
            return success(delete);
        }catch (Exception e){
            log.error("删除好友失败：userId={},toUserId={}", userId,toUserId, e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            if (e instanceof BusinessException) {
                return fail(e.getMessage());
            }
            return fail("删除好友失败");
        }
    }
}
