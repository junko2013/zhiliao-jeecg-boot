package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.PostComment;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QPostComment;

/**
 * <p>
 * 动态评论列表 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
public interface PostCommentService extends IService<PostComment> {
    IPage<PostComment> pagination(MyPage<PostComment> page, QPostComment q);
    Result<Object> createOrUpdate(PostComment item);
    Result<Object> del(String ids);
}
