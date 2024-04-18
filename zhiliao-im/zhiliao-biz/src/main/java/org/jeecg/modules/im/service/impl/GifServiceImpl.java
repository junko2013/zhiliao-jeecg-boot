package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.binarywang.java.emoji.EmojiConverter;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Gif;
import org.jeecg.modules.im.entity.query_helper.QGif;
import org.jeecg.modules.im.mapper.GifMapper;
import org.jeecg.modules.im.service.IGifService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * gif收藏 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-26
 */
@Service
public class GifServiceImpl extends BaseServiceImpl<GifMapper, Gif> implements IGifService {

    @Autowired
    private GifMapper gifMapper;
    @Override
    public IPage<Gif> paginationApi(MyPage<Gif> page, QGif q) {
        return gifMapper.paginationApi(page, q);
    }

    @Override
    public Gif findByMd5(String md5) {
        return gifMapper.findByMd5(md5);
    }

    @Override
    public List<String> findHotEmojis() {
        return gifMapper.findHotEmojis();
    }
}
