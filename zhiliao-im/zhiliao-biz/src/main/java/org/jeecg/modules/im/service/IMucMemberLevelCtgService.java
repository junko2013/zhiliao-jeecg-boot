package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;

/**
 * <p>
 * 群组用户等级分类 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface IMucMemberLevelCtgService extends IService<MucMemberLevelCtg> {
    Result<Object> createOrUpdate(MucMemberLevelCtg ctg);
    Result<Object> del(String ids);
}
