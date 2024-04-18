package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.query_helper.QAdver;

import java.util.List;

/**
 * <p>
 * 广告 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
public interface IAdverService extends IService<Adver> {
    List<Adver> findAll(QAdver q);

    List<Adver> queryLogicDeleted();
    List<Adver> queryLogicDeleted(LambdaQueryWrapper<Adver> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);

    Adver findLatest(Integer serverId);
}
