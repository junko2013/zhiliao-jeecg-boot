package org.jeecg.modules.im.service.impl;

import org.jeecg.modules.im.entity.UserState;
import org.jeecg.modules.im.mapper.UserStateMapper;
import org.jeecg.modules.im.service.IUserStateService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户状态历史 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-07-30
 */
@Service
public class UserStateServiceImpl extends ServiceImpl<UserStateMapper, UserState> implements IUserStateService {

}
