package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.entity.InviteCode;
import org.jeecg.modules.im.entity.query_helper.QInviteCode;
import org.jeecg.modules.im.mapper.InviteCodeMapper;
import org.jeecg.modules.im.service.IServerConfigService;
import org.jeecg.modules.im.service.IInviteCodeService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * <p>
 * 用户邀请码 服务实现类
 * </p>
 *
 * @author junko
 * @since 2023-01-12
 */
@Service
public class InviteCodeServiceImpl extends BaseServiceImpl<InviteCodeMapper, InviteCode> implements IInviteCodeService {

    @Autowired
    private InviteCodeMapper inviteCodeMapper;
    @Resource
    private IServerConfigService serverConfigService;
    @Override
    public InviteCode findByCode(String code) {
        return inviteCodeMapper.findByCode(code);
    }

    @Override
    public Result<Object> checkCode(String code) {
        ServerConfig config = getServerConfig();
        if(config.getInviteCodeType().equals(InviteCode.Type.require.getCode())&&isEmpty(code)){
            return fail("邀请码必填");
        }
        if(!isEmpty(code)){
            InviteCode inviteCode = findByCode(code);
            if(inviteCode==null){
                return fail("邀请码不存在");
            }
            if(!inviteCode.getEnable()){
                return fail("邀请码已被禁用");
            }
            if(inviteCode.getMaxTimes()> -1){
                if(inviteCode.getTimes()>=inviteCode.getMaxTimes()){
                    return fail("邀请码已不可用");
                }
            }
            return success(inviteCode);
        }
        return success();
    }

    @Override
    public IPage<InviteCode> pagination(MyPage<InviteCode> page, QInviteCode q) {
        return inviteCodeMapper.pagination(page,q);
    }
    //逻辑删除
    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        inviteCodeMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public Result<Object> createOrUpdate(InviteCode code) {
        if(code.getId()==null){
            code.setTsCreate(getDate());
            if(!save(code)){
                return fail("添加失败");
            }
        }else{
            if(!updateById(code)){
                return fail("更新失败");
            }
        }
        return success();
    }
}
