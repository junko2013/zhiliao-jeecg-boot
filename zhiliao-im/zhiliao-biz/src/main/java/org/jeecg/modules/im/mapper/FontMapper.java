package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.entity.Font;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.Font;

import java.util.List;

/**
 * <p>
 * 应用字体 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2024-02-12
 */
@Mapper
public interface FontMapper extends BaseMapper<Font> {
    List<Font> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Font> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
