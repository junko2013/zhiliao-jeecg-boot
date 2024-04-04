package org.jeecg.modules.im.controller;


import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.api.ISysBaseAPI;
import org.jeecg.modules.im.anotation.NoNeedServerSet;
import org.jeecg.modules.im.base.util.UUIDTool;
import org.jeecg.modules.im.base.vo.MyPage;
import org.jeecg.modules.im.entity.Server;
import org.jeecg.modules.im.entity.query_helper.QServer;
import org.jeecg.modules.im.service.ServerService;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/im/server")
public class ServerController extends BaseBackController {
    @Resource
    private ServerService serverService;

    @Resource
    private ISysBaseAPI sysBaseAPI;

    @NoNeedServerSet
    //获取租户的服务器列表
    @RequestMapping("/getCurrentTenantServers")
    public Result<Object> getCurrentTenantServers(){
        QServer q = new QServer();
        //如果不是超管，查询当前租户下的服务器
        if(!sysBaseAPI.getUserRoleSet(getCurrentAdminUserName()).contains("admin")){
            q.setTenantId(getTenantId());
        }
        return success(serverService.findAll(q));
    }
    @NoNeedServerSet
    @RequestMapping("/pagination")
    public Result<Object> list(QServer q){
        if(!sysBaseAPI.getUserRoleSet(getCurrentAdminUserName()).contains("admin")){
            q.setTenantId(getTenantId());
        }
        return success(serverService.pagination(new MyPage<>(getPage(),getPageSize()),q));
    }


    /**
     * 创建或更新
     */
    @NoNeedServerSet
    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody @Validated Server server, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return fail(bindingResult.getAllErrors().get(0));
        }
        return serverService.createOrUpdate(server);
    }

    @NoNeedServerSet
    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(serverService.findById(id));
    }
    @NoNeedServerSet
    @RequestMapping("/getAccessToken")
    public Result<Object> getAccessToken(){
        return success(UUIDTool.getUUID2());
    }

    @NoNeedServerSet
    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return serverService.del(ids);
    }

    /**
     * 获取被逻辑删除的服务器列表，无分页
     *
     * @return logicDeletedUserList
     */
    @NoNeedServerSet
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Server> logicDeletedUserList = serverService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的服务器
     *
     * @param jsonObject
     * @return
     */
    @NoNeedServerSet
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            serverService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除服务器
     *
     * @param ids 被删除的服务器ID，多个id用半角逗号分割
     * @return
     */
    @NoNeedServerSet
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            serverService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
    
}
