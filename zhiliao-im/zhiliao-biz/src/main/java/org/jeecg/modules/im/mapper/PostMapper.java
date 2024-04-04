package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.Post;
import org.jeecg.modules.im.entity.query_helper.QPost;

import java.util.List;

/**
 * <p>
 * 动态 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-13
 */
@Mapper
public interface PostMapper extends BaseMapper<Post> {
    List<Post> findAll();
    MyPage<Post> pagination(MyPage<Post> pg, @Param("q") QPost q);

    List<Post> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Post> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
