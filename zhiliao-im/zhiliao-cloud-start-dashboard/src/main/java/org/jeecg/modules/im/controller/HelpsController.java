package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Helps;
import org.jeecg.modules.im.service.HelpsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 帮助
 */
@RestController
@RequestMapping("/im/helps")
public class HelpsController extends BaseBackController {
    @Resource
    private HelpsService helpsService;

    @RequestMapping("/pagination")
    public Result<IPage<Helps>> queryPageList(Helps helps, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<Helps>> result = new Result<>();
        QueryWrapper<Helps> q = QueryGenerator.initQueryWrapper(helps, req.getParameterMap());
        Page<Helps> page = new Page<>(pageNo, pageSize);
        IPage<Helps> pageList = helpsService.page(page, q);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody Helps helps){
        return helpsService.createOrUpdate(helps);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(helpsService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return helpsService.del(ids);
    }
    /**
     * 获取被逻辑删除的帮助列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Helps> logicDeletedUserList = helpsService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的帮助
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            helpsService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除帮助
     *
     * @param ids 被删除的帮助ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            helpsService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
