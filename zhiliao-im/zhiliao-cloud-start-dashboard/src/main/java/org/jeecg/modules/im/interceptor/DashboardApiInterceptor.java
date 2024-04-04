package org.jeecg.modules.im.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtilApp;
import org.jeecg.common.util.oConvertUtils;
import org.jeecg.modules.im.anotation.NoNeedServerSet;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.exception.IpDenyException;
import org.jeecg.modules.im.base.exception.ServerException;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.base.util.ToolWeb;
import org.jeecg.modules.im.entity.Device;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.User;
import org.jeecg.modules.im.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * dashboard api访问拦截器
 */
@Slf4j
public class DashboardApiInterceptor implements HandlerInterceptor {
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
        if(actionKey.equals("/")||actionKey.equals("/doc.html")||actionKey.startsWith("/webjars")||actionKey.startsWith("/swagger-resources")||actionKey.startsWith("/v2-apidocs")){
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
        }
        return true;
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
    }



}