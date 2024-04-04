package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.OnlineData;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 在线数据 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Mapper
public interface OnlineDataMapper extends BaseMapper<OnlineData> {

    List<OnlineData> findByDateOfServer(String date, Integer serverId);
}
