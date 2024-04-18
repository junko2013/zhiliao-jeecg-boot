package org.jeecg.modules.im.service.base;

import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.IUserService;

import javax.annotation.Resource;

public class BaseUploadCtrl extends BaseController {
    @Resource
    private IUserService IUserService;
    /**
     * 根据token获取当前用户
     *
     * @return
     */
    protected User getCurrentUser() {
        String authHeader = request.getHeader(CommonConstant.AUTHORIZATION);
        if (!oConvertUtils.isEmpty(authHeader)&&authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // 解密获得userId，用于和数据库进行对比
            Integer userId = JwtUtilApp.verify(token);
            if (userId == null) {
                throw new AuthenticationException("token非法无效!");
            }

            // 查询用户信息
            User user = IUserService.findById(userId);
            if (user == null) {
                throw new AuthenticationException("用户不存在");
            }
            // 判断用户状态
            if (user.getTsLocked()>0) {
                throw new AuthenticationException("账号已被锁定");
            }
            return user;
        }
        return null;
    }

    /**
     * 根据token获取当前用户id
     *
     * @return
     */
    protected Integer getCurrentUserId() {
        return getCurrentUser()!=null?getCurrentUser().getId():null;
    }

    //当前管理员账号
    protected String getAdmin() {
        try {
            return JwtUtil.getUserNameByToken(request);
        }catch(Exception e){
            return null;
        }
    }
}
