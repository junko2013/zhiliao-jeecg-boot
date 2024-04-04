package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.query_helper.QNotice;
import org.jeecg.modules.im.entity.query_helper.QNotice;
import org.jeecg.modules.im.mapper.NoticeMapper;
import org.jeecg.modules.im.mapper.NoticeMapper;
import org.jeecg.modules.im.mapper.NoticeMapper;
import org.jeecg.modules.im.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 公告 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Service
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Autowired
    private NoticeMapper noticeMapper;

    @Override
    public Result<Object> createOrUpdate(Notice notice) {
        if(notice.getId()==null){
            notice.setTsCreate(getTs());
            if(!save(notice)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(notice)){
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
        noticeMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<Notice> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Notice> queryLogicDeleted(LambdaQueryWrapper<Notice> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Notice::getDelFlag, CommonConstant.DEL_FLAG_1);
        return noticeMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return noticeMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return noticeMapper.deleteLogicDeleted(ids)!=0;
    }

    @Override
    public List<Notice> findAll(QNotice q) {
        return noticeMapper.findAll(q);
    }
}
