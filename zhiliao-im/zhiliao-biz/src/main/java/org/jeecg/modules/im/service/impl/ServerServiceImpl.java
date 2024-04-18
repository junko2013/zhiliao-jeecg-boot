package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.entity.query_helper.QServer;
import org.jeecg.modules.im.mapper.ServerConfigMapper;
import org.jeecg.modules.im.mapper.ServerMapper;
import org.jeecg.modules.im.service.IServerService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务器 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
@Service
public class ServerServiceImpl extends BaseServiceImpl<ServerMapper, Server> implements IServerService {
    @Autowired
    private ServerMapper serverMapper;
    @Autowired
    private ServerConfigMapper serverConfigMapper;
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Override
    public IPage<Server> pagination(MyPage<Server> page, QServer q) {
        return serverMapper.pagination(page,q);
    }

    @Override
    public Server findByNo(String no) {
        String cacheKey = String.format(ConstantCache.SERVER, no);

        Server server = (Server) redisUtil.get(cacheKey);
        if (server == null) {
            server = serverMapper.findByNo(no);
            if (server != null) {
                redisUtil.set(cacheKey, server);
            }
        }
        return server;
    }

    @Override
    public Server findById(Integer id) {
        String cacheKey = String.format(ConstantCache.SERVER, id);

        Server server = (Server) redisUtil.get(cacheKey);
        if (server == null) {
            server = getById(id);
            if (server != null) {
                redisUtil.set(cacheKey, server);
            }
        }
        return server;
    }

    @Override
    public Result<Object> createOrUpdate(Server server) {
        if(server.getId()==null){
            server.setTsCreate(getDate());
            if(!save(server)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(server)){
                return fail("更新失败");
            }
            redisUtil.del(String.format(ConstantCache.SERVER, server.getId()));
        }
        return success();
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        serverMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        redisUtil.removeAll(String.format(ConstantCache.SERVER, ""));
        return success();
    }

    @Override
    public List<Server> findAll(QServer q) {
        return serverMapper.findAll(q);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveMain(Server server, List<ServerConfig> serverConfigList) {
        serverMapper.insert(server);
        if(serverConfigList!=null && !serverConfigList.isEmpty()) {
            for(ServerConfig entity:serverConfigList) {
                //外键设置
                entity.setServerId(server.getId());
                serverConfigMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateMain(Server server, List<ServerConfig> serverConfigList) {
        serverMapper.updateById(server);

        //1.先删除子表数据
        serverConfigMapper.deleteByMainId(server.getId()+"");

        //2.子表数据重新插入
        if(serverConfigList!=null && !serverConfigList.isEmpty()) {
            for(ServerConfig entity:serverConfigList) {
                //外键设置
                entity.setServerId(server.getId());
                serverConfigMapper.insert(entity);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delMain(String id) {
        serverConfigMapper.deleteByMainId(id);
        serverMapper.deleteById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delBatchMain(Collection<? extends Serializable> idList) {
        for(Serializable id:idList) {
            serverConfigMapper.deleteByMainId(id.toString());
            serverMapper.deleteById(id);
        }
    }

}
