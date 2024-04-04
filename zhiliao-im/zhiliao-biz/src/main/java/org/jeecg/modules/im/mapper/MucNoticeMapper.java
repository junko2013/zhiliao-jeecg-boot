package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucNotice;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QMucNotice;

import java.util.List;

/**
 * <p>
 * 群公告 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2024-02-05
 */
@Mapper
public interface MucNoticeMapper extends BaseMapper<MucNotice> {
    MyPage<MucNotice> pagination(MyPage<MucNotice> pg, @Param("q") QMucNotice q);

    //查询逻辑删除
    List<MucNotice> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<MucNotice> wrapper);
    //还原
    int revertLogicDeleted(@Param("ids") List<String> ids);
    //彻底删除
    int deleteLogicDeleted(@Param("ids") List<String> ids);

    List<MucNotice> findAll(@Param("q") QMucNotice q);
}
