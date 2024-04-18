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
import org.jeecg.modules.im.entity.Adver;
import org.jeecg.modules.im.service.IAdverService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 广告
 */
@RestController
@RequestMapping("/im/adver")
public class AdverController extends BaseBackController<Adver, IAdverService> {


    /**
     * 分页列表查询
     *
     * @param adver
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "广告图-分页列表查询")
    @ApiOperation(value="广告图-分页列表查询", notes="广告图-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<Adver>> queryPageList(Adver adver,
                                                                                  @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                  @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                  HttpServletRequest req) {
        QueryWrapper<Adver> queryWrapper = QueryGenerator.initQueryWrapper(adver, req.getParameterMap());
        queryWrapper.eq("server_id",getServerId());
        Page<Adver> page = new Page<Adver>(pageNo, pageSize);
        IPage<Adver> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param adver
     * @return
     */
    @AutoLog(value = "广告图-添加")
    @ApiOperation(value="广告图-添加", notes="广告图-添加")
    @RequiresPermissions("adver:im_adver:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody Adver adver) {
        adver.setServerId(getServer().getId());
        service.save(adver);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param adver
     * @return
     */
    @AutoLog(value = "广告图-编辑")
    @ApiOperation(value="广告图-编辑", notes="广告图-编辑")
    @RequiresPermissions("adver:im_adver:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody Adver adver) {
        service.updateById(adver);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "广告图-通过id删除")
    @ApiOperation(value="广告图-通过id删除", notes="广告图-通过id删除")
    @RequiresPermissions("adver:im_adver:delete")
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
    @AutoLog(value = "广告图-批量删除")
    @ApiOperation(value="广告图-批量删除", notes="广告图-批量删除")
    @RequiresPermissions("adver:im_adver:deleteBatch")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name="ids",required=true) String ids) {
        this.service.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 获取被逻辑删除的广告列表，无分页
     *
     * @return logicDeletedUserList
     */
    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<Adver> logicDeletedUserList = service.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    /**
     * 还原被逻辑删除的广告
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
     * 彻底删除广告
     *
     * @param ids 被删除的广告ID，多个id用半角逗号分割
     * @return
     */
    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "广告图-通过id查询")
    @ApiOperation(value="广告图-通过id查询", notes="广告图-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<Adver> queryById(@RequestParam(name="id",required=true) String id) {
        Adver adver = service.getById(id);
        if(adver==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(adver);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param adver
     */
    @RequiresPermissions("adver:im_adver:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, Adver adver) {
        return super.exportXls(request, adver, Adver.class, "广告图");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("adver:im_adver:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, Adver.class);
    }
}
