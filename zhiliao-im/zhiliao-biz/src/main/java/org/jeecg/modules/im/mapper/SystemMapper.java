package org.jeecg.modules.im.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.entity.System;

@Mapper
public interface SystemMapper extends BaseMapper<System> {
    System findByNo(@Param("no")String no);

}
