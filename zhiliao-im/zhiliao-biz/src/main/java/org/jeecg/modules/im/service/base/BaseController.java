package org.jeecg.modules.im.service.base;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.service.ParamService;
import org.jeecg.modules.im.service.ServerConfigService;
import org.jeecg.modules.im.service.ServerService;
import org.jeecg.modules.im.service.UserService;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@Controller
@Data
public class BaseController {
    @Value("${spring.profiles.active}")
    private String evn;
    @Autowired
    protected HttpServletRequest request;
    @Resource
    private ParamService paramService;
    @Resource
    private UserService userService;
    @Resource
    private ServerService serverService;
    @Resource
    private ServerConfigService serverConfigService;
    @Autowired
    private MessageSource messageSource;

    private int page;
    private int pageSize;
    private String column;
    private String order;


    //公共参数
    @ModelAttribute
    public void init(
            @RequestParam(required = false, defaultValue = "1") int pageNo,
            @RequestParam(required = false, defaultValue = "10") int pageSize
            ) {
        setPage(pageNo);
        setPageSize(pageSize);
    }

    protected HttpSession getSession() {
        return request.getSession();
    }

    protected void setSessionAttr(String name, Object object) {
        getSession().setAttribute(name, object);
    }

    protected Object getSessionAttr(String name) {
        return getSession().getAttribute(name);
    }

    protected void setAttr(String key, Object obj) {
        request.setAttribute(key, obj);
    }

    protected String getPara(String name) {
        return request.getParameter(name);
    }

    protected String getIp(){
        String ip = request.getHeader(ConstantWeb.IP);
        if(StringUtils.isBlank(ip)){
            ip = IPUtil.getIpAddr(request);
        }
        return ip;
    }
    protected Integer getServerId(){
        return Integer.valueOf(request.getHeader(ConstantWeb.SERVER_ID));
    }
    protected Server getServer(){
        return  serverService.findById(getServerId());
    }
    protected Integer getServerAccessToken(){
        return Integer.valueOf(request.getHeader(ConstantWeb.SERVER_ACCESS_TOKEN));
    }
    protected ServerConfig getServerConfig(){
        return  serverConfigService.get(getServer().getId());
    }


    protected String getDeviceNo(){
        return request.getHeader(ConstantWeb.DEVICE_NO);
    }
    protected String getJPushId(){
        return request.getHeader(ConstantWeb.JPUSH_ID);
    }
    protected String getDeviceName(){
        try {
            return URLDecoder.decode(request.getHeader(ConstantWeb.DEVICE_NAME),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    protected String getDeviceDetail(){
        try {
            return URLDecoder.decode(request.getHeader(ConstantWeb.DEVICE_DETAIL),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    //当前设备平台
    protected String getDevicePlatform(){
        return request.getHeader(ConstantWeb.DEVICE_PLATFORM);
    }
    protected String getDeviceSystemVersion(){
        return request.getHeader(ConstantWeb.DEVICE_SYS_VER);
    }
    protected Boolean getDeviceIsPhysic(){
        return Boolean.valueOf(request.getHeader(ConstantWeb.DEVICE_IS_PHYSIC));
    }
    protected String getClientVer(){
        return request.getHeader(ConstantWeb.DEVICE_CLIENT_VER);
    }
    protected String getLocationLongitude(){
        return request.getHeader(ConstantWeb.LOCATION_LONGITUDE);
    }
    protected String getLocationLatitude(){
        return request.getHeader(ConstantWeb.LOCATION_LATITUDE);
    }



    protected Integer getParaToInt(String name) {
        try {
            return Integer.parseInt(getPara(name));
        } catch (Exception e) {
            return null;
        }
    }

    protected Integer getParaToInt(String name, Integer defaultVal) {
        try {
            return Integer.parseInt(getPara(name));
        } catch (Exception e) {
            return defaultVal;
        }
    }

    protected Long getParaToLong(String name) {
        try {
            return Long.parseLong(getPara(name));
        } catch (Exception e) {
            return null;
        }
    }

    protected Boolean getParaToBool(String name) {
        String value = getPara(name);
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        return Boolean.valueOf(getPara(name));
    }

    protected Boolean getParaToBool(String name, boolean defaultValue) {
        Object value = getPara(name);
        if (value == null) {
            return defaultValue;
        }
        return Boolean.valueOf(getPara(name));
    }

    protected Double getParaToDouble(String name) {
        return Double.parseDouble(getPara(name));
    }

    protected String getPara(String name, String defaultValue) {
        String result = getPara(name);
        return result != null && !"".equals(result) ? result : defaultValue;
    }

    public BaseController keepPara() {
        Map<String, String[]> map = this.request.getParameterMap();
        for (Object o : map.entrySet()) {
            Map.Entry e = (Map.Entry) o;
            String[] values = (String[]) e.getValue();
            if (values.length == 1) {
                this.setAttr((String) e.getKey(), values[0]);
            } else {
                this.setAttr((String) e.getKey(), values);
            }
        }
        return this;
    }

    public Boolean setContainsElementInOtherSet(Set<Integer> set1, Set<Integer> set2) {
        Iterator<Integer> it = set1.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (set2.contains(obj)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    public Boolean equals(String str1, String str2) {
        return StringUtils.equals(str1, str2);
    }

    public Boolean equals(Integer i1, Integer i2) {
        return i1 - i2 == 0;
    }

    public Boolean equalsIgnoreCase(String str1, String str2) {
        return StringUtils.equalsIgnoreCase(str1, str2);
    }

    public Boolean isEmpty(String str) {
        return StringUtils.isEmpty(str);
    }

    public Boolean isNotEmpty(String str) {
        return StringUtils.isNotEmpty(str);
    }

    protected Result<Object> fail() {
        return Result.error(messageSource.getMessage("操作失败",null, LocaleContextHolder.getLocale()));
    }

    public Result<Object> fail(String msg) {
        try {
            String message = messageSource.getMessage(msg, null, LocaleContextHolder.getLocale());
            return Result.error(message);
        }catch (Exception e){

        }
        return Result.error(msg);
    }


    protected Result<Object> fail(String msg, Object data) {
        try {
            String message = messageSource.getMessage(msg, null, LocaleContextHolder.getLocale());
            return Result.error(message,data);
        }catch (Exception e){

        }
        return Result.error(msg, data);
    }
    protected Result<Object> fail(int code,String msg) {
        try {
            String message = messageSource.getMessage(msg, null, LocaleContextHolder.getLocale());
            return Result.error(code,message);
        }catch (Exception e){

        }
        return Result.error(code,msg);
    }
    protected Result<Object> fail(int code) {
        return Result.error(code);
    }

    public Result<Object> fail(Object data) {
        return Result.error("error",data);
    }


    protected Result<Object> success() {
        return Result.OK();
    }

    protected Result<Object> success(Object obj) {
        return Result.OK(obj);
    }


    /**
     * 判断项目为测试环境
     * @return
     */
    protected Boolean isTest() {
        return StringUtils.equals(evn.split(",")[evn.split(",").length-1],"test");
    }
    //获取当前时间戳
    protected Long getTs(){
        return new Date().getTime();
    }

}
