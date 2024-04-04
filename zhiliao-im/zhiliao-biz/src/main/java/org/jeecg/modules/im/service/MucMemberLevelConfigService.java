package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevel;
import org.jeecg.modules.im.entity.MucMemberLevelConfig;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelConfig;

/**
 * <p>
 * 群组用户等级配置 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface MucMemberLevelConfigService extends IService<MucMemberLevelConfig> {
    Result<Object> createOrUpdate(MucMemberLevelConfig ctg);
    Result<Object> del(String ids);
}
