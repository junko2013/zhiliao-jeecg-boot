package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.CustomEmoji;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.CustomEmoji;
import org.jeecg.modules.im.entity.query_helper.QClientVer;
import org.jeecg.modules.im.entity.query_helper.QCustomEmoji;

import java.util.List;

/**
 * <p>
 * 自定义表情 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-02-21
 */
@Mapper
public interface CustomEmojiMapper extends BaseMapper<CustomEmoji> {
    MyPage<CustomEmoji> pagination(MyPage<CustomEmoji> pg, @Param("q") QCustomEmoji q);
    MyPage<CustomEmoji> paginationApi(MyPage<CustomEmoji> pg, @Param("q") QCustomEmoji q);
    List<CustomEmoji> findAll(Integer userId);

    int pin(int id, long ts);

    //查询逻辑删除
    List<CustomEmoji> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<CustomEmoji> wrapper);
    //还原
    int revertLogicDeleted(@Param("ids") List<String> ids);
    //彻底删除
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
