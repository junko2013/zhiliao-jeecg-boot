package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ChatBg;
import org.jeecg.modules.im.entity.query_helper.QChatBg;
import org.jeecg.modules.im.mapper.ChatBgMapper;
import org.jeecg.modules.im.service.IChatBgService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 聊天背景 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
@Service
public class ChatBgServiceImpl extends BaseServiceImpl<ChatBgMapper, ChatBg> implements IChatBgService {
    @Autowired
    private ChatBgMapper chatBgMapper;

    @Override
    public IPage<ChatBg> paginationApi(MyPage<ChatBg> page, QChatBg q) {
        return chatBgMapper.paginationApi(page,q);
    }

    @Override
    public List<ChatBg> findAll() {
        return chatBgMapper.findAll();
    }


    @Override
    public List<ChatBg> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<ChatBg> queryLogicDeleted(LambdaQueryWrapper<ChatBg> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(ChatBg::getDelFlag, CommonConstant.DEL_FLAG_1);
        return chatBgMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return chatBgMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return chatBgMapper.deleteLogicDeleted(ids)!=0;
    }
}
