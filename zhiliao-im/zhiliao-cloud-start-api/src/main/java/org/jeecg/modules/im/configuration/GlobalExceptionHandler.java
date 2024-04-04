package org.jeecg.modules.im.configuration;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.common.system.util.JwtUtil;
import org.jeecg.common.util.SpringContextUtils;
import org.jeecg.modules.im.base.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String evn;
    @Autowired
    private MessageSource messageSource;

    protected Boolean isTest() {
        return StringUtils.equals(evn.split(",")[evn.split(",").length-1],"test");
    }


    /**
     * 处理自定义的业务异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = BusinessException.class)
    @ResponseBody
    public Result bizExceptionHandler(HttpServletRequest req, BusinessException e) {
        log.error("发生业务异常！原因是：{}", e.getMessage());
        return Result.error(CommonConstant.SC_BIZ_ERROR,e.getMessage());
    }

    /**
     * 处理空指针的异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, NullPointerException e) {
        log.error("发生空指针异常！原因是:", e);
        if(isTest()){
            return Result.error(CommonConstant.SC_NULL_ERROR,e.getCause().getMessage());
        }
        return Result.error(CommonConstant.SC_NULL_ERROR,"null error");
    }
    /**
     * token异常处理
     * 返回401触发令牌刷新
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = AuthenticationException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, AuthenticationException e) {
        log.error("发生token异常！原因是:", e);
        if(e.getMessage().equals("invalid")){
            JwtUtil.responseError(SpringContextUtils.getHttpServletResponse(),401,e.getMessage());
            return null;
        }
        return Result.error(e.getMessage());
    }


    /**
     * 请求方式不正确
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        try {
            String message = messageSource.getMessage("请求方式不支持", null, LocaleContextHolder.getLocale());
            return Result.error(CommonConstant.REQUEST_METHOD_ERROR,message);
        }catch (Exception e2){
            return Result.error(CommonConstant.REQUEST_METHOD_ERROR);
        }
    }
    /**
     * 处理其他异常
     *
     * @param req
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result exceptionHandler(HttpServletRequest req, Exception e) {
        log.error("未知异常！原因是:", e);
        if(!isTest()){
            return Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,"sys error");
        }
        if(e.getCause()!=null){
            return Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,e.getCause().getMessage());
        }
        return Result.error(CommonConstant.SC_INTERNAL_SERVER_ERROR_500,e.getMessage());
    }
}