package org.jeecg.modules.im.service.impl;

import org.jeecg.modules.im.entity.PostItem;
import org.jeecg.modules.im.mapper.PostItemMapper;
import org.jeecg.modules.im.service.IPostItemService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 动态项 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-03-20
 */
@Service
public class PostItemServiceImpl extends BaseServiceImpl<PostItemMapper, PostItem> implements IPostItemService {

    @Autowired
    private PostItemMapper mapper;
    @Override
    public List<PostItem> getByPostId(Integer postId) {
        return mapper.getByPostId(postId);
    }
}
