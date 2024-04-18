package org.jeecg.modules.im.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Link;
import org.jeecg.modules.im.entity.query_helper.QLink;
import org.jeecg.modules.im.mapper.LinkMapper;
import org.jeecg.modules.im.service.ILinkService;
import org.jeecg.modules.im.service.base.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author junko
 * @since 2021-10-29
 */
@Service
public class LinkServiceImpl extends BaseServiceImpl<LinkMapper, Link> implements ILinkService {

    @Override
    public List<Link> findByServerId(Integer serverId) {
        LambdaQueryWrapper<Link> q = new LambdaQueryWrapper<>();
        q.eq(Link::getServerId, serverId);
        return list(q);
    }
}
