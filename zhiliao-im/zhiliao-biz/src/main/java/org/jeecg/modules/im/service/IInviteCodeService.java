package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.InviteCode;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.im.entity.query_helper.QInviteCode;

/**
 * <p>
 * 用户邀请码 服务类
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
public interface IInviteCodeService extends IService<InviteCode> {
    InviteCode findByCode(String code);
    Result<Object> checkCode(String code);
    Result<Object> createOrUpdate(InviteCode code);
    Result<Object> del(String ids);
    IPage<InviteCode> pagination(MyPage<InviteCode> page, QInviteCode q);
}
