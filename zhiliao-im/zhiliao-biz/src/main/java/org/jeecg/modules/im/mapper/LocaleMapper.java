package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.entity.Locale;
import org.jeecg.modules.im.entity.Locale;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 语言包 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-12-18
 */
@Mapper
public interface LocaleMapper extends BaseMapper<Locale> {
    List<Locale> selectLogicDeleted(@Param(Constants.WRAPPER) Wrapper<Locale> wrapper);
    int revertLogicDeleted(@Param("ids") List<String> ids);
    int deleteLogicDeleted(@Param("ids") List<String> ids);
}
