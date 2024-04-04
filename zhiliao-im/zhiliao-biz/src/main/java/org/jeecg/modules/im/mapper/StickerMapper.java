package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;

import java.util.List;

/**
 * <p>
 * 贴纸 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Mapper
public interface StickerMapper extends BaseMapper<Sticker> {
    MyPage<Sticker> pagination(MyPage<Sticker> pg, @Param("q") QSticker q);
    MyPage<Sticker> paginationApi(MyPage<Sticker> pg, @Param("q") QSticker q);

    Sticker findById(Integer id);
    Sticker findByName(String name);
    List<Sticker> findAll();

    Sticker getBigEmoji();

    List<Sticker> getHot();
    List<Sticker> getEmojis();

    List<Sticker> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Sticker> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
