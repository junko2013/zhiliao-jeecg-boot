package org.jeecg.modules.im.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.MucMemberLevelCtg;
import org.jeecg.modules.im.entity.query_helper.QMucMemberLevelCtg;

/**
 * <p>
 * 群组用户等级分类 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2021-01-20
 */
@Mapper
public interface MucMemberLevelCtgMapper extends BaseMapper<MucMemberLevelCtg> {
}
