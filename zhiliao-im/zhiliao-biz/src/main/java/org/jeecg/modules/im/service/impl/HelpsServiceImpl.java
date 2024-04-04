package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.entity.Helps;
import org.jeecg.modules.im.mapper.HelpsMapper;
import org.jeecg.modules.im.service.HelpsService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 帮助 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Service
public class HelpsServiceImpl extends BaseServiceImpl<HelpsMapper, Helps> implements HelpsService {
    @Autowired
    private HelpsMapper helpsMapper;
    @Override
    public Result<Object> findAll() {
        LambdaQueryWrapper<Helps> q = new LambdaQueryWrapper<>();
        q.eq(Helps::getDelFlag, CommonConstant.DEL_FLAG_0);
        return success(list(q));
    }

    @Override
    public Result<Object> createOrUpdate(Helps helps) {
        if(helps.getId()==null){
            helps.setTsCreate(getTs());
            if(!save(helps)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(helps)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        helpsMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<Helps> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Helps> queryLogicDeleted(LambdaQueryWrapper<Helps> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Helps::getDelFlag, CommonConstant.DEL_FLAG_1);
        return helpsMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return helpsMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return helpsMapper.deleteLogicDeleted(ids)!=0;
    }
}
