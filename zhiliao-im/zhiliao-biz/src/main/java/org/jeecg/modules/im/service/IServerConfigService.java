package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.ServerConfig;

import java.util.List;

/**
 * <p>
 * 应用配置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface IServerConfigService extends IService<ServerConfig> {

    ServerConfig get(Integer serverId);
    Result<Object> updateOne(ServerConfig config);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<ServerConfig>
     */
    public List<ServerConfig> selectByMainId(String mainId);
}
