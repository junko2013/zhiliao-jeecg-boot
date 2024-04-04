package org.jeecg.modules.im.service;

import org.jeecg.modules.im.entity.OnlineData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 在线数据 服务类
 * </p>
 *
 * @author junko
 * @since 2021-11-18
 */
public interface OnlineDataService extends IService<OnlineData> {
    List<OnlineData> findByDateOfServer(String date, Integer serverId);
}
