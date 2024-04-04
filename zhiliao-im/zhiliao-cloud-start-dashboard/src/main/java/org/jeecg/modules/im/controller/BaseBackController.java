package org.jeecg.modules.im.controller;

import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.constant.TenantConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.service.base.BaseController;
import org.springframework.stereotype.Controller;

import javax.annotation.Resource;

@Controller
public class BaseBackController extends BaseController {

    @Resource
    private ISysBaseAPI sysBaseAPI;
    //获取当前租户
    protected Integer getTenantId(){
        String tenantId = request.getParameter(TenantConstant.TENANT_ID);
        if (tenantId == null) {
            tenantId = oConvertUtils.getString(request.getHeader(CommonConstant.TENANT_ID));
        }
        return Integer.parseInt(tenantId);
    }

    protected String getCurrentAdminUserName() {
        try {
            return JwtUtil.getUserNameByToken(request);
        }catch(Exception e){
            return null;
        }
    }

    protected LoginUser getCurrentAdmin(){
        return sysBaseAPI.getUserByName(getCurrentAdminUserName());
    }
}