package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.MucNotice;
import org.jeecg.modules.im.service.MucNoticeService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 群公告
 */
@RestController
@RequestMapping("/im/mucNotice")
public class MucNoticeController extends BaseBackController {
    @Resource
    private MucNoticeService linkService;

    @RequestMapping("/pagination")
    public Result<IPage<MucNotice>> queryPageList(MucNotice inviteLink, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<MucNotice>> result = new Result<>();
        QueryWrapper<MucNotice> q = QueryGenerator.initQueryWrapper(inviteLink, req.getParameterMap());
        Page<MucNotice> page = new Page<>(pageNo, pageSize);
        IPage<MucNotice> pageList = linkService.page(page, q);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(linkService.getById(id));
    }

    @RequestMapping("/lock")
    public Result<Object> lock(@RequestParam Integer id){
        MucNotice emoji = linkService.getById(id);
        if(emoji!=null){
            emoji.setStatus(emoji.getStatus()==1?0:1);
        }
        return success(linkService.updateById(emoji));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return linkService.del(ids);
    }

    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<MucNotice> logicDeletedUserList = linkService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            linkService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            linkService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }

}
