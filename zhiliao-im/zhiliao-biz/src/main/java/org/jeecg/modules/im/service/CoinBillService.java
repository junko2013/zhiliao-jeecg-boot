package org.jeecg.modules.im.service;

import org.jeecg.modules.im.entity.CoinBill;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QCoinBill;

/**
 * <p>
 * 金币账变 服务类
 * </p>
 *
 * @author junko
 * @since 2024-02-15
 */
public interface CoinBillService extends IService<CoinBill> {
    //获取金额
    Integer getAmount(QCoinBill q);
}
