package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Feedback;
import org.jeecg.modules.im.entity.query_helper.QFeedback;

import java.util.List;

/**
 * <p>
 * 意见反馈 服务类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
public interface IFeedbackService extends IService<Feedback> {
    IPage<Feedback> paginationApi(MyPage<Feedback> page, QFeedback q);

    Result<Object> del(String ids);

    List<Feedback> queryLogicDeleted();
    List<Feedback> queryLogicDeleted(LambdaQueryWrapper<Feedback> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);

}
