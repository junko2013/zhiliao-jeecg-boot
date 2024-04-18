package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.entity.ServerConfig;

import java.util.List;

/**
 * <p>
 * 应用配置 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-02-03
 */
@Mapper
public interface ServerConfigMapper extends BaseMapper<ServerConfig> {

    ServerConfig getDefault();
    ServerConfig get(Integer serverId);
    /**
     * 通过主表id删除子表数据
     *
     * @param mainId 主表id
     * @return boolean
     */
    public boolean deleteByMainId(@Param("mainId") String mainId);

    /**
     * 通过主表id查询子表数据
     *
     * @param mainId 主表id
     * @return List<ServerConfig>
     */
    public List<ServerConfig> selectByMainId(@Param("mainId") String mainId);
}
