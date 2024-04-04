package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.Sticker;
import org.jeecg.modules.im.entity.query_helper.QSticker;
import org.jeecg.modules.im.mapper.StickerMapper;
import org.jeecg.modules.im.service.StickerService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 贴纸 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-27
 */
@Service
public class StickerServiceImpl extends BaseServiceImpl<StickerMapper, Sticker> implements StickerService {

    @Autowired
    private StickerMapper stickerMapper;
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Override
    public IPage<Sticker> pagination(MyPage<Sticker> page, QSticker q) {
        return stickerMapper.pagination(page,q);
    }

    @Override
    public Result<Object> createOrUpdate(Integer userId,Sticker sticker) {
        if(sticker.getId()==null){
            if(findByName(sticker.getName())!=null){
                return fail("名称已存在");
            }
            sticker.setTsCreate(getTs());
            sticker.setUserId(userId);
            if(!save(sticker)){
                return fail("添加失败");
            }
        }else{
            Sticker old = findByName(sticker.getName());
            if(old!=null&&!old.getId().equals(sticker.getId())){
                return fail("名称已存在");
            }
            if(!updateById(sticker)){
                return fail("更新失败");
            }
        }
        return success();
    }

    @Override
    public IPage<Sticker> paginationApi(MyPage<Sticker> page, QSticker q) {
        return stickerMapper.paginationApi(page,q);
    }

    @Override
    public Sticker findById(Integer id) {
        return stickerMapper.findById(id);
    }

    @Override
    public Sticker getBigEmoji() {
        return stickerMapper.getBigEmoji();
    }

    @Override
    public List<Sticker> findAll() {
        return stickerMapper.findAll();
    }
    @Override
    public List<Sticker> getHot() {
        return stickerMapper.getHot();
    }
    @Override
    public List<Sticker> getEmojis() {
        return stickerMapper.getEmojis();
    }

    @Override
    public Sticker findByName(String name) {
        return stickerMapper.findByName(name);
    }

    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        stickerMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<Sticker> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<Sticker> queryLogicDeleted(LambdaQueryWrapper<Sticker> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(Sticker::getDelFlag, CommonConstant.DEL_FLAG_1);
        return stickerMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        boolean result = stickerMapper.revertLogicDeleted(ids) > 0;
        if(result){
            redisUtil.removeAll(String.format(ConstantCache.SERVER, ""));
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        boolean result = stickerMapper.deleteLogicDeleted(ids)!=0;
        if(result){
            redisUtil.removeAll(String.format(ConstantCache.SERVER, ""));
        }
        return result;
    }

}
