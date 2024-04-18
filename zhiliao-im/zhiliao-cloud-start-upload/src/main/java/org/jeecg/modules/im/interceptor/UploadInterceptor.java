package org.jeecg.modules.im.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.IBlockIpService;
import org.jeecg.modules.im.service.IDeviceService;
import org.jeecg.modules.im.service.IUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * 文件上传访问拦截器
 */
@Slf4j
public class UploadInterceptor implements HandlerInterceptor {
    @Resource
    private IUserService IUserService;
    @Resource
    private IBlockIpService IBlockIpService;
    @Value("${spring.profiles.active}")
    private String evn;
    @Lazy
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private ISysBaseAPI sysBaseApi;
    @Resource
    private IDeviceService IDeviceService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestIp = IPUtil.getIpAddr(request);
        Result<Object> result = IBlockIpService.checkIp(requestIp);
        if (!result.isSuccess()) {
            throw new BusinessException(requestIp+"已被禁止访问");
        }
        //判断方法是否需要校验token
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            // 从方法处理器中获取出要调用的方法
            Method method = handlerMethod.getMethod();
            // 获取出方法上的Access注解
            NoNeedUserToken noNeedUserToken = method.getAnnotation(NoNeedUserToken.class);
            if (noNeedUserToken != null) {
                // 如果注解为null说明不需要拦截, 直接放过
                return true;
            }
        }
        String token = request.getHeader(CommonConstant.X_ACCESS_TOKEN);
        if (oConvertUtils.isEmpty(token)) {
            token = request.getParameter("token");
        }
        //管理后台
        if(oConvertUtils.isNotEmpty(token)){
            checkAdminTokenIsEffect(token);
            return true;
        }
        //用户端
        String deviceDetail = request.getHeader(ConstantWeb.DEVICE_DETAIL);
        String devicePlatform = request.getHeader(ConstantWeb.DEVICE_PLATFORM);
        String deviceNo = request.getHeader(ConstantWeb.DEVICE_NO);
        String authHeader = request.getHeader(CommonConstant.AUTHORIZATION);
        if (!oConvertUtils.isEmpty(authHeader)&&authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            checkUserTokenIsEffect(token,deviceDetail,deviceNo,devicePlatform);
        }
        return true;
    }


    /**
     * 校验token的有效性
     *
     * @param token
     */
    public void checkUserTokenIsEffect(String token,String deviceDetail,String deviceNo,String devicePlatform) throws AuthenticationException {
        // 解密获得userId，用于和数据库进行对比
        Integer userId = JwtUtilApp.verify(token);
        if (userId == null) {
            throw new AuthenticationException("invalid");
        }

        // 查询用户信息
        log.debug("———校验token是否有效————checkUserTokenIsEffect——————— "+ token);
        User user = IUserService.findById(userId);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        // 判断用户状态
        if (user.getTsLocked()>0) {
            throw new AuthenticationException("账号已被锁定");
        }
        Device device = IDeviceService.findByPlatform(deviceNo, devicePlatform,deviceDetail, user);
        if(device==null){
            throw new AuthenticationException("设备未登录过");
        }
        if(device.getTsDisabled()!=null){
            throw new AuthenticationException("当前设备已被禁用");
        }
    }

    /**
     * 校验管理员token的有效性
     *
     * @param token
     */
    public void checkAdminTokenIsEffect(String token) throws AuthenticationException {
        // 解密获得userId，用于和数据库进行对比
        String username = JwtUtil.getUsername(token);
        if (oConvertUtils.isEmpty(username)) {
            throw new AuthenticationException("token无效");
        }

        // 查询用户信息
        log.debug("———校验token是否有效————checkAdminTokenIsEffect——————— "+ token);
        LoginUser sysUser = sysBaseApi.getUserByName(username);
        if(sysUser==null||CommonConstant.DEL_FLAG_1.equals(sysUser.getDelFlag())) {
            throw new AuthenticationException("管理员不存在");
        }
        if(CommonConstant.USER_FREEZE.equals(sysUser.getStatus())){
            throw new AuthenticationException("管理员已被冻结");
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }



}