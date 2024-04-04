package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.ServerConfig;

/**
 * <p>
 * 应用配置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface ServerConfigService extends IService<ServerConfig> {

    ServerConfig get(Integer serverId);
    Result<Object> updateOne(ServerConfig config);
}
