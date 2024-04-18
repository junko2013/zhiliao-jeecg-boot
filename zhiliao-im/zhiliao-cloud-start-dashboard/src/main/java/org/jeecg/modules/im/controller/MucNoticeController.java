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
import org.jeecg.modules.im.entity.MucNotice;
import org.jeecg.modules.im.service.IMucNoticeService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;


/**
 * 群公告
 */
@RestController
@RequestMapping("/im/mucNotice")
public class MucNoticeController extends BaseBackController<MucNotice, IMucNoticeService> {


    /**
     * 分页列表查询
     *
     * @param mucNotice
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "群公告-分页列表查询")
    @ApiOperation(value="群公告-分页列表查询", notes="群公告-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<MucNotice>> queryPageList(MucNotice mucNotice,
                                                                                          @RequestParam(name="pageNo", defaultValue="1") Integer pageNo,
                                                                                          @RequestParam(name="pageSize", defaultValue="10") Integer pageSize,
                                                                                          HttpServletRequest req) {
        QueryWrapper<MucNotice> queryWrapper = QueryGenerator.initQueryWrapper(mucNotice, req.getParameterMap());
        Page<MucNotice> page = new Page<MucNotice>(pageNo, pageSize);
        IPage<MucNotice> pageList = service.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     *   添加
     *
     * @param mucNotice
     * @return
     */
    @AutoLog(value = "群公告-添加")
    @ApiOperation(value="群公告-添加", notes="群公告-添加")
    @RequiresPermissions("mucNotice:im_muc_notice:add")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody MucNotice mucNotice) {
        service.save(mucNotice);
        return Result.OK("添加成功！");
    }

    /**
     *  编辑
     *
     * @param mucNotice
     * @return
     */
    @AutoLog(value = "群公告-编辑")
    @ApiOperation(value="群公告-编辑", notes="群公告-编辑")
    @RequiresPermissions("mucNotice:im_muc_notice:edit")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT,RequestMethod.POST})
    public Result<String> edit(@RequestBody MucNotice mucNotice) {
        service.updateById(mucNotice);
        return Result.OK("编辑成功!");
    }

    /**
     *   通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "群公告-通过id删除")
    @ApiOperation(value="群公告-通过id删除", notes="群公告-通过id删除")
    @RequiresPermissions("mucNotice:im_muc_notice:delete")
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
    @AutoLog(value = "群公告-批量删除")
    @ApiOperation(value="群公告-批量删除", notes="群公告-批量删除")
    @RequiresPermissions("mucNotice:im_muc_notice:deleteBatch")
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
    //@AutoLog(value = "群公告-通过id查询")
    @ApiOperation(value="群公告-通过id查询", notes="群公告-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<MucNotice> queryById(@RequestParam(name="id",required=true) String id) {
        MucNotice mucNotice = service.getById(id);
        if(mucNotice==null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(mucNotice);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param mucNotice
     */
    @RequiresPermissions("mucNotice:im_muc_notice:exportXls")
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, MucNotice mucNotice) {
        return super.exportXls(request, mucNotice, MucNotice.class, "群公告");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequiresPermissions("mucNotice:im_muc_notice:importExcel")
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, MucNotice.class);
    }

    @GetMapping("/recycleBin")
    public Result getRecycleBin() {
        List<MucNotice> logicDeletedUserList = service.queryLogicDeleted();
        return Result.ok(logicDeletedUserList);
    }

    @RequestMapping(value = "/putRecycleBin", method = RequestMethod.PUT)
    public Result putRecycleBin(@RequestBody JSONObject jsonObject, HttpServletRequest request) {
        String ids = jsonObject.getString("ids");
        if (StringUtils.isNotBlank(ids)) {
            service.revertLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("还原成功");
    }

    @RequestMapping(value = "/deleteRecycleBin", method = RequestMethod.DELETE)
    public Result deleteRecycleBin(@RequestParam("ids") String ids) {
        if (StringUtils.isNotBlank(ids)) {
            service.removeLogicDeleted(Arrays.asList(ids.split(",")));
        }
        return Result.ok("删除成功");
    }

}
