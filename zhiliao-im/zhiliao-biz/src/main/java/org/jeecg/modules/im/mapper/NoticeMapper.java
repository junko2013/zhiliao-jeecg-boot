package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.query_helper.QNotice;

import java.util.List;

/**
 * <p>
 * 公告 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Mapper
public interface NoticeMapper extends BaseMapper<Notice> {
    List<Notice> findAll(@Param("q") QNotice q);

    List<Notice> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Notice> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
