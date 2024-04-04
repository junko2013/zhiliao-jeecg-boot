package org.jeecg.modules.im.controller;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.constant.CommonConstant;
import org.jeecg.modules.im.anotation.NoNeedServerSet;
import org.jeecg.modules.im.anotation.NoNeedUserToken;
import org.jeecg.common.util.Kv;
import org.jeecg.modules.im.entity.System;
import org.jeecg.modules.im.entity.*;
import org.jeecg.modules.im.service.*;
import org.jeecg.modules.im.service.base.BaseApiCtrl;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 服务器与配置
 */
@Slf4j
@RestController
@RequestMapping("/a/server")
public class ServerCtrl extends BaseApiCtrl {

    @Resource
    private ClientVerService clientVerService;
    @Resource
    private SysConfigService sysConfigService;
    @Resource
    private ServerConfigService serverConfigService;
    @Resource
    private ParamService paramService;
    @Resource
    private ServerService serverService;
    @Resource
    private UserService userService;
    @Resource
    private LinkService linkService;
    @Resource
    private LocaleService localeService;
    @Resource
    private TenantService tenantService;
    @Resource
    private SystemService systemService;
    @Resource
    private AdverService adverService;
    @Resource
    private NoticeService noticeService;

    @NoNeedServerSet
    @NoNeedUserToken
    @RequestMapping("/checkSystem")
    public Result<Object> checkSystem(@RequestParam String no) {
        //校验系统
        System system = systemService.findByNo(no);
        if(system==null){
            log.error("系统不存在");
            return fail();
        }
        if(Objects.equals(system.getStatus(), CommonConstant.STATUS_0)){
            log.error("系统未启用");
            return fail();
        }
        Kv data = Kv.create();
        data.set("zone", system.getZone());
        data.set("name", system.getName());
        data.set("apiUrl", system.getApiUrl());
        data.set("enableHttps", system.getEnableHttps());
        return success(data);
    }
    @NoNeedServerSet
    @NoNeedUserToken
    @RequestMapping("/checkServer")
    public Result<Object> checkServer(@RequestParam String no) {
        //校验服务器
        Server server = serverService.findByNo(no);
        if(server ==null){
            log.error("服务器不存在");
            return fail();
        }
        if(Objects.equals(server.getStatus(), CommonConstant.STATUS_0)) {
            log.error("服务器未启用");
            return fail();
        }
        if(server.getTsStop()<getTs()){
            log.error("服务器已到期");
            return fail();
        }
        Tenant tenant = tenantService.getById(server.getTenantId());
        if(tenant==null|| tenant.getDelFlag().equals(CommonConstant.DEL_FLAG_0)){
            log.error("租户不存在");
            return fail();
        }
        if(CommonConstant.STATUS_0.equals(tenant.getStatus())){
            log.error("租户未启用");
            return fail();
        }
        Kv data = Kv.create();
        data.set("accessToken", server.getAccessToken());
        data.set("id", server.getId());
        data.set("config", getConfig(server.getId()));
        return success(data);
    }

    @NoNeedUserToken
    @RequestMapping("/config")
    public Result<Object> config() {
        return success(getConfig(null));
    }

    private Kv getConfig(Integer serverId){
        Server server;
        ServerConfig serverConfig;
        if(serverId!=null){
            server = serverService.findById(serverId);
            serverConfig = serverConfigService.get(serverId);
        }else{
            server = getServer();
            serverConfig = getServerConfig();
        }
        SysConfig sysConfig = sysConfigService.get();
        Kv config = Kv.create();
        //app版本
        config.set("versions", clientVerService.findLatestOfAll())
                //腾讯防水墙
                .set("tencentCaptcha",
                        Kv.by("on",paramService.getByName(Param.Name.tencent_captcha_on))
                                .set("appId",paramService.getByName(Param.Name.tencent_captcha_app_id))
                                .set("appSecret",paramService.getByName(Param.Name.tencent_captcha_app_secret)))
                //文件上传地址
                .set("uploadUrl",sysConfig.getUploadUrl())
                .set("maintenance",sysConfig.getMaintenance())
                .set("xmppDomain",sysConfig.getXmppDomain())
                .set("xmppHost",sysConfig.getXmppHost())
                .set("xmppPort",sysConfig.getXmppPort())
                .set("xmppWs",sysConfig.getXmppWs())
                .set("xmppBosh",sysConfig.getXmppBosh())
                .set("enableVideoCall",sysConfig.getEnableVideoCall())
                //agora
                .set("agoraAppId",sysConfig.getAgoraAppId())
                //服务器时间戳
                .set("serverTimestamp",getTs())
                .set("config", serverConfig)
                .set("wsUrl", server.getWsUrl())
                //发现页链接
                .set("links",linkService.findByServerId(server.getId()))
                //语言包
                .set("locales",localeService.findAll())
                //广告
                .set("adver",adverService.findLatest(server.getId()));
        return config;
    }
}
