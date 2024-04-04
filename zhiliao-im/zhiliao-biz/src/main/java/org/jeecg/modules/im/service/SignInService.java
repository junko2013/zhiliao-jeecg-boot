package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.SignIn;
import org.jeecg.modules.im.entity.query_helper.QSignIn;

/**
 * <p>
 * 签到 服务类
 * </p>
 *
 * @author junko
 * @since 2021-02-10
 */
public interface SignInService extends IService<SignIn> {
    IPage<SignIn> pagination(MyPage<SignIn> page, QSignIn q);

    /**
     * 查询用户某天的签到记录
     */
    SignIn findByDateOfUser(String date, Integer userId);

    /**
     * 签到信息
     */
    Result<Object> info(Integer userId);

    /**
     * 查询用户指定日期内签到数据
     * @param userId
     * @return
     */
    Kv getSignDatesInfo(Integer userId);
    /**
     * 执行签到
     */
    Result<Object> sign(Integer userId);
    //补签
    Result<Object> makeup(String date,Integer userId);

    //查询用户累计签到天数
    Integer getTotalDays(Integer userId);
}
