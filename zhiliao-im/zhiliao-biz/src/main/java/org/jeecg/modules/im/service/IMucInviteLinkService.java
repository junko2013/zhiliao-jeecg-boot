package org.jeecg.modules.im.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.entity.MucInviteLink;

import java.util.List;

/**
 * <p>
 * 群组邀请链接 服务类
 * </p>
 *
 * @author junko
 * @since 2023-07-22
 */
public interface IMucInviteLinkService extends IService<MucInviteLink> {
    List<MucInviteLink> findByMuc(Integer mucId);
    Result<Object> createOrUpdate(MucInviteLink link);

    MucInviteLink findById(String id);
    Result<Object> del(String ids);

    List<MucInviteLink> queryLogicDeleted();
    List<MucInviteLink> queryLogicDeleted(LambdaQueryWrapper<MucInviteLink> wrapper);
    boolean revertLogicDeleted(List<String> ids);
    boolean removeLogicDeleted(List<String> ids);
}
