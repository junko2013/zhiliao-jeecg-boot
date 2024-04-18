package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.System;

public interface ISystemService extends IService<System> {
    System findByNo(String no);

}
