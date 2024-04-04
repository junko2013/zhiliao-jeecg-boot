package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.System;

public interface SystemService extends IService<System> {
    System findByNo(String no);

}
