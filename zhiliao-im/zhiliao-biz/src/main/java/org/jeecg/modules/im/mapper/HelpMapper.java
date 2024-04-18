package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.entity.Help;

import java.util.List;

/**
 * <p>
 * 帮助 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-02-23
 */
@Mapper
public interface HelpMapper extends BaseMapper<Help> {

    List<Help> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Help> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
