package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.query_helper.QAdver;
import org.jeecg.modules.im.mapper.AdverMapper;
import org.jeecg.modules.im.service.IAdverService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 广告 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Service
public class AdverServiceImpl extends BaseServiceImpl<AdverMapper, Adver> implements IAdverService {

    @Autowired
    private AdverMapper adverMapper;

    @Override
    public List<Adver> findAll(QAdver q) {
        return adverMapper.findAll(q);
    }

    @Override
    public List<Adver> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Adver> queryLogicDeleted(LambdaQueryWrapper<Adver> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Adver::getDelFlag, CommonConstant.DEL_FLAG_1);
        return adverMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return adverMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return adverMapper.deleteLogicDeleted(ids)!=0;
    }

    @Override
    public Adver findLatest(Integer serverId) {
        LambdaQueryWrapper<Adver> q = new LambdaQueryWrapper<>();
        q.eq(Adver::getServerId, serverId);
        q.eq(Adver::getEnable,1);
        q.eq(Adver::getDelFlag,CommonConstant.DEL_FLAG_0);
        q.orderByDesc(Adver::getTsCreate);
        return getOne(q);
    }
}
