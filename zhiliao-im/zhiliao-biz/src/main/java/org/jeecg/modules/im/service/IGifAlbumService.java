package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.GifAlbum;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QGifAlbum;

import java.util.List;

/**
 * <p>
 * gif图集 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
public interface IGifAlbumService extends IService<GifAlbum> {
    IPage<GifAlbum> pagination(MyPage<GifAlbum> page, QGifAlbum q);
    List<GifAlbum> findAll(Integer serverId);
}
