package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucInviteLink;
import org.jeecg.modules.im.entity.MucNotice;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QMucNotice;

import java.util.List;

/**
 * <p>
 * 群公告 服务类
 * </p>
 *
 * @author junko
 * @since 2024-02-05
 */
public interface IMucNoticeService extends IService<MucNotice> {
    Result<Object> createOrUpdate(QMucNotice notice);

    List<MucNotice> findByMuc(Integer mucId);
    Result<Object> del(String ids);

    List<MucNotice> queryLogicDeleted();
    List<MucNotice> queryLogicDeleted(LambdaQueryWrapper<MucNotice> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
