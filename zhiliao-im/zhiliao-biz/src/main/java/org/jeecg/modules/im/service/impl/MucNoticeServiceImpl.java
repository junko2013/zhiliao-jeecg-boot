package org.jeecg.modules.im.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CacheConstant;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.base.constant.MsgType;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.base.xmpp.MessageBean;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.entity.MucNotice;
import org.jeecg.modules.im.entity.query_helper.QMucNotice;
import org.jeecg.modules.im.mapper.MucNoticeMapper;
import org.jeecg.modules.im.service.MucMemberService;
import org.jeecg.modules.im.service.MucNoticeService;
import org.jeecg.modules.im.service.MucPermissionService;
import org.jeecg.modules.im.service.XMPPService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 群公告 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-02-05
 */
@Service
public class MucNoticeServiceImpl extends BaseServiceImpl<MucNoticeMapper, MucNotice> implements MucNoticeService {
    @Autowired
    private MucNoticeMapper noticeMapper;
    @Resource
    private MucMemberService memberService;
    @Resource
    private MucPermissionService permissionService;
    @Resource
    private XMPPService xmppService;
    @Override
    public Result<Object> createOrUpdate(QMucNotice notice) {
        if(notice.getIsPin()){
            notice.setTsPin(getTs());
        }else{
            notice.setTsPin(0L);
        }
        MucMember creator = memberService.findByMucIdOfUser(notice.getMucId(),notice.getUserId());
        if(notice.getId()==0){
            notice.setTsCreate(getTs());
            if(creator.getRole()<MucMember.Role.Manager.getCode()){
                return fail("没有权限");
            }
            if(creator.getRole()==MucMember.Role.Manager.getCode()){
                MucPermission permission = permissionService.findByManager(notice.getMucId(),notice.getUserId());
                if(!permission.getModifyNotice()){
                    return fail("权限不足");
                }
            }
            notice.setCreatorId(creator.getId());
            if(!save(notice)){
                return fail("添加失败");
            }
            //发送发布群公告
            MessageBean msg = new MessageBean();
            msg.setUserId(creator.getUserId());
            msg.setType(MsgType.mucNotice.getType());
            msg.setMucId(notice.getMucId());
            msg.setContent(JSON.toJSONString(Kv.by("type","create").set("notice",notice)));
            xmppService.sendMucMsg(msg);
        }else{
            if(getById(notice.getId())==null){
                return fail("公告不存在或已被删除！");
            }
            if(!updateById(notice)){
                return fail("更新失败");
            }
            //发送更新群公告
            MessageBean msg = new MessageBean();
            msg.setUserId(creator.getUserId());
            msg.setType(MsgType.mucNotice.getType());
            msg.setMucId(notice.getMucId());
            msg.setContent(JSON.toJSONString(Kv.by("type","update").set("notice",notice)));
            xmppService.sendMucMsg(msg);
        }
        return success();
    }

    @Override
    public MucNotice findById(Integer id) {
        return noticeMapper.selectById(id);
    }

    @Override
    public Result<Object> del(String ids) {
        if(isEmpty(ids)){
            return fail();
        }
        noticeMapper.deleteBatchIds(Arrays.asList(StringUtils.split(ids,",")));
        return success();
    }

    @Override
    public List<MucNotice> queryLogicDeleted() {
        return this.queryLogicDeleted(null);
    }

    @Override
    public List<MucNotice> queryLogicDeleted(LambdaQueryWrapper<MucNotice> wrapper) {
        if (wrapper == null) {
            wrapper = new LambdaQueryWrapper<>();
        }
        wrapper.eq(MucNotice::getDelFlag, CommonConstant.DEL_FLAG_1);
        return noticeMapper.selectLogicDeleted(wrapper);
    }

    @Override
    @CacheEvict(value={CacheConstant.SYS_USERS_CACHE}, allEntries=true)
    public boolean revertLogicDeleted(List<String> ids) {
        return noticeMapper.revertLogicDeleted(ids) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean removeLogicDeleted(List<String> ids) {
        return noticeMapper.deleteLogicDeleted(ids)!=0;
    }

    @Override
    public List<MucNotice> findByMuc(Integer mucId) {
        QMucNotice q = new QMucNotice();
        q.setMucId(mucId);
        return noticeMapper.findAll(q);
    }
}
