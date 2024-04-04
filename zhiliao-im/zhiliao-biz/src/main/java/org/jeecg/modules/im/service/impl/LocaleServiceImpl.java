package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.entity.Locale;
import org.jeecg.modules.im.mapper.ChatBgMapper;
import org.jeecg.modules.im.mapper.LocaleMapper;
import org.jeecg.modules.im.service.LocaleService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 语言包 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-12-18
 */
@Service
public class LocaleServiceImpl extends BaseServiceImpl<LocaleMapper, Locale> implements LocaleService {
    @Autowired
    private LocaleMapper localeMapper;
    @Override
    public List<Locale> findAll() {
        LambdaQueryWrapper<Locale> q = new LambdaQueryWrapper<>();
        return list(q);
    }

    @Override
    public String getContent(int id) {
        LambdaQueryWrapper<Locale> query = new LambdaQueryWrapper<>();
        query.eq(Locale::getStatus,CommonConstant.STATUS_1);
        query.eq(Locale::getId,id);
        query.select(Locale::getContent);
        Locale locale = getOne(query);
        if(locale==null){
            return "{}";
        }
        return locale.getContent();
    }

    @Override
    public Result<Object> createOrUpdate(Locale locale) {
        if(locale.getId()==null){
            locale.setTsCreate(getTs());
            if(!save(locale)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(locale)){
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
        localeMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<Locale> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Locale> queryLogicDeleted(LambdaQueryWrapper<Locale> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Locale::getDelFlag, CommonConstant.DEL_FLAG_1);
        return localeMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return localeMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return localeMapper.deleteLogicDeleted(ids)!=0;
    }
}
