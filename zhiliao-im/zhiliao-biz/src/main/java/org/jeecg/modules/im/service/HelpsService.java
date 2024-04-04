package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Helps;
import org.jeecg.modules.im.entity.Helps;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 帮助 服务类
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
public interface HelpsService extends IService<Helps> {
    Result<Object> findAll();

    Result<Object> createOrUpdate(Helps adver);
    Result<Object> del(String ids);

    List<Helps> queryLogicDeleted();
    List<Helps> queryLogicDeleted(LambdaQueryWrapper<Helps> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
