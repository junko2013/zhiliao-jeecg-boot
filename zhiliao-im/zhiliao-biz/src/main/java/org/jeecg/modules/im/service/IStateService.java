package org.jeecg.modules.im.service;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.State;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户状态 服务类
 * </p>
 *
 * @author junko
 * @since 2023-07-30
 */
public interface IStateService extends IService<State> {
    Result<Object> findAll();
}
