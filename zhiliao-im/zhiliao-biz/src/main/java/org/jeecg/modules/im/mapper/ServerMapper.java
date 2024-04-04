package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Server;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QServer;

import java.util.List;

/**
 * <p>
 * 服务器 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2024-01-07
 */
@Mapper
public interface ServerMapper extends BaseMapper<Server> {
    MyPage<Server> pagination(MyPage<Server> pg, @Param("q") QServer q);
    Server findByNo(@Param("no")String no);

    List<Server> findAll( @Param("q") QServer q);

    List<Server> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Server> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
