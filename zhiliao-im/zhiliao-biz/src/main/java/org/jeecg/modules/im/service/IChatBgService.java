package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ChatBg;
import org.jeecg.modules.im.entity.query_helper.QChatBg;

import java.util.List;

/**
 * <p>
 * 聊天背景 服务类
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
public interface IChatBgService extends IService<ChatBg> {
    IPage<ChatBg> paginationApi(MyPage<ChatBg> page, QChatBg q);
    List<ChatBg> findAll();

    List<ChatBg> queryLogicDeleted();
    List<ChatBg> queryLogicDeleted(LambdaQueryWrapper<ChatBg> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
