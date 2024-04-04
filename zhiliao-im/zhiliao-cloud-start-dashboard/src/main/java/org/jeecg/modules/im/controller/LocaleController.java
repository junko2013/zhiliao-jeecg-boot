package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Locale;
import org.jeecg.modules.im.service.LocaleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


/**
 * 语言包
 */
@RestController
@RequestMapping("/im/locale")
public class LocaleController extends BaseBackController {
    @Resource
    private LocaleService localeService;

    @RequestMapping("/pagination")
    public Result<IPage<Locale>> queryPageList(Locale locale, @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize, HttpServletRequest req) {
        Result<IPage<Locale>> result = new Result<>();
        QueryWrapper<Locale> queryWrapper = QueryGenerator.initQueryWrapper(locale, req.getParameterMap());
        Page<Locale> page = new Page<>(pageNo, pageSize);
        IPage<Locale> pageList = localeService.page(page, queryWrapper);
        result.setSuccess(true);
        result.setResult(pageList);
        return result;
    }

    @RequestMapping("/createOrUpdate")
    public Result<Object> createOrUpdate(@RequestBody Locale locale){
        return localeService.createOrUpdate(locale);
    }


    @RequestMapping("/detail")
    public Result<Object> detail(@RequestParam Integer id){
        return success(localeService.getById(id));
    }

    @RequestMapping("/del")
    public Result<Object> del(@RequestParam String ids){
        return localeService.del(ids);
    }
    /**
     * 获取被逻辑删除的语言包列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Locale> logicDeletedUserList = localeService.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的语言包
     *
     * @param jsonObject
     * @return
     */
    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            localeService.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    /**
     * 彻底删除语言包
     *
     * @param ids 被删除的语言包ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            localeService.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
