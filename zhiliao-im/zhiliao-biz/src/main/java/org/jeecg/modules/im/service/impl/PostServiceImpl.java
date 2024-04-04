package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.query_helper.QPost;
import org.jeecg.modules.im.mapper.PostMapper;
import org.jeecg.modules.im.service.PostService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 动态 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<PostMapper, Post> implements PostService {
    @Autowired
    private PostMapper mapper;

    @Override
    public IPage<Post> pagination(MyPage<Post> page, QPost q) {
        return mapper.pagination(page,q);
    }

    @Override
    public Result<Object> publish(Post item) {
        item.setTsCreate(getTs());
        if(!save(item)){
            return fail("添加失败");
        }
        return success();
    }
    @Override
    public Result<Object> edit(Post item) {
        if(!updateById(item)){
            return fail("更新失败");
        }
        return success();
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
    public List<Post> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Post> queryLogicDeleted(LambdaQueryWrapper<Post> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Post::getDelFlag, CommonConstant.DEL_FLAG_1);
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
