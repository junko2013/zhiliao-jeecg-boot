package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucInvite;
import org.jeecg.modules.im.entity.query_helper.QMucInvite;

import java.util.List;

/**
 * <p>
 * 加群邀请 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-03-06
 */
@Mapper
public interface MucInviteMapper extends BaseMapper<MucInvite> {
    MyPage<MucInvite> paginationApi(MyPage<MucInvite> pg, @Param("q") QMucInvite q);


    MucInvite findLatestUnDeal(Integer inviter, Integer mucId, Integer invitee);

    int invalidOfUserByMuc(Integer invitee, Integer mucId);


    //查询逻辑删除
    List<MucInvite> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<MucInvite> wrapper);
    //还原
    int revertLogicDeleted(@Param("ids") List<String> ids);
    //彻底删除
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
