package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.constant.ConstantCache;
import org.jeecg.modules.im.base.tools.ToolDateTime;
import org.jeecg.modules.im.entity.MucMsgRead;
import org.jeecg.modules.im.mapper.MucMsgReadMapper;
import org.jeecg.modules.im.service.IMucMsgReadService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 群消息已读记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-12-03
 */
@Service
public class MucMsgReadServiceImpl extends BaseServiceImpl<MucMsgReadMapper, MucMsgRead> implements IMucMsgReadService {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public MucMsgRead getOne(Long msgId, int userId) {
        LambdaQueryWrapper<MucMsgRead> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MucMsgRead::getMsgId,msgId);
        queryWrapper.eq(MucMsgRead::getUserId,userId);
        return getOne(queryWrapper);
    }

    @Override
    public List<MucMsgRead> listByMsgId(Long msgId) {
        //先查redis
        String cacheKey = String.format(ConstantCache.MUC_MSG_READ,msgId);
        List<MucMsgRead> records = null;
        Map<Object,Object> map = redisUtil.hmget(cacheKey);
        if(!map.isEmpty()){
            records = new ArrayList<>();
            for (Object k : map.keySet()) {
                Integer userId = Integer.parseInt((String) k);
                Long tsRead = (Long) map.get(k);
                MucMsgRead read = new MucMsgRead();
                read.setMsgId(msgId);
                read.setUserId(userId);
                read.setTsRead(ToolDateTime.getDate(tsRead));
                records.add(read);
            }
            return records;
        }else{
            LambdaQueryWrapper<MucMsgRead> query = new LambdaQueryWrapper<>();
            query.eq(MucMsgRead::getMsgId,msgId);
            return list(query);
        }
    }
}
