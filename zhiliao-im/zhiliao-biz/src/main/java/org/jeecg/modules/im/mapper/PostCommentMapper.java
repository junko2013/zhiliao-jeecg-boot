package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.PostComment;
import org.jeecg.modules.im.entity.query_helper.QPostComment;

import java.util.List;

/**
 * <p>
 * 动态评论列表 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Mapper
public interface PostCommentMapper extends BaseMapper<PostComment> {
    MyPage<PostComment> pagination(MyPage<PostComment> pg, @Param("q") QPostComment q);
    void delLogicBatch(String ids,Long tsDelete);
}
