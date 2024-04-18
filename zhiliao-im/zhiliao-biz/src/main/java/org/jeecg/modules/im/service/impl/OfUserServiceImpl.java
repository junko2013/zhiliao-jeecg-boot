package org.jeecg.modules.im.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.im.entity.OfUser;
import org.jeecg.modules.im.mapper.OfUserMapper;
import org.jeecg.modules.im.service.IOfUserService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * openfire用户
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Service
@Slf4j
public class OfUserServiceImpl extends BaseServiceImpl<OfUserMapper, OfUser> implements IOfUserService {

    @Autowired
    private OfUserMapper ofUserMapper;

    @Override
    public OfUser findByUsername(String username) {
        return ofUserMapper.findByUsername(username);
    }

}
