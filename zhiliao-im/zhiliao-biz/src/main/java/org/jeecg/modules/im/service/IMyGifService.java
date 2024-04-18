package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MyGif;

import java.util.List;

/**
 * <p>
 * 我的gif 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-27
 */
public interface IMyGifService extends IService<MyGif> {
    //查询用户全部的gif
    List<MyGif> findAll(Integer userId);
    Result<Object> createOrUpdate(MyGif gifAlbum);
    Result<Object> delBatch(Integer userId,String ids);
    Result<Object> addGif(Integer userId,Integer gifId);

    MyGif findByGifId(Integer userId,Integer gifId);

    Result<Object> pin(int id, long ts);
}
