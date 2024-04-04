package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Notice;
import org.jeecg.modules.im.entity.query_helper.QNotice;

import java.util.List;

/**
 * <p>
 * 公告 服务类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
public interface NoticeService extends IService<Notice> {
    Result<Object> createOrUpdate(Notice notice);
    Result<Object> del(String ids);

    //逻辑删除相关
    List<Notice> queryLogicDeleted();
    List<Notice> queryLogicDeleted(LambdaQueryWrapper<Notice> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
    //查询用户的
    List<Notice> findAll(QNotice q);
}
