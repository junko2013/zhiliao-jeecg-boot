package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.entity.query_helper.QFeedback;
import org.jeecg.modules.im.mapper.FeedbackMapper;
import org.jeecg.modules.im.service.IFeedbackService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 意见反馈 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Service
public class FeedbackServiceImpl extends BaseServiceImpl<FeedbackMapper, Feedback> implements IFeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public IPage<Feedback> paginationApi(MyPage<Feedback> page, QFeedback q) {
        return feedbackMapper.paginationApi(page,q);
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        feedbackMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<Feedback> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Feedback> queryLogicDeleted(LambdaQueryWrapper<Feedback> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Feedback::getDelFlag, CommonConstant.DEL_FLAG_1);
        return feedbackMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return feedbackMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return feedbackMapper.deleteLogicDeleted(ids)!=0;
    }
}
