package org.jeecg.modules.im.service.impl;

import org.jeecg.modules.im.entity.System;
import org.jeecg.modules.im.mapper.SystemMapper;
import org.jeecg.modules.im.service.SystemService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SystemServiceImpl extends BaseServiceImpl<SystemMapper, System> implements SystemService {
    @Autowired
    private SystemMapper mapper;

    @Override
    public System findByNo(String no) {
        return mapper.findByNo(no);
    }
}
