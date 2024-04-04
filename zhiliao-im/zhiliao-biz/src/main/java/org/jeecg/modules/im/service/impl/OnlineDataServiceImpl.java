package org.jeecg.modules.im.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.OnlineData;
import org.jeecg.modules.im.mapper.OnlineDataMapper;
import org.jeecg.modules.im.service.OnlineDataService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 在线数据 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
@Service
public class OnlineDataServiceImpl extends BaseServiceImpl<OnlineDataMapper, OnlineData> implements OnlineDataService {

    @Autowired
    private OnlineDataMapper mapper;
    @Override
    public List<OnlineData> findByDateOfServer(String date, Integer serverId) {
        return mapper.findByDateOfServer(date,serverId);
    }
}
