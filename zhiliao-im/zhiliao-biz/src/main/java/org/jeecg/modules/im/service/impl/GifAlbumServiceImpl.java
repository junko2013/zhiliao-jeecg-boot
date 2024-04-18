package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.GifAlbum;
import org.jeecg.modules.im.entity.query_helper.QGifAlbum;
import org.jeecg.modules.im.mapper.GifAlbumMapper;
import org.jeecg.modules.im.service.IGifAlbumService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * gif图集 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
@Service
public class GifAlbumServiceImpl extends BaseServiceImpl<GifAlbumMapper, GifAlbum> implements IGifAlbumService {
    @Autowired
    private GifAlbumMapper gifAlbumMapper;

    @Override
    public IPage<GifAlbum> pagination(MyPage<GifAlbum> page, QGifAlbum q) {
        return gifAlbumMapper.pagination(page, q);
    }


    @Override
    public List<GifAlbum> findAll(Integer serverId) {
        return gifAlbumMapper.findAll(serverId);
    }
}
