package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucInviteLink;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.MucInviteLink;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QLink;
import org.jeecg.modules.im.entity.query_helper.QMucInviteLink;

import java.util.List;

/**
 * <p>
 * 群组邀请链接 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-07-22
 */
@Mapper
public interface MucInviteLinkMapper extends BaseMapper<MucInviteLink> {
    MyPage<MucInviteLink> pagination(MyPage<MucInviteLink> pg, @Param("q") QMucInviteLink q);

    //查询逻辑删除
    List<MucInviteLink> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<MucInviteLink> wrapper);
    //还原
    int revertLogicDeleted(@Param("ids") List<String> ids);
    //彻底删除
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
