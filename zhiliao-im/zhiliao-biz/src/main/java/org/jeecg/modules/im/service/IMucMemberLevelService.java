package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucMemberLevel;

/**
 * <p>
 * 群组用户等级 服务类
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
public interface IMucMemberLevelService extends IService<MucMemberLevel> {
    Result<Object> createOrUpdate(MucMemberLevel ctg);
    Result<Object> del(String ids);

}
