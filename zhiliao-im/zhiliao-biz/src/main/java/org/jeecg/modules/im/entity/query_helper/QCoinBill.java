package org.jeecg.modules.im.entity.query_helper;


import lombok.Data;
import org.jeecg.modules.im.entity.CoinBill;
import org.jeecg.modules.im.entity.SayHelloReply;

import java.util.List;

@Data
public class QCoinBill extends CoinBill {
    private List<String> types;
}
