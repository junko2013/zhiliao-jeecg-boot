package org.jeecg.modules.im.service;

import org.jeecg.modules.im.entity.MucMsgRead;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 群消息已读记录 服务类
 * </p>
 *
 * @author junko
 * @since 2023-12-03
 */
public interface IMucMsgReadService extends IService<MucMsgRead> {
    //查询某条
    MucMsgRead getOne(Long msgId,int userId);
    //查询某条消息的阅读记录
    List<MucMsgRead> listByMsgId(Long msgId);
}
