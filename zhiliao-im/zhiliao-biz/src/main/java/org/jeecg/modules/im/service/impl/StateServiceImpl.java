package org.jeecg.modules.im.service.impl;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.State;
import org.jeecg.modules.im.mapper.StateMapper;
import org.jeecg.modules.im.service.IStateService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户状态 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-07-30
 */
@Service
public class StateServiceImpl extends BaseServiceImpl<StateMapper, State> implements IStateService {
    @Autowired
    private StateMapper stateMapper;
    @Override
    public Result<Object> findAll() {
        return success(stateMapper.findAll());
    }
}
