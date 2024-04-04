package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ChatBg;
import org.jeecg.modules.im.entity.query_helper.QChatBg;

import java.util.List;

/**
 * <p>
 * 聊天背景 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
@Mapper
public interface ChatBgMapper extends BaseMapper<ChatBg> {
    MyPage<ChatBg> paginationApi(MyPage<ChatBg> pg, @Param("q") QChatBg q);
    List<ChatBg> findAll();

    List<ChatBg> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<ChatBg> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
