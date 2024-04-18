package org.jeecg.modules.im.service.impl;

import org.jeecg.modules.im.entity.UserLog;
import org.jeecg.modules.im.mapper.UserLogMapper;
import org.jeecg.modules.im.service.IUserLogService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户操作日志
 * @Author: jeecg-boot
 * @Date:   2024-04-17
 * @Version: V1.0
 */
@Service
public class UserLogServiceImpl extends ServiceImpl<UserLogMapper, UserLog> implements IUserLogService {

}
