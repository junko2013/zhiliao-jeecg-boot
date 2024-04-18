package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.entity.Help;
import org.jeecg.modules.im.mapper.HelpMapper;
import org.jeecg.modules.im.service.IHelpService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class HelpServiceImpl extends BaseServiceImpl<HelpMapper, Help> implements IHelpService {
    @Autowired
    private HelpMapper helpMapper;
    @Override
    public Result<Object> findAll() {
        LambdaQueryWrapper<Help> q = new LambdaQueryWrapper<>();
        q.eq(Help::getDelFlag, CommonConstant.DEL_FLAG_0);
        return success(list(q));
    }

    @Override
    public List<Help> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Help> queryLogicDeleted(LambdaQueryWrapper<Help> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Help::getDelFlag, CommonConstant.DEL_FLAG_1);
        return helpMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return helpMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return helpMapper.deleteLogicDeleted(ids)!=0;
    }
}
