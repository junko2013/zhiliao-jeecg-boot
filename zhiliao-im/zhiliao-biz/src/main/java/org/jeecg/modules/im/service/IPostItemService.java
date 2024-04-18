package org.jeecg.modules.im.service;

import org.jeecg.modules.im.entity.PostItem;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 动态项 服务类
 * </p>
 *
 * @author junko
 * @since 2024-03-20
 */
public interface IPostItemService extends IService<PostItem> {
    List<PostItem> getByPostId(Integer postId);
}
