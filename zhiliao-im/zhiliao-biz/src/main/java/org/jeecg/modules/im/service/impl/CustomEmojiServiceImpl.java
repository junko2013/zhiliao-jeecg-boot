package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ClientVer;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QClientVer;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;
import org.jeecg.modules.im.mapper.CustomEmojiMapper;
import org.jeecg.modules.im.mapper.CustomEmojiMapper;
import org.jeecg.modules.im.service.CustomEmojiService;
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
 * 自定义表情 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-21
 */
@Service
public class CustomEmojiServiceImpl extends BaseServiceImpl<CustomEmojiMapper, CustomEmoji> implements CustomEmojiService {

    @Autowired
    private CustomEmojiMapper mapper;
    @Override
    public IPage<CustomEmoji> pagination(MyPage<CustomEmoji> page, QCustomEmoji q) {
        return mapper.pagination(page,q);
    }
    @Override
    public IPage<CustomEmoji> paginationApi(MyPage<CustomEmoji> page, QCustomEmoji q) {
        return mapper.paginationApi(page, q);
    }
    @Override
    public List<CustomEmoji> findAll(Integer userId) {
        return mapper.findAll(userId);
    }

    @Override
    public Result<Object> pin(int id, long ts) {
        return success(mapper.pin(id,ts));
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        mapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<CustomEmoji> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<CustomEmoji> queryLogicDeleted(LambdaQueryWrapper<CustomEmoji> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(CustomEmoji::getDelFlag, CommonConstant.DEL_FLAG_1);
        return mapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return mapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return mapper.deleteLogicDeleted(ids)!=0;
    }
}
