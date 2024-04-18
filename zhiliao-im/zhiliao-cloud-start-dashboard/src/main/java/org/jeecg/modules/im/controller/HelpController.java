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
import org.jeecg.modules.im.entity.Help;
import org.jeecg.modules.im.service.IHelpService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 帮助
 */
@RestController
@RequestMapping("/im/help")
public class HelpController extends BaseBackController<Help, IHelpService> {


    /**
     * 分页列表查询
     *
     * @param help
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "使用帮助-分页列表查询")
    @ApiOperation(value="使用帮助-分页列表查询", notes="使用帮助-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Help>> queryPageList(Help help,
                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                HttpServletRequest req) {
        QueryWrapper<Help> queryWrapper = QueryGenerator.initQueryWrapper(help, req.getParameterMap());
        Page<Help> page = new Page<Help>(pageNo, pageSize);
        IPage<Help> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param help
     * @return
     */
    @AutoLog(value = "使用帮助-添加")
    @ApiOperation(value="使用帮助-添加", notes="使用帮助-添加")
    @RequiresPermissions("help:im_help:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Help help) {
        service.save(help);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param help
     * @return
     */
    @AutoLog(value = "使用帮助-编辑")
    @ApiOperation(value="使用帮助-编辑", notes="使用帮助-编辑")
    @RequiresPermissions("help:im_help:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Help help) {
        service.updateById(help);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "使用帮助-通过id删除")
    @ApiOperation(value="使用帮助-通过id删除", notes="使用帮助-通过id删除")
    @RequiresPermissions("help:im_help:delete")
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
    @AutoLog(value = "使用帮助-批量删除")
    @ApiOperation(value="使用帮助-批量删除", notes="使用帮助-批量删除")
    @RequiresPermissions("help:im_help:deleteBatch")
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
    //@AutoLog(value = "使用帮助-通过id查询")
    @ApiOperation(value="使用帮助-通过id查询", notes="使用帮助-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Help> queryById(@RequestParam(name="id",required=true) String id) {
        Help help = service.getById(id);
        if(help==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(help);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param help
     */
    @RequiresPermissions("help:im_help:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Help help) {
        return super.exportXls(request, help, Help.class, "使用帮助");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("help:im_help:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Help.class);
    }

    /**
     * 获取被逻辑删除的帮助列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Help> logicDeletedUserList = service.queryLogicDeleted();
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
            service.revertLogicDeleted(Arrays.asList(ids.split(",")));
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
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }
}
