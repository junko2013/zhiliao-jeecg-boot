package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.Help;
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
public interface IHelpService extends IService<Help> {
    Result<Object> findAll();


    List<Help> queryLogicDeleted();
    List<Help> queryLogicDeleted(LambdaQueryWrapper<Help> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
