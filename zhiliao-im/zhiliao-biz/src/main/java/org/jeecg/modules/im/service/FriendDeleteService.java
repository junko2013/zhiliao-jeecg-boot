package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.FriendDelete;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.query_helper.QFriendDelete;
import org.jeecg.modules.im.entity.query_helper.QSayHello;

/**
 * <p>
 * 解除好友关系 服务类
 * </p>
 *
 * @author junko
 * @since 2024-02-25
 */
public interface FriendDeleteService extends IService<FriendDelete> {
    IPage<FriendDelete> pagination(MyPage<FriendDelete> page, QFriendDelete q);

}
