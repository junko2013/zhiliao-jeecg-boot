package org.jeecg.modules.im.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.State;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 用户状态 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-07-30
 */
@Mapper
public interface StateMapper extends BaseMapper<State> {

    List<State> findAll();
}
