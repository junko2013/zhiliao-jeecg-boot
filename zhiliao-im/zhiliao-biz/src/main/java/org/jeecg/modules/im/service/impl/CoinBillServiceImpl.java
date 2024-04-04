package org.jeecg.modules.im.service.impl;

import org.apache.ibatis.annotations.Mapper;
import org.jeecg.modules.im.entity.CoinBill;
import org.jeecg.modules.im.entity.query_helper.QCoinBill;
import org.jeecg.modules.im.mapper.CoinBillMapper;
import org.jeecg.modules.im.service.CoinBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 金币账变 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-02-15
 */
@Service
public class CoinBillServiceImpl extends ServiceImpl<CoinBillMapper, CoinBill> implements CoinBillService {

    @Autowired
    private CoinBillMapper coinBillMapper;
    @Override
    public Integer getAmount(QCoinBill q) {
        Integer amount =  coinBillMapper.getAmount(q);
        return amount==null?0:amount;
    }
}
