package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.PostComment;
import org.jeecg.modules.im.entity.query_helper.QPostComment;
import org.jeecg.modules.im.mapper.PostCommentMapper;
import org.jeecg.modules.im.service.IPostCommentService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 动态评论列表 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Service
public class PostCommentServiceImpl extends BaseServiceImpl<PostCommentMapper, PostComment> implements IPostCommentService {
    @Autowired
    private PostCommentMapper mapper;

    @Override
    public IPage<PostComment> pagination(MyPage<PostComment> page, QPostComment q) {
        return mapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(PostComment item) {
        if(item.getId()==null){
            item.setTsCreate(getTs());
            if(!save(item)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(item)){
                return fail("更新失败");
            }
        }
        return success();
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        mapper.delLogicBatch(ids,getTs());
        return success();
    }
}
