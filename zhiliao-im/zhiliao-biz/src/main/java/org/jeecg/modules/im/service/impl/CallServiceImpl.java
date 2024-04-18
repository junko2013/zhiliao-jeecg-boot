package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Call;
import org.jeecg.modules.im.entity.query_helper.QCall;
import org.jeecg.modules.im.mapper.CallMapper;
import org.jeecg.modules.im.service.ICallService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 通话记录 服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-12-31
 */
@Service
public class CallServiceImpl extends BaseServiceImpl<CallMapper, Call> implements ICallService {
    @Autowired
    private CallMapper callMapper;
    @Override
    public IPage<Call> pagination(MyPage<Call> page, QCall q) {
        return callMapper.pagination(page, q);
    }

    //删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        callMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Result<Object> findAll(int userId) {
        return success(callMapper.findAll(userId));
    }

    @Override
    public Result<Object> launch(Call call) {
        //判断对方目前状态
        call.setTsCreate(getDate());
        save(call);
        return success(call);
    }

    @Override
    public List<Call> findUnFinished() {
        LambdaQueryWrapper<Call> q = new LambdaQueryWrapper<>();
        List<Integer> statusList = Arrays.asList(Call.Status.Waiting.getCode(),Call.Status.Connected.getCode());
        q.in(Call::getStatus,statusList);
        return list(q);
    }
}
