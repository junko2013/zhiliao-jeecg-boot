package org.jeecg.modules.im.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.mapper.ServerConfigMapper;
import org.jeecg.modules.im.service.IServerConfigService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 应用配置 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
public class ServerConfigServiceImpl extends BaseServiceImpl<ServerConfigMapper, ServerConfig> implements IServerConfigService {

    @Autowired
    private ServerConfigMapper serverConfigMapper;
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Override
    public ServerConfig get(Integer serverId) {
        if(serverId==null){
            serverId = 0;
        }
        String cacheKey = String.format(ConstantCache.SERVER_CONFIG, serverId);

        ServerConfig config = (ServerConfig) redisUtil.get(cacheKey);
        if (config == null) {
            if(serverId==0){
                config = serverConfigMapper.getDefault();
            }else{
                config = serverConfigMapper.get(serverId);
            }
            if (config != null) {
                redisUtil.set(cacheKey, config);
            }
        }
        return config;
    }

    @Override
    public Result<Object> updateOne(ServerConfig config) {
        if(updateById(config)){
            //修改默认的
            Integer serverId = config.getServerId();
            if(serverId==null){
                serverId = 0;
            }
            String cacheKey = String.format(ConstantCache.SERVER_CONFIG, serverId);
            redisUtil.set(cacheKey, config);
            return success();
        }
        return fail();
    }

    @Override
    public List<ServerConfig> selectByMainId(String mainId) {
        return serverConfigMapper.selectByMainId(mainId);
    }


}
