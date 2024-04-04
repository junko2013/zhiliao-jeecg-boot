package org.jeecg.modules.im.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Call;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QCall;

import java.util.List;

/**
 * <p>
 * 通话记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-12-31
 */
@Mapper
public interface CallMapper extends BaseMapper<Call> {
    MyPage<Call> pagination(MyPage<Call> pg, @Param("q") QCall q);
    List<Call> findAll(int userId);
}
