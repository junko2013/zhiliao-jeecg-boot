package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.FriendDelete;
import org.jeecg.modules.im.entity.query_helper.QFriendDelete;
import org.jeecg.modules.im.mapper.FriendDeleteMapper;
import org.jeecg.modules.im.service.IFriendDeleteService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 解除好友关系 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-02-25
 */
@Service
public class FriendDeleteServiceImpl extends BaseServiceImpl<FriendDeleteMapper, FriendDelete> implements IFriendDeleteService {
    @Autowired
    private FriendDeleteMapper friendDeleteMapper;
    @Override
    public IPage<FriendDelete> pagination(MyPage<FriendDelete> page, QFriendDelete q) {
        return friendDeleteMapper.pagination(page,q);
    }
}
