package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.ServerConfig;

/**
 * <p>
 * 应用配置 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Mapper
public interface ServerConfigMapper extends BaseMapper<ServerConfig> {

    ServerConfig getDefault();
    ServerConfig get(Integer serverId);
}
