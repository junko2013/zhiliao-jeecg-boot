package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.entity.query_helper.QAdver;
import org.jeecg.modules.im.entity.query_helper.QAdver;

import java.util.List;

/**
 * <p>
 * 广告 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-19
 */
@Mapper
public interface AdverMapper extends BaseMapper<Adver> {
    List<Adver> findAll(@Param("q") QAdver q);

    List<Adver> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Adver> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
