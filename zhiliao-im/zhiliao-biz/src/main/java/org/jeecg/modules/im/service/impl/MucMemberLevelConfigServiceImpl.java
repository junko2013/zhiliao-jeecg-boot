package org.jeecg.modules.im.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucMemberLevelConfig;
import org.jeecg.modules.im.mapper.MucMemberLevelConfigMapper;
import org.jeecg.modules.im.service.IMucMemberLevelConfigService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * <p>
 * 群组用户等级配置 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Service
public class MucMemberLevelConfigServiceImpl extends BaseServiceImpl<MucMemberLevelConfigMapper, MucMemberLevelConfig> implements IMucMemberLevelConfigService {
    @Autowired
    private MucMemberLevelConfigMapper mucMemberLevelConfigMapper;
    @Override
    public Result<Object> createOrUpdate(MucMemberLevelConfig config) {
        if(config.getId()!=null){
            return success(updateById(config));
        }
        return success(save(config));
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        mucMemberLevelConfigMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }
}
