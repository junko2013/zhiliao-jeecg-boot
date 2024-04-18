package org.jeecg.modules.im.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.im.entity.Locale;
import org.jeecg.modules.im.service.ILocaleService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 语言包
 */
@RestController
@RequestMapping("/im/locale")
public class LocaleController extends BaseBackController<Locale, ILocaleService> {

    /**
     * 分页列表查询
     *
     * @param locale
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "语言包-分页列表查询")
    @ApiOperation(value="语言包-分页列表查询", notes="语言包-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Locale>> queryPageList(Locale locale,
                                                                                    @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                    @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                    HttpServletRequest req) {
        QueryWrapper<Locale> queryWrapper = QueryGenerator.initQueryWrapper(locale, req.getParameterMap());
        Page<Locale> page = new Page<Locale>(pageNo, pageSize);
        IPage<Locale> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param locale
     * @return
     */
    @AutoLog(value = "语言包-添加")
    @ApiOperation(value="语言包-添加", notes="语言包-添加")
    @RequiresPermissions("locale:im_locale:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Locale locale) {
        service.save(locale);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param locale
     * @return
     */
    @AutoLog(value = "语言包-编辑")
    @ApiOperation(value="语言包-编辑", notes="语言包-编辑")
    @RequiresPermissions("locale:im_locale:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Locale locale) {
        service.updateById(locale);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "语言包-通过id删除")
    @ApiOperation(value="语言包-通过id删除", notes="语言包-通过id删除")
    @RequiresPermissions("locale:im_locale:delete")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name="id",required=true) String id) {
        service.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     *  批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "语言包-批量删除")
    @ApiOperation(value="语言包-批量删除", notes="语言包-批量删除")
    @RequiresPermissions("locale:im_locale:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "语言包-通过id查询")
    @ApiOperation(value="语言包-通过id查询", notes="语言包-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Locale> queryById(@RequestParam(name="id",required=true) String id) {
        Locale locale = service.getById(id);
        if(locale==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(locale);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param locale
     */
    @RequiresPermissions("locale:im_locale:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Locale locale) {
        return super.exportXls(request, locale, Locale.class, "语言包");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("locale:im_locale:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Locale.class);
    }

    /**
     * 获取被逻辑删除的语言包列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Locale> logicDeletedUserList = service.queryLogicDeleted();
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
            service.revertLogicDeleted(Arrays.asList(ids.split(",")));
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
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
