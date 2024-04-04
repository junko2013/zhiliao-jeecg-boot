package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.entity.Font;
import org.jeecg.modules.im.entity.Font;
import org.jeecg.modules.im.mapper.FontMapper;
import org.jeecg.modules.im.mapper.FontMapper;
import org.jeecg.modules.im.service.FontService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
public class FontServiceImpl extends BaseServiceImpl<FontMapper, Font> implements FontService {
    @Autowired
    private FontMapper fontMapper;
    @Override
    public Result<Object> findAll() {
        LambdaQueryWrapper<Font> q = new LambdaQueryWrapper<>();
        return success(list(q));
    }

    @Override
    public Result<Object> createOrUpdate(Font font) {
        if(font.getId()==null){
            font.setTsCreate(getTs());
            if(!save(font)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(font)){
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
        fontMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
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
