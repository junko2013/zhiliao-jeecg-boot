package org.jeecg.modules.im.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.MucMsgRead;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 群消息已读记录 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2023-12-03
 */
@Mapper
public interface MucMsgReadMapper extends BaseMapper<MucMsgRead> {

}
