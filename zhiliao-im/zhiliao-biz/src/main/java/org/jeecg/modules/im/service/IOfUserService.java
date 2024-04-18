package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.OfUser;

/**
 * <p>
 * 用户的标签 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
public interface IOfUserService extends IService<OfUser> {

    OfUser findByUsername(String username);
}
