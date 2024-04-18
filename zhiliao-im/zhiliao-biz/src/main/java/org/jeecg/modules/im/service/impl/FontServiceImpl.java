package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.entity.Font;
import org.jeecg.modules.im.mapper.FontMapper;
import org.jeecg.modules.im.service.IFontService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 应用字体 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-02-12
 */
@Service
public class FontServiceImpl extends BaseServiceImpl<FontMapper, Font> implements IFontService {
    @Autowired
    private FontMapper fontMapper;
    @Override
    public Result<Object> findAll() {
        LambdaQueryWrapper<Font> q = new LambdaQueryWrapper<>();
        return success(list(q));
    }


    @Override
    public List<Font> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Font> queryLogicDeleted(LambdaQueryWrapper<Font> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Font::getDelFlag, CommonConstant.DEL_FLAG_1);
        return fontMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return fontMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return fontMapper.deleteLogicDeleted(ids)!=0;
    }

}
