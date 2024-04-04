package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QPost;

import java.util.List;

/**
 * <p>
 * 动态 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
public interface PostService extends IService<Post> {
    IPage<Post> pagination(MyPage<Post> page, QPost q);
    Result<Object> publish(Post item);
    Result<Object> edit(Post item);
    Result<Object> del(String ids);

    List<Post> queryLogicDeleted();
    List<Post> queryLogicDeleted(LambdaQueryWrapper<Post> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
