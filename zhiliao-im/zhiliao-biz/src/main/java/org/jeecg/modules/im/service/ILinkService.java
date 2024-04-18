package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QLink;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
public interface ILinkService extends IService<Link> {

    List<Link> findByServerId(Integer serverId);
}
