package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.query_helper.QServer;
import org.jeecg.modules.im.mapper.ServerMapper;
import org.jeecg.modules.im.service.ServerService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
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
public class ServerServiceImpl extends BaseServiceImpl<ServerMapper, Server> implements ServerService {
    @Autowired
    private ServerMapper serverMapper;
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
            server.setTsCreate(getTs());
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
    public List<Server> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Server> queryLogicDeleted(LambdaQueryWrapper<Server> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Server::getDelFlag, CommonConstant.DEL_FLAG_1);
        return serverMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        boolean result = serverMapper.revertLogicDeleted(ids) > 0;
        if(result){
            redisUtil.removeAll(String.format(ConstantCache.SERVER, ""));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        boolean result = serverMapper.deleteLogicDeleted(ids)!=0;
        if(result){
            redisUtil.removeAll(String.format(ConstantCache.SERVER, ""));
        }
        return result;
    }

}
