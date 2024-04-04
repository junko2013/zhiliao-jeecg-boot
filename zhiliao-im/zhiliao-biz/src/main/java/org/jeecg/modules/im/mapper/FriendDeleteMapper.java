package org.jeecg.modules.im.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.FriendDelete;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.SayHello;
import org.jeecg.modules.im.entity.query_helper.QFriendDelete;
import org.jeecg.modules.im.entity.query_helper.QSayHello;

/**
 * <p>
 * 解除好友关系 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2024-02-25
 */
@Mapper
public interface FriendDeleteMapper extends BaseMapper<FriendDelete> {
    MyPage<FriendDelete> pagination(MyPage<FriendDelete> pg, @Param("q") QFriendDelete q);

}
