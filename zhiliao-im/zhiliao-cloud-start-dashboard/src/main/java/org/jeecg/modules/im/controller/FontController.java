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
import org.jeecg.modules.im.entity.Font;
import org.jeecg.modules.im.service.IFontService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 应用字体
 */
@RestController
@RequestMapping("/im/font")
public class FontController extends BaseBackController<Font, IFontService> {

    /**
     * 分页列表查询
     *
     * @param font
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "字体-分页列表查询")
    @ApiOperation(value="字体-分页列表查询", notes="字体-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Font>> queryPageList(Font font,
                                                                                @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                HttpServletRequest req) {
        QueryWrapper<Font> queryWrapper = QueryGenerator.initQueryWrapper(font, req.getParameterMap());
        Page<Font> page = new Page<Font>(pageNo, pageSize);
        IPage<Font> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param font
     * @return
     */
    @AutoLog(value = "字体-添加")
    @ApiOperation(value="字体-添加", notes="字体-添加")
    @RequiresPermissions("font:im_font:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Font font) {
        service.save(font);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param font
     * @return
     */
    @AutoLog(value = "字体-编辑")
    @ApiOperation(value="字体-编辑", notes="字体-编辑")
    @RequiresPermissions("font:im_font:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Font font) {
        service.updateById(font);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "字体-通过id删除")
    @ApiOperation(value="字体-通过id删除", notes="字体-通过id删除")
    @RequiresPermissions("font:im_font:delete")
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
    @AutoLog(value = "字体-批量删除")
    @ApiOperation(value="字体-批量删除", notes="字体-批量删除")
    @RequiresPermissions("font:im_font:deleteBatch")
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
    //@AutoLog(value = "字体-通过id查询")
    @ApiOperation(value="字体-通过id查询", notes="字体-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Font> queryById(@RequestParam(name="id",required=true) String id) {
        Font font = service.getById(id);
        if(font==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(font);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param font
     */
    @RequiresPermissions("font:im_font:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Font font) {
        return super.exportXls(request, font, Font.class, "字体");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("font:im_font:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Font.class);
    }
    /**
     * 获取被逻辑删除的应用字体列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Font> logicDeletedUserList = service.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的应用字体
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
     * 彻底删除应用字体
     *
     * @param ids 被删除的应用字体ID，多个id用半角逗号分割
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
