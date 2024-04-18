package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.entity.query_helper.QFeedback;

import java.util.List;

/**
 * <p>
 * 意见反馈 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Mapper
public interface FeedbackMapper extends BaseMapper<Feedback> {
    MyPage<Feedback> paginationApi(MyPage<Feedback> pg, @Param("q") QFeedback q);

    List<Feedback> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Feedback> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
