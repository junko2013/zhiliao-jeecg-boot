package org.jeecg.modules.im.service.impl;

import org.jeecg.modules.im.entity.MucMember;
import org.jeecg.modules.im.entity.UserInfo;
import org.jeecg.modules.im.mapper.UserInfoMapper;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户信息 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-09-21
 */
@Service
public class UserInfoServiceImpl extends BaseServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;
    @Resource
    private IMucService IMucService;
    @Resource
    private IFriendService IFriendService;
    @Resource
    private IContactService IContactService;
    @Resource
    private IDeviceService IDeviceService;
    @Override
    public UserInfo findBasicByUserId(Integer userId) {
        return userInfoMapper.findByUserId(userId);
    }
    @Override
    public UserInfo findByUserId(Integer userId) {
        UserInfo info = userInfoMapper.findByUserId(userId);
        info.setMucCreate(IMucService.getCountOfRole(userId, MucMember.Role.Master));
        info.setMucManage(IMucService.getCountOfRole(userId, MucMember.Role.Manager));
        info.setMucJoin(IMucService.getCountOfRole(userId, MucMember.Role.Member));
        info.setFriendCount(IFriendService.getCountOfUser(userId));
        info.setDeviceCount(IDeviceService.getCount(userId,null));
        info.setContactCount(IContactService.getCountOfUser(userId));
        return info;
    }
}
