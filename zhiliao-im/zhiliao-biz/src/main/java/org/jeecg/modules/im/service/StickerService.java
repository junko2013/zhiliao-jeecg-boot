package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;

import java.util.List;

/**
 * <p>
 * 贴纸 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
public interface StickerService extends IService<Sticker> {
    IPage<Sticker> pagination(MyPage<Sticker> page, QSticker q);
    IPage<Sticker> paginationApi(MyPage<Sticker> page, QSticker q);
    Result<Object> createOrUpdate(Integer userId,Sticker sticker);

    Sticker findById(Integer id);
    Sticker getBigEmoji();
    List<Sticker> findAll();
    List<Sticker> getHot();
    List<Sticker> getEmojis();
    Sticker findByName(String name);

    Result<Object> del(String ids);

    List<Sticker> queryLogicDeleted();
    List<Sticker> queryLogicDeleted(LambdaQueryWrapper<Sticker> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
