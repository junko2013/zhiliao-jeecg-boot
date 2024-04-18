package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Server;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.entity.query_helper.QServer;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 服务器 服务类
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
public interface IServerService extends IService<Server> {
    IPage<Server> pagination(MyPage<Server> page, QServer q);
    Server findByNo(String serverId);

    Result<Object> createOrUpdate(Server server);

    Server findById(Integer id);
    Result<Object> del(String ids);

    List<Server> findAll(QServer q);

    /**
     * 添加一对多
     *
     * @param server
     * @param serverConfigList
     */
    public void saveMain(Server server, List<ServerConfig> serverConfigList) ;

    /**
     * 修改一对多
     *
     * @param server
     * @param serverConfigList
     */
    public void updateMain(Server server, List<ServerConfig> serverConfigList);

    /**
     * 删除一对多
     *
     * @param id
     */
    public void delMain (String id);

    /**
     * 批量删除一对多
     *
     * @param idList
     */
    public void delBatchMain (Collection<? extends Serializable> idList);

}
