package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.Server;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QServer;

import java.util.List;

/**
 * <p>
 * 服务器 服务类
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
public interface ServerService extends IService<Server> {
    IPage<Server> pagination(MyPage<Server> page, QServer q);
    Server findByNo(String serverId);

    Result<Object> createOrUpdate(Server server);

    Server findById(Integer id);
    Result<Object> del(String ids);

    List<Server> findAll(QServer q);

    List<Server> queryLogicDeleted();
    List<Server> queryLogicDeleted(LambdaQueryWrapper<Server> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
