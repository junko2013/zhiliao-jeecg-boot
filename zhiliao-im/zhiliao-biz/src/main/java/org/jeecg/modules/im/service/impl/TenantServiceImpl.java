package org.jeecg.modules.im.service.impl;

import org.jeecg.modules.im.entity.Tenant;
import org.jeecg.modules.im.mapper.TenantMapper;
import org.jeecg.modules.im.service.ITenantService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 多租户信息表 服务实现类
 * </p>
 *
 * @author junko
 * @since 2024-03-24
 */
@Service
public class TenantServiceImpl extends ServiceImpl<TenantMapper, Tenant> implements ITenantService {

}
