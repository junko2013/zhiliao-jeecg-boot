package org.jeecg.modules.im.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.jeecg.modules.im.entity.CoinBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.im.entity.query_helper.QCoinBill;

/**
 * <p>
 * 金币账变 Mapper 接口
 * </p>
 *
 * @author junko
 * @since 2024-02-15
 */
@Mapper
public interface CoinBillMapper extends BaseMapper<CoinBill> {

    Integer getAmount(@Param("q") QCoinBill q);
}
