package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.query_helper.QGif;

import java.util.List;

/**
 * <p>
 * gif收藏 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Mapper
public interface GifMapper extends BaseMapper<Gif> {
    MyPage<Gif> pagination(MyPage<Gif> pg, @Param("q") QGif q);
    MyPage<Gif> paginationApi(MyPage<Gif> pg, @Param("q") QGif q);

    Gif findByMd5(String md5);
    List<String> findHotEmojis();
}
