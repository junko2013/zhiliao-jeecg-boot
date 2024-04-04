package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.MucInvite;
import org.jeecg.modules.im.service.MucInviteService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 群组邀请记录
 */
@RestController
@RequestMapping("/im/mucInvite")
public class MucInviteController extends BaseBackController {
    @Resource
    private MucInviteService inviteService;

    @RequestMapping("/pagination")
    public Result<IPage<MucInvite>> queryPageList(MucInvite mucInvite, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<MucInvite>> result = new Result<>();
        QueryWrapper<MucInvite> q = QueryGenerator.initQueryWrapper(mucInvite, req.getParameterMap());
        q.eq("server_id",getServerId());
        Page<MucInvite> page = new Page<>(pageNo, pageSize);
        IPage<MucInvite> pageList = inviteService.page(page, q);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(inviteService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return inviteService.del(ids);
    }

    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<MucInvite> logicDeletedUserList = inviteService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            inviteService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            inviteService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }

}
