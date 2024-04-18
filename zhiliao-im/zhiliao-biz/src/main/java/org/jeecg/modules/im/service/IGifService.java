package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.query_helper.QGif;

import java.util.List;

/**
 * <p>
 * gif收藏 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
public interface IGifService extends IService<Gif> {
    IPage<Gif> paginationApi(MyPage<Gif> page, QGif q);

    Gif findByMd5(String md5);
    List<String> findHotEmojis();
}
