package org.jeecg.modules.im.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.util.RedisUtil;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.anotation.NoNeedServerSet;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.constant.ConstantZhiLiao;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.jeecg.modules.im.base.exception.IpDenyException;
import org.jeecg.modules.im.base.exception.ServerException;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.base.util.ToolWeb;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * api访问拦截器
 */
@Slf4j
public class ApiInterceptor implements HandlerInterceptor {
    @Resource
    private ParamService paramService;
    @Resource
    private UserService userService;
    @Resource
    private DeviceService deviceService;
    @Resource
    private BlockIpService blockIpService;
    @Resource
    private ServerService serverService;
    @Value("${spring.profiles.active}")
    private String evn;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String actionKey = ToolWeb.getActionKey(request);
        //过滤swagger
        if(actionKey.startsWith("/a/ws/")||actionKey.equals("/")||actionKey.equals("/error")||actionKey.equals("/doc.html")||actionKey.startsWith("/webjars")||actionKey.startsWith("/swagger-resources")||actionKey.startsWith("/v2/api-docs")){
            return true;
        }
        //判断方法是否需要校验token
        if(handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            NoNeedServerSet noNeedServerSet = method.getAnnotation(NoNeedServerSet.class);
            //为空表示需要校验服务器
            if (noNeedServerSet == null) {
                //校验服务器
                Integer serverId = Integer.valueOf(request.getHeader(ConstantWeb.SERVER_ID));
                String serverAccessToken = request.getHeader(ConstantWeb.SERVER_ACCESS_TOKEN);
                Server server = serverService.findById(serverId);
                if(server==null||server.getDelFlag()==1){
                    throw new ServerException("Server not found!");
                }
                if(server.getStatus()==0){
                    throw new ServerException("Server is locked!");
                }
                if(server.getTsStop()<new Date().getTime()){
                    throw new ServerException("Server has expired!");
                }
                if(!server.getAccessToken().equals(serverAccessToken)){
                    throw new ServerException("Server access denied!");
                }
            }
            NoNeedUserToken noNeedUserToken = method.getAnnotation(NoNeedUserToken.class);
            //为空表示需要校验用户令牌
            if (noNeedUserToken == null) {
                String deviceDetail = request.getHeader(ConstantWeb.DEVICE_DETAIL);
                String devicePlatform = request.getHeader(ConstantWeb.DEVICE_PLATFORM);
                String deviceNo = request.getHeader(ConstantWeb.DEVICE_NO);
                String authHeader = request.getHeader(CommonConstant.AUTHORIZATION);
                //校验用户令牌
                if (!oConvertUtils.isEmpty(authHeader)&&authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    checkUserTokenIsEffect(token,deviceDetail,deviceNo,devicePlatform);
                }
            }
        }
        //校验ip
        String requestIp = request.getHeader(ConstantWeb.IP);
        if(StringUtils.isBlank(requestIp)){
            requestIp = IPUtil.getIpAddr(request);
        }
        Result<Object> result = blockIpService.checkIp(requestIp);
        if (!result.isSuccess()) {
            throw new IpDenyException(requestIp+"已被禁止访问");
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
        User user = userService.findById(userId);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        // 判断用户状态
        if (user.getTsLocked()>0) {
            throw new AuthenticationException("账号已被锁定");
        }
        Device device = deviceService.findByPlatform(deviceNo, devicePlatform,deviceDetail, user);
        if(device==null){
            throw new AuthenticationException("设备未登录过");
        }
        if(device.getTsDisabled()>0){
            throw new AuthenticationException("当前设备已被禁用");
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }



}