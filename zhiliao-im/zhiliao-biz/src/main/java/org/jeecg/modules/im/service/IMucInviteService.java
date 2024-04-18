package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucInvite;
import org.jeecg.modules.im.entity.query_helper.QMucInvite;

import java.util.List;

/**
 * <p>
 * 加群邀请 服务类
 * </p>
 *
 * @author junko
 * @since 2021-03-06
 */
public interface IMucInviteService extends IService<MucInvite> {
    IPage<MucInvite> paginationApi(MyPage<MucInvite> page, QMucInvite q);

    //邀请人发送进群邀请
    Result<Object> send(Integer fromId, Integer mucId, Integer toId);
    //被邀请人直接通过
    Result<Object> passBySelf(Integer toId,Integer fromId, Integer mucId);
    //查询A邀请B进C群的邀请
    MucInvite findLatestUnDeal(Integer fromId, Integer mucId, Integer toId);
    //批量邀请
    Result<Object> inviteBatch(Integer userId, Integer mucId, String userIds);

    int invalidOfUserByMuc(Integer userId, Integer mucId);

    Result<Object> del(String ids);

    List<MucInvite> queryLogicDeleted();
    List<MucInvite> queryLogicDeleted(LambdaQueryWrapper<MucInvite> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
