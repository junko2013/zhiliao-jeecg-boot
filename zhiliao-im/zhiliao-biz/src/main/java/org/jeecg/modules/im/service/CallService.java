package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Call;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QCall;

import java.util.List;

/**
 * <p>
 * 通话记录 服务类
 * </p>
 *
 * @author junko
 * @since 2021-12-31
 */
public interface CallService extends IService<Call> {
    IPage<Call> pagination(MyPage<Call> page, QCall q);
    Result<Object> del(String ids);
    Result<Object> findAll(int userId);

    Result<Object> launch(Call call);
    //查询未完成的通话
    List<Call> findUnFinished();
}
