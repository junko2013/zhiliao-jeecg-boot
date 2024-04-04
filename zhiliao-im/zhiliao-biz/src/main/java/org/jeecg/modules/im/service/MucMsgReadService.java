package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
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
public interface MucMsgReadService extends IService<MucMsgRead> {
    //查询某条
    MucMsgRead getOne(String stanzaId,int userId);
    //查询某条消息的阅读记录
    List<MucMsgRead> listByMsgId(String stanzaId);
}
