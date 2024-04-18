package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucMemberLevelConfig;

/**
 * <p>
 * 群组用户等级配置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface IMucMemberLevelConfigService extends IService<MucMemberLevelConfig> {
    Result<Object> createOrUpdate(MucMemberLevelConfig ctg);
    Result<Object> del(String ids);
}
