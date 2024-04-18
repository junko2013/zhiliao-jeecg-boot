package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.entity.MucInviteLink;
import org.jeecg.modules.im.mapper.MucInviteLinkMapper;
import org.jeecg.modules.im.service.IMucInviteLinkService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 群组邀请链接 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-07-22
 */
@Service
public class MucInviteLinkServiceImpl extends BaseServiceImpl<MucInviteLinkMapper, MucInviteLink> implements IMucInviteLinkService {
    @Autowired
    private MucInviteLinkMapper linkMapper;

    @Override
    public Result<Object> createOrUpdate(MucInviteLink link) {
        if(link.getId()==null){
            link.setTsCreate(getDate());
            if(!save(link)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(link)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public MucInviteLink findById(String id) {
        return linkMapper.selectById(id);
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        linkMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<MucInviteLink> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<MucInviteLink> queryLogicDeleted(LambdaQueryWrapper<MucInviteLink> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(MucInviteLink::getDelFlag, CommonConstant.DEL_FLAG_1);
        return linkMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return linkMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return linkMapper.deleteLogicDeleted(ids)!=0;
    }

    @Override
    public List<MucInviteLink> findByMuc(Integer mucId) {
        LambdaQueryWrapper<MucInviteLink> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MucInviteLink::getMucId, mucId);
        return list(wrapper);
    }
}
