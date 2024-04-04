package org.jeecg.modules.im.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.PostItem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 动态项 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2024-03-20
 */
@Mapper
public interface PostItemMapper extends BaseMapper<PostItem> {

    List<PostItem> getByPostId(Integer postId);
}
