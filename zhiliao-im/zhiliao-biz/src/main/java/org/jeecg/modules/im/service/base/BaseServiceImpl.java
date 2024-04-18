package org.jeecg.modules.im.service.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.im.base.constant.ConstantWeb;
import org.jeecg.modules.im.base.util.IPUtil;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.ServerConfig;
import org.jeecg.modules.im.service.IServerConfigService;
import org.jeecg.modules.im.service.IServerService;
import org.jeecg.modules.im.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

/**
 * 自定义基础service实现，抽取公用代码
 * @param <M>
 * @param <T>
 */
public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M,T> {

    @Value("${spring.profiles.active}")
    private String evn;
    @Autowired
    protected HttpServletRequest request;
    @Resource
    private IServerService IServerService;
    @Resource
    private IServerConfigService serverConfigService;
    @Resource
    private IUserService IUserService;
    @Autowired
    private MessageSource messageSource;

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
    public Boolean isEqual(String str1,String str2) {
        return StringUtils.equals(str1,str2);
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

    protected Result<Object> success() {
        return Result.OK(messageSource.getMessage("操作成功",null, LocaleContextHolder.getLocale()));
    }

    protected Result<Object> success(String msg) {
        try {
            String message = messageSource.getMessage(msg, null, LocaleContextHolder.getLocale());
            return Result.OK(message);
        }catch (Exception e){

        }
        return Result.OK(msg);
    }
    protected Result<Object> success(String msg,Object data) {
        try {
            String message = messageSource.getMessage(msg, null, LocaleContextHolder.getLocale());
            return Result.error(message,data);
        }catch (Exception e){

        }
        return Result.OK(msg,data);
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

    protected String getIp(){
        String ip = request.getHeader(ConstantWeb.IP);
        if(StringUtils.isBlank(ip)){
            ip = IPUtil.getIpAddr(request);
        }
        return ip;
    }
    protected String getIpInfo(){
        return IPUtil.getCityInfo(getIp());
    }
    protected Integer getServerId(){
        return Integer.valueOf(request.getHeader(ConstantWeb.SERVER_ID));
    }
    protected Server getServer(){
        return IServerService.findById(getServerId());
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

    //获取当前时间戳
    protected Long getTs(){
        return new Date().getTime();
    }
    //获取当前时间戳
    protected Date getDate(){
        return new Date();
    }

//    /**
//     * 根据token获取当前用户
//     *
//     * @return
//     */
//    protected User getCurrentUser() {
//        Integer userId = getCurrentUserId();
//        return userId==null?null:userService.findById(userId);
//    }
//
//    /**
//     * 根据token获取当前用户id
//     *
//     * @return
//     */
//    protected Integer getCurrentUserId() {
//        try {
//            return JwtUtilApp.getUserId(request);
//        }catch(Exception e){
//            return null;
//        }
//    }
//    protected String getCurrentAdminId() {
//        try {
//            return JwtUtil.getUserNameByToken(request);
//        }catch(Exception e){
//            return null;
//        }
//    }

}
